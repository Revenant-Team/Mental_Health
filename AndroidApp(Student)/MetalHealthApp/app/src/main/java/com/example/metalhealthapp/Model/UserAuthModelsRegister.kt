package com.example.metalhealthapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpReq(
    val email : String ,
    val username : String ,
    val password : String ,
    val instituteCode : String? = null,
    val profile : UserProfile? = null
    )

@Serializable
data class UserProfile(
    val firstName : String? =null,
    val lastName : String? =null,
    val rollNumber : String? = null,
    val course : String? = null,
    val year : String? =null,
    val department : String? = null

)



@Serializable
data class RegisterResponse(
    val success : Boolean,
    val message : String,
    val data : DataResp
)

@Serializable
data class DataResp(
    val token : String,
    val user : UserRegisterResp
)
@Serializable
data class UserRegisterResp(
    val id : String?=null,
    val email : String? =null,
    val username : String? = null,
    val institute : String? = null

)