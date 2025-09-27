package com.example.metalhealthapp.Screens.AuthPages.SignIn

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.metalhealthapp.Model.UserSignInReq
import com.example.metalhealthapp.Repo.Repo
import com.example.metalhealthapp.Utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject  constructor(private val repo: Repo): ViewModel() {

    private val _user : MutableStateFlow<UserSignInReq> = MutableStateFlow(UserSignInReq("",""))
    val user : StateFlow<UserSignInReq> = _user

    val isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun signIn(context : Context,onSuccess : (String) -> Unit,onFailure : (String) -> Unit){
        viewModelScope.launch {
            isLoading.value = true
            val response =repo.signIn(user.value)
            response.onSuccess {
                result->
                Log.d("loging",result.data.toString())
                val tokenManager = TokenManager(context)
                tokenManager.saveToken(result.data.token)
                onSuccess(result.message)
                isLoading.value=false
            }
                .onFailure {
                    onFailure(it.message?:"Unknown Error")
                    isLoading.value=false
                }
        }
    }

    //updating email password
    fun updateEmail(email : String){
        _user.value = _user.value.copy(email = email)
    }
    fun updatePassword(password : String){
        _user.value = _user.value.copy(password = password)
    }

}