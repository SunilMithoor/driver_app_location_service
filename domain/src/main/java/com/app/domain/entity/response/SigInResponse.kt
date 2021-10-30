package com.app.domain.entity.response

import kotlinx.serialization.Serializable

class SigInResponse {
}

/*****************/

@Serializable
data class SignInUpdate(

    val message: String? = null,
    val success: String? = null,
)