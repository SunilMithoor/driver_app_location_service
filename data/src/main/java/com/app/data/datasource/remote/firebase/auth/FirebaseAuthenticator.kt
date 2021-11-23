package com.app.data.datasource.remote.firebase.auth

import com.app.data.datasource.remote.firebase.database.FireBaseDatabaseCall
import com.app.data.datasource.remote.firebase.deviceid.FirebaseDeviceIdCall
import com.app.data.datasource.remote.firebase.message.FirebaseMessageCall
import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.*
import com.app.domain.repository.FirebaseDataRepo

class FirebaseAuthenticator(
    private val firebaseAuth: FirebaseAuthCall,
    private val firebaseMessageCall: FirebaseMessageCall,
    private val fireBaseDeviceIdCall: FirebaseDeviceIdCall,
    private val fireBaseDatabaseCall: FireBaseDatabaseCall
) : FirebaseDataRepo {

    override suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuth.signUpWithEmailPassword(firebaseRequest)


    override suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuth.signInWithEmailPassword(firebaseRequest)


    override suspend fun signOut(): FirebaseAuthResponse<FireBaseMessage>? =
        firebaseAuth.signOut()


    override suspend fun getUser(): FirebaseAuthResponse<FireBaseAuthUser>? = firebaseAuth.getUser()

    override suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest):
            FirebaseAuthResponse<FireBaseAuthUser>? =
        firebaseAuth.sendPasswordReset(firebaseRequest)

    override suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>? =
        fireBaseDeviceIdCall.getDeviceId()


    override suspend fun getMessageToken(): FirebaseCallResponse<FireBaseMessageToken>? =
        firebaseMessageCall.getMessageToken()

    override suspend fun setDatabaseValue(firebaseDatabaseRequest: FirebaseDatabaseRequest): FirebaseDatabaseCallResponse<FireBaseDatabase>? =
        fireBaseDatabaseCall.setDatabaseValue(firebaseDatabaseRequest)

}