package com.app.vm

import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.request.SignUpRequest

interface UserEvent

sealed class OnSignInUpdateEvent : UserEvent {
    data class UpdateSignIn(val signInRequest: SignInRequest) : OnSignInUpdateEvent()
}
