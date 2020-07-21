package com.jammuwalastore

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jammuwalastore.adapter.CountryAdapter
import com.jammuwalastore.helper.EmailValid.isEmailValid
import com.jammuwalastore.helper.ProgressDialogUtil
import com.jammuwalastore.model.*
import com.students.students.ApiInterface
import com.students.students.NetworkCheck
import com.students.students.RetroBaseClass
import kotlinx.android.synthetic.main.signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by ABC on 11-Apr-20.
 *
 * https://github.com/hiiamrohit/Countries-States-Cities-database
 *
 */


class SignUp : FragmentActivity(), View.OnClickListener {

    private lateinit var dialog: AlertDialog
    private lateinit var mobile: String
    private lateinit var otp: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var addressOne: String
    private lateinit var addressTwo: String
    private lateinit var state: String
    private lateinit var city: String
    private lateinit var postalCode: String

    lateinit var networkCheck: NetworkCheck
    private var countriesModel: CountriesModel? = null
    private var statesModel: StatesModel? = null
    private var citiesModel: CitiesModel? = null
    private var stateArrayList = ArrayList<State>()
    private var cityArrayList = ArrayList<City>()
    private lateinit var statesAdapter: CountryAdapter<State>
    private lateinit var cityAdapter: CountryAdapter<City>
    private var runningSpinner = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        networkCheck = NetworkCheck()
        dialog = ProgressDialogUtil.setProgressDialog(this, "Please wait ...")
        regFullLL.visibility = View.GONE
        verifyLL.visibility = View.VISIBLE
        // click listner
        txt_signin.setOnClickListener(this)
        txt_signin1.setOnClickListener(this)
        btn_verify.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
        val gson = Gson()

        countriesModel =
            gson.fromJson(loadJSONFromAsset("cdata/countries.json"), CountriesModel::class.java)
        statesModel = gson.fromJson(loadJSONFromAsset("cdata/states.json"), StatesModel::class.java)
        citiesModel = gson.fromJson(loadJSONFromAsset("cdata/cities.json"), CitiesModel::class.java)

        // You can create an anonymous listener to handle the event when is selected an spinner item
        edtState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                // Here you get the current item (a User object) that is selected by its position
                //if (runningSpinner) { }
                if (position != 0) {
                    val item = stateArrayList[position]
                    state = item.name.toString()
                    txtState.setText(state)
                    setCityAdapter(item.id.toString(), "")
                    Log.e(
                        "TAG ",
                        " txtState - " + txtState.text.toString() + ", getId " + item.id
                    )
                } else {
                    state = "";
                    txtState.setText(state)
                    setCityAdapter("0", "")
                    city = ""
                    txtCity.setText(city)
                }

            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }

        // You can create an anonymous listener to handle the event when is selected an spinner item
        edtCity.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Here you get the current item (a User object) that is selected by its position

                val user = adapterView.adapter.getItem(position)
                if (user is City) {
                    val item = user as City
                    city = item.name.toString()
                    txtCity.setText(city)
                    Log.e(
                        "TAG ",
                        " txtCity - " + txtCity.text.toString() + ", getId " + item.id
                    )
                }

                //if (runningSpinner) { }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }

        setStatesAdapter("101");


    }


    override fun onClick(v: View?) {
        if (networkCheck.isNetworkAvailable(this)) {
            when (v!!.id) {
                R.id.txt_signin -> {
                    openLoginScreen()
                }
                R.id.txt_signin1 -> {
                    openLoginScreen()
                }
                R.id.btn_verify -> {
                    if (btn_verify.text.toString().trim().equals("Next")) {
                        mobile = edt_mob_et.text.toString().trim()
                        if (mobile.equals("")) {
                            Toast.makeText(
                                this@SignUp,
                                getString(R.string.enter_mob_num),
                                Toast.LENGTH_SHORT
                            ).show()
                        } /*else if (mobile.length > 10) {
                        Toast.makeText(this@SignUp, "Enter Valid Number", Toast.LENGTH_SHORT).show()
                    } else if (mobile.length < 10) {
                        Toast.makeText(this@SignUp, "Enter Valid Number", Toast.LENGTH_SHORT).show()
                    }*/ else {
                            SendOtp()
                        }
                    } else if (btn_verify.text.toString().trim().equals("Verify")) {
                        otp = edt_otp.text.toString().trim()
                        if (otp.equals("")) {
                            Toast.makeText(
                                this@SignUp,
                                getString(R.string.enter_otp),
                                Toast.LENGTH_SHORT
                            ).show()
                        } /*else if (mobile.length > 10) {
                        Toast.makeText(this@SignUp, "Enter Valid Number", Toast.LENGTH_SHORT).show()
                    } else if (mobile.length < 10) {
                        Toast.makeText(this@SignUp, "Enter Valid Number", Toast.LENGTH_SHORT).show()
                    }*/ else {
                            VerifyOtp()
                        }
                    }
                }

                R.id.btn_signup -> {
                    firstName = edt_first_name.text.toString().trim()
                    lastName = edt_last_name.text.toString().trim()
                    email = edt_email.text.toString().trim()
                    password = edt_password.text.toString().trim()
                    confirmPassword = edt_con_password.text.toString().trim()
                    addressOne = edt_address_one.text.toString().trim()
                    addressTwo = edt_address_two.text.toString().trim()
                    state = txtState.text.toString().trim()
                    city = txtCity.text.toString().trim()
                    postalCode = edt_post_code_et.text.toString().trim()

                    if (firstName.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_first_name),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (lastName.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_last_name),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (email.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!email.isEmailValid()) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_valid_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (password.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (confirmPassword.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_confirm_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!confirmPassword.equals(password)) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_confirm_password_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (state.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_state),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (city.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_city),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (postalCode.equals("")) {
                        Toast.makeText(
                            this@SignUp,
                            getString(R.string.enter_postal_code),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        signup()

                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
        }

    }

    private fun openLoginScreen() {

        val loginIntent = Intent(this@SignUp, Login::class.java)
        startActivity(loginIntent)
        finish()
    }


    var SendOtp = {
        dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)
        val jsonObject = JsonObject()
        jsonObject.addProperty("mobile", mobile)
        /*val jsuser = JsonObject()
        jsuser.add("user", jsonObject)*/
        Log.e("SendOtp", "param -  $jsonObject")

        val response: Call<ResponseMessage>? = apiInterface.SendOtp(jsonObject)

        response?.enqueue(object : Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>?,
                response: Response<ResponseMessage>?
            ) {
                dialog.dismiss()
                if (response!!.isSuccessful) {
                    val message = response.body()!!.message
                    Toast.makeText(this@SignUp, message, Toast.LENGTH_SHORT).show()
                    Log.e("SendOtp", "response -" + message)
                    edt_otp.visibility = View.VISIBLE
                    edt_mob_et.visibility = View.GONE
                    btn_verify.setText("Verify")
                } else {
                    //val data = response.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseMessage>() {}.type
                    val errorResponse: ResponseMessage? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("SendOtp", "response -" + errorResponse!!.message)
                    edt_mob_et.setError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<ResponseMessage>?, t: Throwable?) {
                dialog.dismiss()
            }
        })

    }


    var VerifyOtp = {
        dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)
        val jsonObject = JsonObject()
        jsonObject.addProperty("mobile", mobile)
        jsonObject.addProperty("otp", otp)
        /*val jsuser = JsonObject()
        jsuser.add("user", jsonObject)*/
        Log.e("VerifyOtp", "param -  $jsonObject")

        val response: Call<ResponseMessage>? = apiInterface.verifyOtp(jsonObject)

        response?.enqueue(object : Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>?,
                response: Response<ResponseMessage>?
            ) {
                dialog.dismiss()
                if (response!!.isSuccessful) {
                    val message = response.body()!!.message
                    Toast.makeText(this@SignUp, message, Toast.LENGTH_SHORT).show()
                    Log.e("VerifyOtp", "response -" + message)
                    verifyLL.visibility = View.GONE
                    regFullLL.visibility = View.VISIBLE
                } else {
                    //val data = response.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseMessage>() {}.type
                    val errorResponse: ResponseMessage? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("VerifyOtp", "response -" + errorResponse!!.message)
                    edt_otp.setError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<ResponseMessage>?, t: Throwable?) {
                dialog.dismiss()
            }
        })

    }


    var signup = {
        dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)
        val jsonObject = JsonObject()
        jsonObject.addProperty("fname", firstName)
        jsonObject.addProperty("lname", lastName)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("mobile", mobile)
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("confirm_password", confirmPassword)
        jsonObject.addProperty("address", addressOne)
        jsonObject.addProperty("address_1", addressTwo)
        jsonObject.addProperty("state", state)
        jsonObject.addProperty("city", city)
        jsonObject.addProperty("postcode", postalCode)

        Log.e("signup", "param -  $jsonObject")

        val response: Call<ResponseMessage>? = apiInterface.signup(jsonObject)

        response?.enqueue(object : Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>?,
                response: Response<ResponseMessage>?
            ) {
                dialog.dismiss()
                if (response!!.isSuccessful) {
                    val message = response.body()!!.message
                    Toast.makeText(this@SignUp, message, Toast.LENGTH_SHORT).show()
                    Log.e("signup", "response -" + message)
                    openLoginScreen()
                } else {
                    //val data = response.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseMessage>() {}.type
                    val errorResponse: ResponseMessage? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("signup", "response -" + errorResponse!!.message)
                    Toast.makeText(this@SignUp, errorResponse.message, Toast.LENGTH_SHORT).show()
                    //edt_mob_et.setError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<ResponseMessage>?, t: Throwable?) {
                dialog.dismiss()
            }
        })

    }

    fun loadJSONFromAsset(name: String?): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = this@SignUp.getAssets().open(name.toString())
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String(buffer, StandardCharsets.UTF_8)
            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun setStatesAdapter(id: String) {
        stateArrayList = ArrayList<State>()
        for (item in statesModel!!.states!!) {
            if (item.country_id.equals(id)) stateArrayList.add(item)
        }
        statesAdapter = CountryAdapter(this@SignUp, R.layout.spinner_layout, stateArrayList)
        edtState.adapter = statesAdapter

        /*val handler = Handler()
        handler.postDelayed(mSplashHandler, 15000)*/
    }

    internal var mSplashHandler: Runnable = Runnable {
        // your code to do after handler completes
        runningSpinner = true
    }

    private fun setCityAdapter(id: String, from: String) {
        cityArrayList = ArrayList()
        for (item in citiesModel!!.cities!!) {
            if (item.state_id.equals(id)) cityArrayList.add(item)
        }
        Log.e("TAG ", " cityList - " + cityArrayList.size)
        cityAdapter = CountryAdapter(this@SignUp, R.layout.spinner_layout, cityArrayList)
        edtCity.adapter = cityAdapter

        /*if (from.equalsIgnoreCase("xyz")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    txtCountry.setText(mSettingsData.getCountry());
                    txtCountry.setTag(mSettingsData.getCountry());

                    txtState.setText(mSettingsData.getState());
                    txtCity.setText(mSettingsData.getCity());

                    //edtState.setSelection(indexState);
                    //edtCity.setSelection(indexCity);

                    //progressDialog.dismiss();
                }
            }, 3000);
        }*/
    }

}