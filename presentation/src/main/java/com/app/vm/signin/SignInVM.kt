package com.app.vm.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.app.commonutils.DataOrException
import com.app.domain.entity.request.SignInFirebaseRequest
import com.app.domain.entity.request.SignInRequest
import com.app.domain.usecase.SignInUseCase
import com.app.repository.FireBaseRepository
import com.app.vm.BaseVM
import com.app.vm.OnSignInFirebaseEvent
import com.app.vm.OnSignInUpdateEvent
import com.app.vm.UserEvent


class SignInVM(
    private val signInUseCase: SignInUseCase
) : BaseVM() {

    private val _signIn = MutableLiveData<SignInRequest>()
    private var fireBaseRepository: FireBaseRepository? = FireBaseRepository()
    var deviceTokenLive: LiveData<DataOrException<String, Exception>>? = null
    var messageTokenLive: LiveData<DataOrException<String, Exception>>? = null
    var tokens: String? = null

    private val _signInFirebase = MutableLiveData<SignInFirebaseRequest>()


    override fun onAction(event: UserEvent) {
        when (event) {
            is OnSignInUpdateEvent.UpdateSignIn -> _signIn.value = event.signInRequest

            is OnSignInFirebaseEvent.SignInFirebase -> _signInFirebase.value =
                event.signInFirebaseRequest
        }
    }

    val signInLiveData = Transformations.switchMap(_signIn) {
        signInUseCase.execute(viewModelIOScope, it).toLiveData()
    }

//    val signInFirebaseLiveData = Transformations.switchMap(_signInFirebase) {
//        signInUseCase.execute(viewModelIOScope, it).toLiveData()
//    }


}