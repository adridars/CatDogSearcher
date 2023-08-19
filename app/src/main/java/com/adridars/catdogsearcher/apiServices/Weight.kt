package com.adridars.catdogsearcher.apiServices


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

internal data class Weight(
    @SerializedName("imperial")
    @Expose
    val imperial: String,
    @SerializedName("metric")
    @Expose
    val metric: String
)