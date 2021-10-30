package com.app.domain.repository

import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.wrapped.Response


interface SignInRepo {
    suspend fun signInUpdate(map: Map<String, String>): Response<SignInUpdate>
}