package com.demo.demoapp.repo

import androidx.lifecycle.MutableLiveData
import com.demo.demoapp.model.ResponseModel
import com.demo.demoapp.model.SignupResponse
import com.demo.demoapp.retrofit.APIClient
import com.demo.demoapp.retrofit.Api
import com.octel.crysta.utils.BaseResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object mainRepo {

    fun Login(email: String, password: String): MutableLiveData<BaseResponse<ResponseModel>> {

        val result = MutableLiveData<BaseResponse<ResponseModel>>()
        val server = APIClient.getInstance()!!.create(Api::class.java)
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .build()

        val call = server.login(body)
        call.enqueue(object : Callback<ResponseModel>{

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                result.value = BaseResponse.error("", null)
            }

            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.code()==200 && response.body()!=null){
                    result.value = BaseResponse.success(response.body())
                }
                else if(response.code()==404){
                    result.value = BaseResponse.failed("", null)
                }
                else {
                    result.value = BaseResponse.error("", null)
                }
            }
        })
     return result
    }

    fun Signup(firstname: String, lastname:String, email: String, password: String, conpass: String, phone: String, address: String, city: String, state: String, postal: String, uri: String): MutableLiveData<BaseResponse<SignupResponse>> {

        val result = MutableLiveData<BaseResponse<SignupResponse>>()
        val server = APIClient.getInstance()!!.create(Api::class.java)
        val file = File(uri)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), File(uri))
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("first_name", firstname)
            .addFormDataPart("last_name", lastname)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("password_confirmation", conpass)
            .addFormDataPart("address", address)
            .addFormDataPart("phone", phone)
            .addFormDataPart("select_city", city)
            .addFormDataPart("state", state)
            .addFormDataPart("postal_code", postal)
            .addFormDataPart("logo_image", file.name, requestBody)
            .build()

        val call = server.register(body)
        call.enqueue(object : Callback<SignupResponse>{

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                result.value = BaseResponse.error("", null)
            }

            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.code()==200 && response.body()!=null){
                    result.value = BaseResponse.success(response.body())
                }
                else if(response.code()==404){
                    result.value = BaseResponse.failed(response.message(), null)
                }
                else {
                    result.value = BaseResponse.error("", null)
                }
            }
        })
        return result
    }
}