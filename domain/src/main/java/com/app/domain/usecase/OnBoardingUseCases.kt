package com.app.domain.usecase

import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.wrapped.Result
import com.app.domain.entity.wrapped.toResult
import com.app.domain.extention.fromJson
import com.app.domain.extention.toJson
import com.app.domain.manager.OnBoardingUpdateManager


class SignInUseCase(private val signInUpdateManager: OnBoardingUpdateManager) :
    UseCase<SignInRequest, SignInUpdate> {

    override suspend fun onExecute(parameter: SignInRequest?): Result<SignInUpdate> {
        return signInUpdateManager.signInUpdate(parameter!!.toJson().fromJson()).toResult()
    }
}




