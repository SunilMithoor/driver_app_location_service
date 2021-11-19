package com.app.domain.usecase

import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.FireBaseAuthUser
import com.app.domain.entity.response.FireBaseMessage
import com.app.domain.manager.FirebaseUpdateManager


class SignInFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {

    suspend operator fun invoke(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseUpdateManager.signInWithEmailPassword(firebaseRequest)
}

class SignUpFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {

    suspend operator fun invoke(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseUpdateManager.signUpWithEmailPassword(firebaseRequest)
}

class GetUserFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {

    suspend operator fun invoke(): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseUpdateManager.getUser()
}

class SignOutFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {

    suspend operator fun invoke(): FirebaseAuthResponse<FireBaseMessage>? =
        firebaseUpdateManager.signOut()
}

class ResetPasswordFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {
    suspend operator fun invoke(firebaseRequest: FirebaseRequest) =
        firebaseUpdateManager.sendPasswordReset(firebaseRequest)
}

class MessageTokenFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {
    suspend operator fun invoke() = firebaseUpdateManager.getMessageToken()
}


class DeviceIdFirebaseUseCase(
    private val firebaseUpdateManager: FirebaseUpdateManager
) {
    suspend operator fun invoke() = firebaseUpdateManager.getDeviceId()
}
