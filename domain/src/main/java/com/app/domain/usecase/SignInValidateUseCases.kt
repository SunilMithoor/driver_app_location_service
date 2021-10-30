package com.app.domain.usecase

import com.app.domain.entity.request.SignInRequest
import com.app.domain.entity.request.SignInValidateRequest
import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.response.SignInValidate
import com.app.domain.entity.wrapped.Result
import com.app.domain.entity.wrapped.toResult
import com.app.domain.extention.fromJson
import com.app.domain.extention.toJson
import com.app.domain.manager.SignInUpdateManager
import com.app.domain.manager.SignInValidateManager


class SignInValidateUseCase(private val signInValidateManager: SignInValidateManager) :
    UseCase<SignInValidateRequest, SignInValidate> {

    override suspend fun onExecute(parameter: SignInValidateRequest?): Result<SignInValidate> {
        return signInValidateManager.signInValidate(parameter!!.toJson().fromJson()).toResult()
    }


}
