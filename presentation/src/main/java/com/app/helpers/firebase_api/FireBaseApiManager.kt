package com.app.helpers.firebase_api

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FireBaseApiManager {


    fun forceSignOutUser() {
        apiWrapper?.signOutUser()
    }

    /**
     * To determine whether user has verifies their email address or not
     *
     * @return boolean value for user email verification
     */
    val isUserEmailVerified: Boolean
        get() = apiWrapper?.isUserVerified == true

    fun createNewUserWithEmailPassword(
        userEmail: String?,
        password: String?,
        onTaskCompleteListener: OnTaskCompleteListener
    ) {
        apiWrapper?.createNewUserWithEmailPassword(userEmail, password) { task ->
            if (task.isSuccessful) {
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                when (task.exception) {
                    is FirebaseNetworkException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("No Internet")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                    }
                    else -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred")
                    }
                }
            }
        }
    }

    fun signInWithEmailAndPassword(
        userEmail: String,
        password: String,
        onCompleteListener: OnTaskCompleteListener
    ) {
        apiWrapper?.signInWithEmailAndPassword(userEmail, password) { task ->
            if (task.isSuccessful) {
                onCompleteListener.onTaskSuccessful()
            } else {
                when (task.exception) {
                    is FirebaseNetworkException -> {
                        onCompleteListener.onTaskCompleteButFailed("No Internet")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        onCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        onCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                    }
                    else -> {
                        onCompleteListener.onTaskCompleteButFailed("Error Occurred")
                    }
                }
            }
        }
    }

    val userEmail: String?
        get() = apiWrapper?.currentUserEmail

    fun sendPasswordResetEmail(userEmail: String?, onTaskCompleteListener: OnTaskCompleteListener) {
        apiWrapper?.sendPasswordResetEmail(userEmail, { task ->
            if (task.isSuccessful) {
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                when (task.exception) {
                    is FirebaseNetworkException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("No Internet")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                    }
                    else -> {
                        onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred")
                    }
                }
            }
        }) { e -> onTaskCompleteListener.onTaskFailed(e) }
    }

    fun reloadUserAuthState(
        onSuccessListener: OnSuccessListener<Void?>?,
        onFailureListener: OnFailureListener
    ) {
        apiWrapper?.reloadCurrentUserAuthState(onSuccessListener, onFailureListener)
    }

    fun determineIfUpdateNeededAtSplash(
        versionName: String,
        eventListener: DBValueEventListener<String?>
    ) {
        val versionCheck =
            FirebaseDatabase.getInstance().reference.child(BaseUrl.VERSION_CHECK).child(versionName)
        apiWrapper?.singleValueEventListener(versionCheck, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventListener.onDataChange(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                eventListener.onCancelled(Error(databaseError.message))
            }
        })
    }

    val isUserLoggedIn: Boolean
        get() = userEmail != null


    object BaseUrl {
        // Declare the constants
        const val VERSION_CHECK = "version-check"
        const val USER_DATA = "UserData"
        const val TOKEN = "Token"
    }

    companion object {
        private var apiManager: FireBaseApiManager? = null
        private var apiWrapper: com.app.helpers.firebase_api.FireBaseApiWrapper? = null

        @JvmStatic
        val instance: FireBaseApiManager?
            get() {
                if (apiManager == null) {
                    apiManager = FireBaseApiManager()
                }
                if (apiWrapper == null) {
                    apiWrapper = com.app.helpers.firebase_api.FireBaseApiWrapper.instance
                }
                return apiManager
            }
    }
}