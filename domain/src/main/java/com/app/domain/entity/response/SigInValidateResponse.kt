package com.app.domain.entity.response

import kotlinx.serialization.Serializable

class SigInValidateResponse {
}

/*****************/

@Serializable
data class SignInValidate(

    val message: String? = null,
    val success: String? = null,
)