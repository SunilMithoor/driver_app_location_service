package com.app.domain.repository

import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.*


interface FirebaseDataRepo {

    suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun signOut(): FirebaseAuthResponse<FireBaseMessage>?

    suspend fun getUser(): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>?

    suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>?

    suspend fun getMessageToken(): FirebaseCallResponse<FireBaseMessageToken>?

    suspend fun setDatabaseValue(firebaseDatabaseRequest: FirebaseDatabaseRequest): FirebaseDatabaseCallResponse<FireBaseDatabase>?
}
