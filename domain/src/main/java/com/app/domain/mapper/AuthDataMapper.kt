package com.app.domain.mapper

import com.app.domain.entity.response.SignInUpdate

import com.app.domain.entity.wrapped.Response
import com.app.domain.entity.wrapped.success
import com.app.domain.repository.UserDataRepo
import com.google.firebase.auth.FirebaseUser


fun Response<SignInUpdate>.storeUserData(userDataRepo: UserDataRepo): Response<SignInUpdate> {
    return success {
        userDataRepo.deviceToken = it.token
    }
}

fun Response<FirebaseUser>.storeUserInfo(userDataRepo: UserDataRepo): Response<FirebaseUser> {
    return success {
        if (it!=null) {
            userDataRepo.uId = it.uid
            userDataRepo.isUserLoggedIn = true
        }
    }
}