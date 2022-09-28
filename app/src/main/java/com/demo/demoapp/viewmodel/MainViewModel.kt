package com.demo.demoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.demoapp.model.ResponseModel
import com.demo.demoapp.model.SignupResponse
import com.demo.demoapp.repo.mainRepo
import com.octel.crysta.utils.BaseResponse
import java.io.File

class MainViewModel : ViewModel() {

    val resultLogin = MutableLiveData<BaseResponse<ResponseModel>>()
    val resultSignup = MutableLiveData<BaseResponse<SignupResponse>>()
    val repo = mainRepo

    fun Login(email: String, password: String) {
        repo.Login(email, password).observeForever {
            resultLogin.value = it
        }
    }
    fun Signup(firstname: String, lastname:String, email: String, password: String, conpass: String, phone: String, address: String, city: String, state: String, postal: String, file: String) {
        repo.Signup(firstname, lastname, email, password, conpass, phone, address, city, state, postal, file).observeForever {
            resultSignup.value = it
        }
    }
}