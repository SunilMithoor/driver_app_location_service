package com.app.domain.manager

import com.app.domain.entity.request.SignInRequest
import com.app.domain.repository.SignInRepo

class SignInUpdateManager(
    private val signInRepo: SignInRepo,
) : SignInRepo {
    override suspend fun signInUpdate(map: Map<String, String>) =
        signInRepo.signInUpdate(map)
}