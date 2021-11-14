package com.app.domain.repository

import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.wrapped.Response


interface OnBoardingRepo {
    suspend fun signInUpdate(map: Map<String, String>): Response<SignInUpdate>
}