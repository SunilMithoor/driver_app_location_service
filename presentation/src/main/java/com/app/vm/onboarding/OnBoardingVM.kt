package com.app.vm.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.FirebaseAuthResponse
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.entity.request.FirebaseRequest
import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.response.*
import com.app.domain.usecase.*
import com.app.vm.BaseVM
import com.app.vm.OnSignInUpdateEvent
import com.app.vm.UserEvent
import kotlinx.coroutines.launch


class OnBoardingVM(
    private val signInUseCase: SignInUseCase,
    private val signInFirebaseUseCase: SignInFirebaseUseCase,
    private val signUpFirebaseUseCase: SignUpFirebaseUseCase,
    private val getUserFirebaseUseCase: GetUserFirebaseUseCase,
    private val signOutFirebaseUseCase: SignOutFirebaseUseCase,
    private val resetPasswordFirebaseUseCase: ResetPasswordFirebaseUseCase,
    private val messageTokenFirebaseUseCase: MessageTokenFirebaseUseCase,
    private val deviceIdFirebaseUseCase: DeviceIdFirebaseUseCase,
    private val databaseFirebaseUseCase: DatabaseFirebaseUseCase

) : BaseVM() {

    private val _signIn = MutableLiveData<SignInRequest>()

    private val _firebaseSignInResponse =
        MutableLiveData<FirebaseAuthResponse<FireBaseAuthUser>?>()
    val firebaseSignInResponse get() = _firebaseSignInResponse

    private val _firebaseSignUpResponse = MutableLiveData<FirebaseAuthResponse<FireBaseAuthUser>?>()
    val firebaseSignUpResponse get() = _firebaseSignUpResponse

    private val _firebaseGetUserResponse =
        MutableLiveData<FirebaseAuthResponse<FireBaseAuthUser>?>()
    val firebaseGetUserResponse get() = _firebaseGetUserResponse

    private val _firebaseSignOutResponse =
        MutableLiveData<FirebaseAuthResponse<FireBaseMessage>?>()
    val firebaseSignOutResponse get() = _firebaseSignOutResponse

    private val _firebaseResetPasswordResponse =
        MutableLiveData<FirebaseAuthResponse<FireBaseAuthUser>?>()
    val firebaseResetPasswordResponse get() = _firebaseResetPasswordResponse

    private val _firebaseMessageTokenResponse =
        MutableLiveData<FirebaseCallResponse<FireBaseMessageToken>?>()
    val firebaseMessageTokenResponse get() = _firebaseMessageTokenResponse

    private val _firebaseDeviceIdResponse =
        MutableLiveData<FirebaseCallResponse<FireBaseDeviceId>?>()
    val firebaseDeviceIdResponse get() = _firebaseDeviceIdResponse

    private val _firebaseDatabaseResponse =
        MutableLiveData<FirebaseDatabaseCallResponse<FireBaseDatabase>?>()
    val firebaseDatabaseResponse get() = _firebaseDatabaseResponse


    override fun onAction(event: UserEvent) {
        when (event) {
            is OnSignInUpdateEvent.UpdateSignIn -> _signIn.value = event.signInRequest

        }
    }


    val signInLiveData = Transformations.switchMap(_signIn) {
        signInUseCase.execute(viewModelIOScope, it).toLiveData()
    }


    //validate all fields first before performing any sign in operations

//    fun signInUser(email: String, password: String) = viewModelScope.launch {
//        try {
//            val user = signInFirebaseUseCase.invoke(FirebaseRequest(email, password))
//            user?.let {
//                _firebaseSignInResponse.postValue(it)
//            }
//        } catch (e: Exception) {
//            val error = e.toString().split(":").toTypedArray()
//            Timber.d("signInUser: ${error[1]}")
//            _firebaseSignInResponse.postValue(FirebaseState(null, e))
//        }
//    }

    fun signInUser(email: String, password: String) =
        viewModelScope.launch {
            _firebaseSignInResponse.postValue(
                signInFirebaseUseCase.invoke(
                    FirebaseRequest(
                        email,
                        password
                    )
                )
            )
        }

//    fun signUpUser(email: String, password: String) = viewModelScope.launch {
//        try {
//            val user = signUpFirebaseUseCase.invoke(FirebaseRequest(email, password))
//            user?.let {
//                _firebaseSignUpResponse.postValue(FirebaseState(it, null))
//            }
//        } catch (e: Exception) {
//            val error = e.toString().split(":").toTypedArray()
//            Timber.d("signInUser: ${error[1]}")
//            _firebaseSignUpResponse.postValue(FirebaseState(null, e))
//        }
//    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            _firebaseSignUpResponse.postValue(
                signUpFirebaseUseCase.invoke(
                    FirebaseRequest(
                        email,
                        password
                    )
                )
            )
        }
    }

//    fun getUser() = viewModelScope.launch {
//        try {
//            val user = getUserFirebaseUseCase.invoke()
//            user?.let {
//                _firebaseGetUserResponse.postValue(FirebaseState(it, null))
//            }
//        } catch (e: Exception) {
//            val error = e.toString().split(":").toTypedArray()
//            Timber.d("signInUser: ${error[1]}")
//            _firebaseGetUserResponse.postValue(FirebaseState(null, e))
//        }
//    }

    fun getUser() {
        viewModelScope.launch {
            _firebaseGetUserResponse.postValue(getUserFirebaseUseCase.invoke())
        }
    }

//    fun signOut() = viewModelScope.launch {
//        try {
//            val user = signOutFirebaseUseCase.invoke()
//            user?.let {
//                _firebaseSignOutResponse.postValue(FirebaseState(it, null))
//            }
//        } catch (e: Exception) {
//            val error = e.toString().split(":").toTypedArray()
//            Timber.d("signInUser: ${error[1]}")
//            _firebaseSignOutResponse.postValue(FirebaseState(null, e))
//        }
//    }

    fun signOut() {
        viewModelScope.launch {
            _firebaseSignOutResponse.postValue(signOutFirebaseUseCase.invoke())
        }
    }

//    fun resetPassword(email: String) = viewModelScope.launch {
//        try {
//            val user = resetPasswordFirebaseUseCase.invoke(FirebaseRequest(email, ""))
//            user?.let {
//                _firebaseResetPasswordResponse.postValue(FirebaseState(it, null))
//            }
//        } catch (e: Exception) {
//            val error = e.toString().split(":").toTypedArray()
//            _firebaseSignInResponse.postValue(FirebaseState(null, e))
//        }
//    }

    fun resetPassword(email: String) =
        viewModelScope.launch {
            _firebaseResetPasswordResponse.postValue(
                resetPasswordFirebaseUseCase.invoke(
                    FirebaseRequest(email, "")
                )
            )
        }


    fun getMessageToken() =
        viewModelScope.launch {
            _firebaseMessageTokenResponse.postValue(messageTokenFirebaseUseCase.invoke())
        }

    fun getDeviceId() =
        viewModelScope.launch {
            _firebaseDeviceIdResponse.postValue(deviceIdFirebaseUseCase.invoke())
        }

    fun setDatabaseData(firebaseDatabaseRequest: FirebaseDatabaseRequest) =
        viewModelScope.launch {
            _firebaseDatabaseResponse.postValue(databaseFirebaseUseCase.invoke(firebaseDatabaseRequest))
        }

}


