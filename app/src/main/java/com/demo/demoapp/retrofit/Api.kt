package com.demo.demoapp.retrofit

import com.demo.demoapp.model.ResponseModel
import com.demo.demoapp.model.SignupResponse
import com.octel.crysta.utils.BaseResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST

interface Api {

    @POST("quote-login")
    fun login(@Body body: RequestBody) : Call<ResponseModel>

    @POST("quote-register")
    fun register(@Body body: RequestBody) : Call<SignupResponse>

}