package com.jammuwalastore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_thanks.*

class ThanksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thanks)
        txtOrderId.text = "Order ID: #${intent.getStringExtra("orderID")}"
        btnDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        //Nothing to do
    }
}
