package com.adridars.catdogsearcher.apiServices

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetRetrofit {
    fun getCatRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDogRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.thedogapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
