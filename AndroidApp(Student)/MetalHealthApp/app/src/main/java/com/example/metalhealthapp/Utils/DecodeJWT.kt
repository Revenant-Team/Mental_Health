package com.example.metalhealthapp.Utils

import com.auth0.android.jwt.JWT

internal fun extractUserIdFromJWT(token : String)  : String?{
    val decodedJWT = JWT(token)
    return decodedJWT.getClaim("_id").asString()
}