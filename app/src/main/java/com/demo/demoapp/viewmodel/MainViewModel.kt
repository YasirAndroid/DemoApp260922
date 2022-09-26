package com.demo.demoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.demoapp.model.ResponseModel
import com.demo.demoapp.repo.mainRepo
import com.octel.crysta.utils.BaseResponse

class MainViewModel : ViewModel() {

    val result = MutableLiveData<BaseResponse<ResponseModel>>()
    val repo = mainRepo

    fun Login(email: String, password: String) {
        repo.Login(email, password).observeForever {
            result.value = it
        }
    }
    fun Signup(firstname: String, lastname:String, email: String, password: String, conpass: String, phone: String, address: String, city: String, state: String, postal: String, uri: String) {
        repo.Signup(firstname, lastname, email, password, conpass, phone, address, city, state, postal, uri).observeForever {
            result.value = it
        }
    }
}