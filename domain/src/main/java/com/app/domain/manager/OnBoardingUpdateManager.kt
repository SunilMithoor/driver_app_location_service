package com.app.domain.manager

import com.app.domain.repository.OnBoardingRepo

class OnBoardingUpdateManager(
    private val signInRepo: OnBoardingRepo,
) : OnBoardingRepo {
    override suspend fun signInUpdate(map: Map<String, String>) =
        signInRepo.signInUpdate(map)
}

