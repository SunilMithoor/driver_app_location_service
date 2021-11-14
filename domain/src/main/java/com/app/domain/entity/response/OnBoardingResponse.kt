package com.app.domain.entity.response

import kotlinx.serialization.Serializable

/*****************/

@Serializable
data class SignInUpdate(

    val message: String? = null,
    val success: String? = null,
)

@Serializable
data class SignInFirebaseUpdate(

    val message: String? = null,
    val success: String? = null,
)