package com.app.vm


sealed class FirebaseEvent {

    data class SignIn(
        val email: String, val password: String
    ) : FirebaseEvent()


}
