package com.app.domain.repository

import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.FireBaseAuthUser
import com.app.domain.entity.response.FireBaseDeviceId
import com.app.domain.entity.response.FireBaseMessage
import com.app.domain.entity.response.FireBaseMessageToken


interface FirebaseDataRepo {

    suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun signOut(): FirebaseAuthResponse<FireBaseMessage>?

    suspend fun getUser(): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>?

    suspend fun getMessageToken(): FirebaseCallResponse<FireBaseMessageToken>?
}
