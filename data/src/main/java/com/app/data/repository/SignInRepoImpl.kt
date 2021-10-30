package com.app.data.repository

import com.app.data.datasource.remote.SighInApi
import com.app.domain.entity.wrapped.toResponseBody
import com.app.domain.repository.SignInRepo

class SignInRepoImpl(private val signInApi: SighInApi) : SignInRepo {

    override suspend fun signInUpdate(map: Map<String, String>) =
        signInApi.signInUpdate(map).toResponseBody()

}

