package com.app.data.datasource.remote.firebase.deviceid

import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.response.FireBaseDeviceId
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await

class FirebaseDeviceIdCall {

    private var firebaseInstallations: FirebaseInstallations? = null

    init {
        //Initialising auth and updates loginStatusLiveData value to be true if the user is already logged in
        if (firebaseInstallations == null) {
            firebaseInstallations = FirebaseInstallations.getInstance()
        }
    }


    suspend fun getDeviceId(): FirebaseCallResponse<FireBaseDeviceId>? {
        return try {
            val deviceId = firebaseInstallations?.getToken(false)?.await()
            FirebaseCallResponse.Success(FireBaseDeviceId(deviceId?.token))
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCallResponse.Failure(e)
        }
    }

}