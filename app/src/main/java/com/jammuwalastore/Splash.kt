package com.jammuwalastore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jammuwalastore.helper.SessionManager
import com.students.students.NetworkCheck

/**
 * Created by Kishan Singh on 11-Apr-20.
 */


class Splash : AppCompatActivity() {
    lateinit var networkCheck: NetworkCheck
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        sessionManager = SessionManager(this)
        networkCheck = NetworkCheck()
            if (networkCheck.isNetworkAvailable(this)) {
                //getBaseUrls()
                val handler = Handler()
                handler.postDelayed(mSplashHandler, 3000)
            } else {
                Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
            }

    }

    internal var mSplashHandler: Runnable = Runnable {
        // your code to do after handler completes
        if (sessionManager.isLoggedIn) {
            val loginIntent = Intent(this@Splash, MainActivity::class.java)
            startActivity(loginIntent)
        } else {
            val loginIntent = Intent(this@Splash, Login::class.java)
            startActivity(loginIntent)
        }
        finish()
    }


}