package com.app.domain.manager

import com.app.domain.repository.SignInValidateRepo

class SignInValidateManager(
    private val signInValidateRepo: SignInValidateRepo,
) : SignInValidateRepo {
    override suspend fun signInValidate(map: Map<String, String>) =
        signInValidateRepo.signInValidate(map)

}