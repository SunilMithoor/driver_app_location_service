package com.app.domain.entity.request

import org.json.JSONArray

data class SignInRequest(
    var mobile: String,
    var password: String,
    var deviceId: String
)

data class SignUpRequest(
    var email: String,
    var password: String,
    var confirmPassword: String
)

data class FirebaseRequest(
    var email: String,
    var password: String,
)

data class FirebaseDatabaseRequest(
    var data: JSONArray
)