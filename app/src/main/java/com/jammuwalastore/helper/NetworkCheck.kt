package com.students.students

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

/**
 * Created by ABC on 19-Mar-18.
 */
open class NetworkCheck {

    open fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.e("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.e("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.e("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }


        /*var networkInfo = manager.activeNetworkInfo

        if (networkInfo != null) {
            if (networkInfo.isConnected) {
                if (networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                } else if (networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                }
            } else {
                return false
            }
        }*/

        return false
    }
}