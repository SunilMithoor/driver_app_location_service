package com.app.domain.entity.response

import com.google.firebase.auth.FirebaseUser

/*****************/


data class SignInUpdate(

    val message: String? = null,
    val success: String? = null,
    val token: String? = null
)


data class FirebaseUpdate(
    val firebaseUser: FirebaseUser? = null
)