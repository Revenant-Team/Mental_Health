package com.example.metalhealthapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserSignInReq(
    val email : String,
    val password : String
)

@Serializable
data class LoginResponse(
    val success : Boolean,
    val message : String,
    val data : DataResp
)
