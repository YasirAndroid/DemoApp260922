package com.demo.demoapp.model


import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
        @SerializedName("name")
        val name: String,
        @SerializedName("token")
        val token: String
    )
}