package com.app.domain.entity.request

data class SignInValidateRequest(
    var mobile: String,
    var password: String,
    var deviceId: String
)
