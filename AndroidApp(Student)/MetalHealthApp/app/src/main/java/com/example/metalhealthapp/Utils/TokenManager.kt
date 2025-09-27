package com.example.metalhealthapp.Utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("speedyserve_prefs", Context.MODE_PRIVATE)

    fun saveToken(token : String){
        sharedPreferences.edit { putString("jwt_token", token) }
    }

    fun getToken() : String? {
        return sharedPreferences.getString("jwt_token",null)
    }

    fun clearToken() {
        sharedPreferences.edit { remove("jwt_token") }
    }

}