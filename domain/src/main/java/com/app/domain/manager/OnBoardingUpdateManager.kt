package com.app.domain.manager

import com.app.domain.mapper.storeUserData
import com.app.domain.repository.OnBoardingRepo
import com.app.domain.repository.UserDataRepo

class OnBoardingUpdateManager(
    private val onBoardingRepo: OnBoardingRepo,
    private val userDataRepo: UserDataRepo
) : OnBoardingRepo {

    override suspend fun signInUpdate(map: Map<String, String>) =
        onBoardingRepo.signInUpdate(map).storeUserData(userDataRepo)

}

