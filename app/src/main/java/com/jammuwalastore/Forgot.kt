package com.jammuwalastore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jammuwalastore.helper.EmailValid.isEmailValid
import com.jammuwalastore.helper.EmailValid.isPhoneValid
import com.jammuwalastore.helper.ProgressDialogUtil
import com.jammuwalastore.model.ResponseMessage
import com.students.students.ApiInterface
import com.students.students.NetworkCheck
import com.students.students.RetroBaseClass
import kotlinx.android.synthetic.main.forgot.*
import kotlinx.android.synthetic.main.signup.txt_signin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by ABC on 11-Apr-20.
 */


class Forgot : FragmentActivity(), View.OnClickListener {
    lateinit var networkCheck: NetworkCheck
    private lateinit var email: String
    private lateinit var otp: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var dialog: AlertDialog
    var isEmail: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot)
        dialog = ProgressDialogUtil.setProgressDialog(this, "Please wait ...")
        networkCheck = NetworkCheck()

        forgot_edt_email.visibility = View.VISIBLE
        forgot_edt_otp.visibility = View.GONE
        forgot_edt_password.visibility = View.GONE
        forgot_edt_con_password.visibility = View.GONE

        txt_signin.setOnClickListener(this)
        txt_signin1.setOnClickListener(this)
        btn_forgot.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (networkCheck.isNetworkAvailable(this)) {
            when (v!!.id) {
                R.id.txt_signin -> {
                    val loginIntent = Intent(this@Forgot, Login::class.java)
                    startActivity(loginIntent)
                    finish()
                }
                R.id.txt_signin1 -> {
                    val loginIntent = Intent(this@Forgot, Login::class.java)
                    startActivity(loginIntent)
                    finish()
                }

                R.id.btn_forgot -> {

                    if (btn_forgot.text.toString().trim().equals("Forgot Password")) {
                        email = forgot_edt_email.text.toString().trim()
                        when {
                            email == "" -> {
                                Toast.makeText(
                                    this@Forgot,
                                    getString(R.string.enter_email_or_phone),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            email.isEmailValid() -> {
                                isEmail=true
                                sendResetPassword(true)

                            }
                            email.isPhoneValid() -> {
                                isEmail=false
                                sendResetPassword(false)
                            }
                            else -> {
                                Toast.makeText(
                                    this@Forgot,
                                    getString(R.string.enter_valid_email_or_phone),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else if (btn_forgot.text.toString().trim().equals("Verify")) {
                        otp = forgot_edt_otp.text.toString().trim()
                        password = forgot_edt_password.text.toString().trim()
                        confirmPassword = forgot_edt_con_password.text.toString().trim()
                        if (otp.equals("")) {
                            Toast.makeText(
                                this@Forgot,
                                getString(R.string.enter_otp),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (password.equals("")) {
                            Toast.makeText(
                                this@Forgot,
                                getString(R.string.enter_password),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (confirmPassword.equals("")) {
                            Toast.makeText(
                                this@Forgot,
                                getString(R.string.enter_confirm_password),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!confirmPassword.equals(password)) {
                            Toast.makeText(
                                this@Forgot,
                                getString(R.string.enter_confirm_password_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            resetpassword()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendResetPassword(isEmail: Boolean) {
        dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)
        val jsonObject = JsonObject()
        if (isEmail) {
            jsonObject.addProperty("email", email)
        } else {
            jsonObject.addProperty("mobile", email)
        }
        /*val jsuser = JsonObject()
        jsuser.add("user", jsonObject)*/
        Log.e("SendResetPassword ", "param -  $jsonObject")

        val response: Call<ResponseMessage>? = apiInterface.SendResetPassword(jsonObject)

        response?.enqueue(object : Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>?,
                response: Response<ResponseMessage>?
            ) {
                dialog.dismiss()
                if (response!!.isSuccessful) {
                    val message = response.body()!!.message
                    Toast.makeText(this@Forgot, message, Toast.LENGTH_SHORT).show()
                    Log.e("SendResetPassword ", "response -" + message)
                    forgot_edt_email.visibility = View.GONE
                    forgot_edt_otp.visibility = View.VISIBLE
                    forgot_edt_password.visibility = View.VISIBLE
                    forgot_edt_con_password.visibility = View.VISIBLE
                    btn_forgot.setText("Verify")
                } else {
                    //val data = response.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseMessage>() {}.type
                    val errorResponse: ResponseMessage? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("SendResetPassword ", "response -" + errorResponse!!.message)
                    //forgot_edt_email.setError(errorResponse.message)
                    Toast.makeText(this@Forgot, errorResponse.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMessage>?, t: Throwable?) {
                dialog.dismiss()
            }
        })

    }

    var resetpassword = {
        dialog.show()
        val baseClass = RetroBaseClass()
        val apiInterface = baseClass.getRetroFit().create(ApiInterface::class.java)
        val jsonObject = JsonObject()
        if (isEmail) {
            jsonObject.addProperty("email", email)
        } else {
            jsonObject.addProperty("mobile", email)
        }
        jsonObject.addProperty("password", password)
        jsonObject.addProperty("otp", otp)
        Log.e("signup", "param -  $jsonObject")
        val response: Call<ResponseMessage>? = apiInterface.ResetPassword(jsonObject)

        response?.enqueue(object : Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>?,
                response: Response<ResponseMessage>?
            ) {
                dialog.dismiss()
                if (response!!.isSuccessful) {
                    val message = response.body()!!.message
                    Toast.makeText(this@Forgot, message, Toast.LENGTH_SHORT).show()
                    Log.e("signup", "response -" + message)
                    openLoginScreen()
                } else {
                    //val data = response.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseMessage>() {}.type
                    val errorResponse: ResponseMessage? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("signup", "response -" + errorResponse!!.message)
                    Toast.makeText(this@Forgot, errorResponse.message, Toast.LENGTH_SHORT).show()
                    //edt_mob_et.setError(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<ResponseMessage>?, t: Throwable?) {
                dialog.dismiss()
            }
        })

    }

    private fun openLoginScreen() {

        val loginIntent = Intent(this@Forgot, Login::class.java)
        startActivity(loginIntent)
        finish()
    }

}