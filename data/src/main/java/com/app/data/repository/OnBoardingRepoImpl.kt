package com.app.data.repository

import com.app.data.datasource.remote.retrofit.OnBoardingApi
import com.app.domain.entity.wrapped.toResponseBody
import com.app.domain.repository.OnBoardingRepo

class OnBoardingRepoImpl(
    private val onBoardingApi: OnBoardingApi
) : OnBoardingRepo {

    override suspend fun signInUpdate(map: Map<String, String>) =
        onBoardingApi.signInUpdate(map).toResponseBody()

}


