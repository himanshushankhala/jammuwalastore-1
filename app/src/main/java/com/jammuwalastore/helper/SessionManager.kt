package com.jammuwalastore.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import com.jammuwalastore.Login
import java.util.*

class SessionManager @SuppressLint("CommitPrefEdits") constructor(var _context: Context) {
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0
    fun createLogin(userName: String?, password: String?) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)
        editor.putString("LoginToken", userName)
        editor.putString("LoginTokenPassword", password)
        editor.commit()
    }

    //editor.putString(Constant.KEY_TOKEN, token);
    //return pref.getString(Constant.KEY_USER_PHONE, "");
    var LoginToken: String?
        get() = pref.getString("LoginToken", "");
        set(token) {
            editor.putString("LoginToken", token)
            editor.commit()
        }

    var LoginTokenPassword: String?
        get() = pref.getString("LoginTokenPassword", "");
        set(token) {
            editor.putString("LoginTokenPassword", token)
            editor.commit()
        }

    //update user detail
    fun updateUserDetail(
        fname: String?,
        lname: String?,
        address: String?,
        address_2: String?,
        city: String?,
        mobile_no: String?,
        postcode: String?,
        state: String?,
        country: String?,
        email: String?
    ) {
        editor.putString("first_name", fname);
        editor.putString("last_name", lname);
        editor.putString("address", address);
        editor.putString("address_2", address_2);
        editor.putString("city", city);
        editor.putString("mobile_no", mobile_no);
        editor.putString("postcode", postcode);
        editor.putString("state", state);
        editor.putString("country", country);
        editor.putString("email", email);
        editor.commit()
    }

    //update user detail
    fun updateUserDetail(
        fname: String?,
        lname: String?,
        address: String?,
        address_2: String?,
        state: String?,
        city: String?,
        postcode: String?
    ) {
        editor.putString("first_name", fname);
        editor.putString("last_name", lname);
        editor.putString("address", address);
        editor.putString("address_2", address_2);
        editor.putString("city", city);
        editor.putString("postcode", postcode);
        editor.putString("state", state);
        editor.commit()
    }
    var first_name: String?
        get() = pref.getString("first_name", "");
        set(first_name) {
            editor.putString("first_name", first_name)
            editor.commit()
        }
    var last_name: String?
        get() = pref.getString("last_name", "");
        set(last_name) {
            editor.putString("last_name", last_name)
            editor.commit()
        }
    var address: String?
        get() = pref.getString("address", "");
        set(address) {
            editor.putString("address", address)
            editor.commit()
        }

    var address_2: String?
        get() = pref.getString("address_2", "");
        set(address_2) {
            editor.putString("address_2", address_2)
            editor.commit()
        }
    var city: String?
        get() = pref.getString("city", "");
        set(city) {
            editor.putString("city", city)
            editor.commit()
        }

    var mobile_no: String?
        get() = pref.getString("mobile_no", "");
        set(mobile_no) {
            editor.putString("mobile_no", mobile_no)
            editor.commit()
        }

    var postcode: String?
        get() = pref.getString("postcode", "");
        set(postcode) {
            editor.putString("postcode", postcode)
            editor.commit()
        }

    var state: String?
        get() = pref.getString("state", "");
        set(state) {
            editor.putString("state", state)
            editor.commit()
        }

    var country: String?
        get() = pref.getString("country", "");
        set(country) {
            editor.putString("country", country)
            editor.commit()
        }

    var email: String?
        get() = pref.getString("email", "");
        set(email) {
            editor.putString("email", email)
            editor.commit()
        }


    fun updateUserStatus(status: String?, deviceid: String?) {
        /*editor.putString(Constant.KEY_ACTIVE_STATUS, status);
        editor.putString(Constant.KEY_USER_PHONE_ID, deviceid);*/
        editor.commit()
    }

    fun setUserDeviceId(deviceid: String?) {
        //editor.putString(Constant.KEY_USER_PHONE_ID, deviceid);
        editor.commit()
    }

    //return pref.getString(Constant.KEY_ACTIVE_STATUS, "null");
    val userStatus: String?
        get() =//return pref.getString(Constant.KEY_ACTIVE_STATUS, "null");
            pref.getString("", "null")

    //editor.putString(Constant.KEY_USER_PHONE_ID, avtar);
    //return pref.getString(Constant.KEY_USER_PHONE_ID, "null");
    var avatar: String?
        get() =//return pref.getString(Constant.KEY_USER_PHONE_ID, "null");
            pref.getString("", "null")
        set(avtar) {
            //editor.putString(Constant.KEY_USER_PHONE_ID, avtar);
            editor.commit()
        }

    //editor.putString(Constant.LOGIN_IMG, avtar);
    //return pref.getString(Constant.LOGIN_IMG, "null");
    var loginImg: String?
        get() =//return pref.getString(Constant.LOGIN_IMG, "null");
            pref.getString("", "null")
        set(avtar) {
            //editor.putString(Constant.LOGIN_IMG, avtar);
            editor.commit()
        }// user password
    /*user.put(Constant.KEY_USERID, pref.getString(Constant.KEY_USERID, null));
    user.put(Constant.KEY_USER_EMAIL, pref.getString(Constant.KEY_USER_EMAIL, null));
    user.put(Constant.KEY_USER_PHONE, pref.getString(Constant.KEY_USER_PHONE, null));
    user.put(Constant.KEY_USER_FNAME, pref.getString(Constant.KEY_USER_FNAME, null));
    user.put(Constant.KEY_USER_LNAME, pref.getString(Constant.KEY_USER_LNAME, null));
    user.put(Constant.KEY_ACTIVE_STATUS, pref.getString(Constant.KEY_ACTIVE_STATUS, null));*/

    //get user info
    val userDetails: HashMap<String, String>
        get() =// user password
            /*user.put(Constant.KEY_USERID, pref.getString(Constant.KEY_USERID, null));
            user.put(Constant.KEY_USER_EMAIL, pref.getString(Constant.KEY_USER_EMAIL, null));
            user.put(Constant.KEY_USER_PHONE, pref.getString(Constant.KEY_USER_PHONE, null));
            user.put(Constant.KEY_USER_FNAME, pref.getString(Constant.KEY_USER_FNAME, null));
            user.put(Constant.KEY_USER_LNAME, pref.getString(Constant.KEY_USER_LNAME, null));
            user.put(Constant.KEY_ACTIVE_STATUS, pref.getString(Constant.KEY_ACTIVE_STATUS, null));*/HashMap()

    //return pref.getString(Constant.KEY_USERID, null);
    val userID: String?
        get() =//return pref.getString(Constant.KEY_USERID, null);
            pref.getString("", null)

    //return pref.getString(Constant.KEY_USER_NAME, null);
    val userName: String?
        get() =//return pref.getString(Constant.KEY_USER_NAME, null);
            pref.getString("", null)

    //return pref.getString(Constant.KEY_USER_EMAIL, null);
    val userEmail: String?
        get() =//return pref.getString(Constant.KEY_USER_EMAIL, null);
            pref.getString("", null)

    //return pref.getString(Constant.KEY_PERMA_LINK, null);
    val permalLink: String?
        get() =//return pref.getString(Constant.KEY_PERMA_LINK, null);
            pref.getString("", null)

    fun updatePermalLink(link: String?) {
        //editor.putString(Constant.KEY_PERMA_LINK, link);
        editor.commit()
    }

    //return pref.getString(Constant.BADGE_LINK, null);
    val badge: String?
        get() =//return pref.getString(Constant.BADGE_LINK, null);
            pref.getString("", null)

    fun logoutUser(context: Context?) {
        editor.putBoolean(IS_LOGIN, false)
        editor.commit()
        Toast.makeText(_context, "Logged Out", Toast.LENGTH_LONG).show()
        val intent = Intent(_context, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        _context.startActivity(intent)
        (_context as Activity).finish()
    }

    fun clearSession() {
        //editor.clear();
        //editor.commit();

        //LoginManager.getInstance().logOut();
        editor.putBoolean(IS_LOGIN, false)
        editor.commit()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(
            IS_LOGIN,
            false
        )

    companion object {
        lateinit var pref: SharedPreferences
        private const val PREF_NAME = "jammuwalastore"
        private const val IS_LOGIN = "IsLoggedIn"
        private const val IS_FBLOGIN = "IsFBIn"
    }

    // Constructor
    init {
        pref = _context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )
        editor = pref.edit()
    }
}