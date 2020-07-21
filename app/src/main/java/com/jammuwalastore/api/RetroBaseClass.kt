package com.students.students

import com.jammuwalastore.helper.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ABC on 19-Mar-18.
 */
class RetroBaseClass {

    lateinit var retroFit: Retrofit
    lateinit var retroFitHome: Retrofit

    var getRetroFit: () -> Retrofit = {
        retroFit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        retroFit
    }

    var getRetroFitHome: () -> Retrofit = {
        retroFitHome = Retrofit.Builder()
            .baseUrl(Constants.BASE_HOME)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        retroFitHome
    }
}