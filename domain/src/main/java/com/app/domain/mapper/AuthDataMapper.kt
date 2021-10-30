package com.app.domain.mapper

import com.app.domain.entity.response.SignInUpdate

import com.app.domain.entity.wrapped.Response
import com.app.domain.entity.wrapped.success
import com.app.domain.repository.UserDataRepo


fun Response<SignInUpdate>.storeUserData(userDataRepo: UserDataRepo): Response<SignInUpdate> {
    return success {
        //userDataRepo.token = it.token
    }
}