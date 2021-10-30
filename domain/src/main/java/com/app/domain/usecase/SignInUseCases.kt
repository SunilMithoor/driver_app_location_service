package com.app.domain.usecase

import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.request.SignUpRequest
import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.wrapped.Result
import com.app.domain.entity.wrapped.toResult
import com.app.domain.extention.fromJson
import com.app.domain.extention.toJson
import com.app.domain.manager.SignInUpdateManager


class SignInUseCase(private val signInUpdateManager: SignInUpdateManager) :
    UseCase<SignInRequest, SignInUpdate> {

    override suspend fun onExecute(parameter: SignInRequest?): Result<SignInUpdate> {
        return signInUpdateManager.signInUpdate(parameter!!.toJson().fromJson()).toResult()
    }
}

class SignUpUseCase(private val signInUpdateManager: SignInUpdateManager) :
    UseCase<SignUpRequest, SignInUpdate> {

    override suspend fun onExecute(parameter: SignUpRequest?): Result<SignInUpdate> {
        return signInUpdateManager.signInUpdate(parameter!!.toJson().fromJson()).toResult()
    }
}



