package com.app.data.repository

import com.app.data.datasource.remote.retrofit.SighInApi
import com.app.domain.entity.wrapped.toResponseBody
import com.app.domain.repository.OnBoardingRepo

class SignInRepoImpl(private val signInApi: SighInApi) : OnBoardingRepo {

    override suspend fun signInUpdate(map: Map<String, String>) =
        signInApi.signInUpdate(map).toResponseBody()

}

