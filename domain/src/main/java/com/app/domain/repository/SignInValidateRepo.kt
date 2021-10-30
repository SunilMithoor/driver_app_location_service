package com.app.domain.repository

import com.app.domain.entity.response.SignInUpdate
import com.app.domain.entity.response.SignInValidate
import com.app.domain.entity.wrapped.Response


interface SignInValidateRepo {
    suspend fun signInValidate(map: Map<String, String>): Response<SignInValidate>
}