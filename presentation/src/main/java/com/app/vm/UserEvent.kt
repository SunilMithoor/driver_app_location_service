package com.app.vm

import com.app.domain.entity.request.SignInFirebaseRequest
import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.request.SignUpRequest

interface UserEvent

sealed class OnSignInUpdateEvent : UserEvent {
    data class UpdateSignIn(val signInRequest: SignInRequest) : OnSignInUpdateEvent()
}

sealed class OnSignInFirebaseEvent : UserEvent {
    data class SignInFirebase(val signInFirebaseRequest: SignInFirebaseRequest) : OnSignInUpdateEvent()
}
