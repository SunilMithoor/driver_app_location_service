package com.app.vm

import com.app.domain.entity.request.SignInRequest

interface UserEvent

sealed class OnSignInUpdateEvent : UserEvent {
    data class UpdateSignIn(val signInRequest: SignInRequest) : OnSignInUpdateEvent()
}

//sealed class OnSignInFirebaseEvent : UserEvent {
//    data class UpdateSignInFirebase(val signInFirebaseRequest: SignInFirebaseRequest) : OnSignInUpdateEvent()
//}
