package com.demo.demoapp.model


import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("token")
        val token: String,
        @SerializedName("userId")
        val userId: Int
    )
}