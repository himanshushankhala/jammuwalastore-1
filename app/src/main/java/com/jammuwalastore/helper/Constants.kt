package com.jammuwalastore.helper

import android.content.Context
import android.os.Build
import com.jammuwalastore.R
import com.jammuwalastore.model.ProductsData
import okhttp3.Credentials
import okhttp3.Request
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets


/**
 * Created by ABC on 19-Mar-18.
 */
class Constants {

    companion object {

        var PREF_NAME = R.string.app_name;
        var BASE_URL = "http://www.jammuwalastore.com/wp-json/v1/"
        var BASE_HOME = "https://www.jammuwalastore.com/wp-json/wc/v3/"
        var BASE_HOME_ORDER = "https://www.jammuwalastore.com/wp-json/wc/v2/"

        val Product_Details_url = BASE_HOME + "products"
        val User_Details_url = BASE_URL + "user/getUser/"
        val SignIn_url = BASE_URL + "signin"
        val OrderURL = BASE_HOME_ORDER + "orders"
        val Categories = BASE_HOME + "products/categories?stock_status=instock"
        val CategoriesSearch = BASE_HOME + "products"
        val KeywordSearch = BASE_HOME + "products"

        var productsDataList: List<ProductsData>? = null;
        var NETWORK_MESSAGE = "No Internet Connection"
        var LOCATION_PERMISSION = 1
        var LOCATION_PERMISSION_BUS = 1

        var KEY_USER_NAME: String = ""
        var KEY_PASSWORD: String = ""

        var GOTOCART = false;
        private fun addBasicAuthHeaders(
            request: Request,
            username: String,
            password: String
        ): Request? {
            val credential: String = Credentials.basic(username, password)
            return request.newBuilder().header("Authorization", credential).build()
        }

        fun loadJSONFromAsset(name: String?, context: Context): String? {
            var json: String? = null
            json = try {
                val `is`: InputStream = context.assets.open(name.toString())
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
    }
}