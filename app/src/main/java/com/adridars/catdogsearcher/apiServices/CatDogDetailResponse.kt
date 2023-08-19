package com.adridars.catdogsearcher.apiServices

import com.google.gson.annotations.SerializedName

data class CatDogDetailResponse(
    @SerializedName("url") val imageUrl: String,
    @SerializedName("breeds") val breeds: List<CatDogBreedDetail>,
)

data class CatDogBreedDetail(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("temperament") val temperament: String,
)
