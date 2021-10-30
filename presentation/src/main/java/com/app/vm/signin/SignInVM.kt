package com.app.vm.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.app.commonutils.DataOrException
import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.request.SignUpRequest
import com.app.domain.usecase.SignInUseCase
import com.app.domain.usecase.SignUpUseCase
import com.app.repository.FireBaseRepository
import com.app.vm.BaseVM
import com.app.vm.OnSignInUpdateEvent
import com.app.vm.UserEvent
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class SignInVM(
    private val signInUseCase: SignInUseCase
) : BaseVM() {

    private val _signIn = MutableLiveData<SignInRequest>()
    private var fireBaseRepository: FireBaseRepository? = FireBaseRepository()
    var deviceTokenLive: LiveData<DataOrException<String, Exception>>? = null
    var messageTokenLive: LiveData<DataOrException<String, Exception>>? = null
    var tokens: String? = null


    override fun onAction(event: UserEvent) {
        when (event) {
            is OnSignInUpdateEvent.UpdateSignIn -> _signIn.value = event.signInRequest

        }
    }

    val signInLiveData = Transformations.switchMap(_signIn) {
        signInUseCase.execute(viewModelIOScope, it).toLiveData()
    }

//    val signUpLiveData = Transformations.switchMap(_signUp) {
//        signInUseCase.execute(viewModelIOScope, it).toLiveData()
//    }


//    fun isValidEmail(emailId:MutableLiveData<String>): Int {
//        return try {
//            if (emailId.value.isNullOrEmpty()) {
//                1//email is empty
//            } else if (!validateEmail(emailId.value)) {
//                2//invalid email
//            } else {
//                0//success
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            -1
//        }
//    }


//    fun signUpUser(email: String, password: String) = viewModelScope.launch {
//        when {
//            email.isEmpty() -> {
//                eventsChannel.send(AllEvent.ErrorCode(1))
//            }
//            password.isEmpty() -> {
//                eventsChannel.send(AllEvent.ErrorCode(2))
//            }
//            else -> {
//                Timber.d("Success")
////                actualSignInUser(email , password)
//            }
//        }
//    }

//    sealed class AllEvent {
//
//        data class Message(val message: String) : AllEvent()
//
//        data class ErrorCode(val code: Int) : AllEvent()
//
//        data class Error(val error: String) : AllEvent()
//    }

}