package com.app.vm.events

import com.google.firebase.auth.FirebaseUser


sealed class FirebaseEvent {
    data class SignIn(
        val email: String, val password: String
    ) : FirebaseEvent()
}

sealed class AllEvents {
    data class Data(val firebaseUser: FirebaseUser) : AllEvents()
    data class Message(val message: String) : AllEvents()
    data class ErrorCode(val code: Int) : AllEvents()
    data class Error(val error: String) : AllEvents()
}




