package com.jammuwalastore.helper

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.regex.Pattern

/**
 * Created by ABC on 19-Mar-18.
 */
object ProgressDialogUtil {

    fun setProgressDialog(context: Context, message: String): AlertDialog {
        val padding = 50
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.setPadding(padding, padding, padding, padding)
        linearLayout.gravity = Gravity.START
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        linearLayout.layoutParams = params

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, padding, 0)
        progressBar.layoutParams = params

        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = params

        linearLayout.addView(progressBar)
        linearLayout.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(linearLayout)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }


}


object EmailValid {

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun String.isPhoneValid(): Boolean {
        if(Pattern.matches("[0-9]+", this)) {
            return this.length in 10..13;
        }
        return false;
    }


}

