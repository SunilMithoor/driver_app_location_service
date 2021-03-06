package com.app.data.repository

import com.app.data.datasource.remote.firebase.auth.FirebaseAuthenticator
import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.*
import com.app.domain.repository.FirebaseDataRepo


class FirebaseRepoImpl(
    private val firebaseAuthenticator: FirebaseAuthenticator
) : FirebaseDataRepo {

    override suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuthenticator.signUpWithEmailPassword(firebaseRequest)


    override suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuthenticator.signInWithEmailPassword(firebaseRequest)


    override suspend fun signOut(): FirebaseAuthResponse<FireBaseMessage>? =
        firebaseAuthenticator.signOut()

    override suspend fun getUser(): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuthenticator.getUser()

    override suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest):
            FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuthenticator.sendPasswordReset(firebaseRequest)

    override suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>? =
        firebaseAuthenticator.getDeviceId()

    override suspend fun getMessageToken(): FirebaseCallResponse<FireBaseMessageToken>? =
        firebaseAuthenticator.getMessageToken()

    override suspend fun setDatabaseValue(firebaseDatabaseRequest: FirebaseDatabaseRequest):
            FirebaseDatabaseCallResponse<FireBaseDatabase>? =
        firebaseAuthenticator.setDatabaseValue(firebaseDatabaseRequest)


}

