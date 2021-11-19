package com.app.data.datasource.remote.firebase.auth

import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.response.FireBaseAuthUser
import com.app.domain.entity.response.FireBaseMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthCall {

    private var auth: FirebaseAuth? = null

    init {
        //Initialising auth and updates loginStatusLiveData value to be true if the user is already logged in
        if (auth == null) {
            auth = FirebaseAuth.getInstance()
        }
    }

//    suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseUser? {
//        auth?.createUserWithEmailAndPassword(
//            firebaseRequest.email,
//            firebaseRequest.password
//        )?.await()
//        return auth?.currentUser
//    }


    suspend fun signUpWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? {
        return try {
            auth?.createUserWithEmailAndPassword(
                firebaseRequest.email,
                firebaseRequest.password
            )?.await()
            FirebaseAuthResponse.Success(
                FireBaseAuthUser(
                    auth?.currentUser?.uid,
                    auth?.currentUser?.providerId,
                    auth?.currentUser?.displayName,
                    auth?.currentUser?.email,
                    auth?.currentUser?.photoUrl,
                    auth?.currentUser?.phoneNumber,
                    auth?.currentUser?.isEmailVerified
                )
            )
        } catch (exception: Exception) {
            FirebaseAuthResponse.Failure(exception)
        }
    }


//    suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseUser? {
//        auth?.signInWithEmailAndPassword(
//            firebaseRequest.email,
//            firebaseRequest.password
//        )?.await()
//        return auth?.currentUser
//    }


    suspend fun signInWithEmailPassword(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? {
        return try {
            auth?.signInWithEmailAndPassword(
                firebaseRequest.email,
                firebaseRequest.password
            )?.await()
            FirebaseAuthResponse.Success(
                FireBaseAuthUser(
                    auth?.currentUser?.uid,
                    auth?.currentUser?.providerId,
                    auth?.currentUser?.displayName,
                    auth?.currentUser?.email,
                    auth?.currentUser?.photoUrl,
                    auth?.currentUser?.phoneNumber,
                    auth?.currentUser?.isEmailVerified
                )
            )
        } catch (exception: Exception) {
            FirebaseAuthResponse.Failure(exception)
        }
    }


//    fun signOut(): FirebaseUser? {
//        auth?.signOut()
//        return auth?.currentUser
//    }

    fun signOut(): FirebaseAuthResponse<FireBaseMessage>? {
        return try {
            auth?.signOut()
            FirebaseAuthResponse.Success(FireBaseMessage(true))
        } catch (exception: Exception) {
            FirebaseAuthResponse.Failure(exception)
        }
    }

//    fun getUser(): FirebaseUser? {
//        return auth?.currentUser
//    }

    suspend fun getUser(): FirebaseAuthResponse<FireBaseAuthUser>? {
        return try {
            FirebaseAuthResponse.Success(
                FireBaseAuthUser(
                    auth?.currentUser?.uid,
                    auth?.currentUser?.providerId,
                    auth?.currentUser?.displayName,
                    auth?.currentUser?.email,
                    auth?.currentUser?.photoUrl,
                    auth?.currentUser?.phoneNumber,
                    auth?.currentUser?.isEmailVerified
                )
            )
        } catch (exception: Exception) {
            FirebaseAuthResponse.Failure(exception)
        }
    }

//    suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest) {
//        auth?.sendPasswordResetEmail(
//            firebaseRequest.email
//        )?.await()
//    }

    suspend fun sendPasswordReset(firebaseRequest: FirebaseRequest): FirebaseAuthResponse<FireBaseAuthUser>? {
        return try {
            auth?.sendPasswordResetEmail(
                firebaseRequest.email
            )?.await()
            FirebaseAuthResponse.Success(
                FireBaseAuthUser(
                    auth?.currentUser?.uid,
                    auth?.currentUser?.providerId,
                    auth?.currentUser?.displayName,
                    auth?.currentUser?.email,
                    auth?.currentUser?.photoUrl,
                    auth?.currentUser?.phoneNumber,
                    auth?.currentUser?.isEmailVerified
                )
            )
        } catch (exception: Exception) {
            FirebaseAuthResponse.Failure(exception)
        }
    }


}


