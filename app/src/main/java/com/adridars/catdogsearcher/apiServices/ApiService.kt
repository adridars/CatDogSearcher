package com.adridars.catdogsearcher.apiServices

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET ("/v1/breeds")
    suspend fun getBreeds():Response<ArrayList<BreedsCatDogDataResponse>>
    @GET("/v1/images/{id}")
    suspend fun getCatDog(@Path("id") CatID:String):Response<CatDogDetailResponse>
}