package com.app.repository

import androidx.lifecycle.MutableLiveData
import com.app.commonutils.DataOrException
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import javax.inject.Singleton


@Singleton
class FireBaseRepository {

    fun getFireBaseDeviceToken(): MutableLiveData<DataOrException<String, Exception>>? {
        val dataOrExceptionMutableLiveData: MutableLiveData<DataOrException<String, Exception>> =
            MutableLiveData<DataOrException<String, Exception>>()
        try {
            val dataOrException: DataOrException<String, Exception> = DataOrException()
            FirebaseInstallations
                .getInstance()
                .getToken(false)
                .addOnSuccessListener {
                    Timber.d("Firebase Device token-->${it.token}")
                    dataOrException.data = it.token
                }
                .addOnFailureListener {
                    dataOrException.exception = it
                }
            dataOrExceptionMutableLiveData.value = dataOrException
            Timber.d("Return value-->${dataOrExceptionMutableLiveData}")
            return dataOrExceptionMutableLiveData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getFireBaseMessagingToken(): MutableLiveData<DataOrException<String, Exception>> {
        val dataOrExceptionMutableLiveData: MutableLiveData<DataOrException<String, Exception>> =
            MutableLiveData<DataOrException<String, Exception>>()
        val dataOrException: DataOrException<String, Exception> = DataOrException()
        try {
            FirebaseMessaging
                .getInstance()
                .token
                .addOnSuccessListener { s: String ->
                    Timber.d("Firebase Message token-->$s")
                    dataOrException.data = s
                }
                .addOnFailureListener {
                    dataOrException.exception = it
                }

        } catch (e: Exception) {
            e.printStackTrace()
            dataOrException.exception = e
        }
        dataOrExceptionMutableLiveData.value = dataOrException
        return dataOrExceptionMutableLiveData
    }

}