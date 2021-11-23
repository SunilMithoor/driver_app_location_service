package com.app.domain.manager

import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.FireBaseDatabase
import com.app.domain.entity.response.FireBaseDeviceId
import com.app.domain.entity.response.FireBaseMessageToken
import com.app.domain.repository.FirebaseDataRepo
import com.app.domain.repository.UserDataRepo


class FirebaseUpdateManager(
    private val firebaseDataRepo: FirebaseDataRepo,
    private val userDataRepo: UserDataRepo
) : FirebaseDataRepo {


    override suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest) =
        firebaseDataRepo.signUpWithEmailPassword(firebaseRequest)

    override suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest) =
        firebaseDataRepo.signInWithEmailPassword(firebaseRequest)


    override suspend fun signOut() = firebaseDataRepo.signOut()

    override suspend fun getUser() = firebaseDataRepo.getUser()

    override suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest) =
        firebaseDataRepo.sendPasswordReset(firebaseRequest)

    override suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>? =
        firebaseDataRepo.getDeviceId()


    override suspend fun getMessageToken(): FirebaseCallResponse<FireBaseMessageToken>? =
        firebaseDataRepo.getMessageToken()

    override suspend fun setDatabaseValue(firebaseDatabaseRequest: FirebaseDatabaseRequest):
            FirebaseDatabaseCallResponse<FireBaseDatabase>? =
        firebaseDataRepo.setDatabaseValue(firebaseDatabaseRequest)
}

