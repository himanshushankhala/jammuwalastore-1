package com.jammuwalastore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.jammuwalastore.helper.Constants
import com.jammuwalastore.helper.EmailValid.isEmailValid
import com.jammuwalastore.helper.ProgressDialogUtil
import com.jammuwalastore.helper.SessionManager
import com.students.students.NetworkCheck
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject


/**
 * Created by ABC on 11-Apr-20.
 */


class Login : FragmentActivity(), View.OnClickListener {
    lateinit var networkCheck: NetworkCheck
    private lateinit var mobEmail: String
    private lateinit var password: String
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        sessionManager = SessionManager(this)

        networkCheck = NetworkCheck()



        txt_skip.setOnClickListener(this)
        txt_signnup.setOnClickListener(this)
        txt_forgot.setOnClickListener(this)
        btn_signin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (networkCheck.isNetworkAvailable(this)) {


            when (v!!.id) {
                R.id.txt_skip -> {
                    openMainScreen()
                }
                R.id.txt_signnup -> {
                    val loginIntent = Intent(this@Login, SignUp::class.java)
                    startActivity(loginIntent)
                    finish()
                }

                R.id.txt_forgot -> {
                    val loginIntent = Intent(this@Login, Forgot::class.java)
                    startActivity(loginIntent)
                    finish()
                }

                R.id.btn_signin -> {
                    mobEmail = edt_mobile_email.text.toString().trim()
                    password = edt_login_password.text.toString().trim()
                    if (mobEmail.equals("")) {
                        Toast.makeText(
                            this,
                            getString(R.string.enter_mob_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!mobEmail.isEmailValid()) {
                        Toast.makeText(
                            this,
                            getString(R.string.enter_valid_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (password.equals("")) {
                        Toast.makeText(
                            this,
                            getString(R.string.enter_mob_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        SignIn()
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    var SignIn = {
        val dialog = ProgressDialogUtil.setProgressDialog(this, "Please wait ...")
        dialog.show()
        val url = Constants.SignIn_url
        Log.e("url>>", "$url<<")
        val jsonObject = JSONObject()
        jsonObject.put("email", mobEmail)
        jsonObject.put("password", password)
        Log.e("signin ", "param -  $jsonObject")
        AndroidNetworking.post(url)
            .setPriority(Priority.HIGH)
            .addJSONObjectBody(jsonObject)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    dialog.dismiss()
                    Log.e("SignIn>>", "$response<<")
                    val data = response.getJSONObject("data")
                    val username = data.getString("username")
                    val password = data.getString("password")
                    sessionManager.createLogin(username, password)
                    openMainScreen()
                }

                override fun onError(anError: ANError) {
                    dialog.dismiss()
                    Log.e("error", "${anError.errorBody}<<")
                    try {
                        val data = JSONObject(anError.errorBody)
                        val message = data.getString("message")
                        Toast.makeText(this@Login, message.toString(), Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {
                    }
                }
            })
    }

    private fun openMainScreen() {
        val loginIntent = Intent(this@Login, MainActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}