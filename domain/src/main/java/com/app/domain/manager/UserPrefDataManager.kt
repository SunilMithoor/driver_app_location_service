package com.app.domain.manager

import com.app.domain.repository.UserDataRepo


class UserPrefDataManager(private val userDataRepo: UserDataRepo) : UserDataRepo {

    override var isFirstTimeAppLaunch: Boolean
        get() = userDataRepo.isFirstTimeAppLaunch
        set(value) {
            userDataRepo.isFirstTimeAppLaunch = value
        }

    override var isPermissionGranted: Boolean
        get() = userDataRepo.isPermissionGranted
        set(value) {
            userDataRepo.isPermissionGranted = value
        }


    override var isUserLoggedIn: Boolean
        get() = userDataRepo.isUserLoggedIn
        set(value) {
            userDataRepo.isUserLoggedIn = value
        }

    override var userId: Int?
        get() = userDataRepo.userId
        set(value) {
            userDataRepo.userId = value
        }

    override var uId: String?
        get() = userDataRepo.uId
        set(value) {
            userDataRepo.uId = value
        }

    override var mobile: String?
        get() = userDataRepo.mobile
        set(value) {
            userDataRepo.mobile = value
        }

    override var email: String?
        get() = userDataRepo.email
        set(value) {
            userDataRepo.email = value
        }

    override var deviceToken: String?
        get() = userDataRepo.deviceToken
        set(value) {
            userDataRepo.deviceToken = value
        }

    override var fireBaseToken: String?
        get() = userDataRepo.fireBaseToken
        set(value) {
            userDataRepo.fireBaseToken = value
        }

    override fun logOut() {
        userDataRepo.isUserLoggedIn = false
        userDataRepo.userId = null
        userDataRepo.uId = null
        userDataRepo.mobile = null
        userDataRepo.email = null
        userDataRepo.deviceToken = null
        userDataRepo.fireBaseToken = null
    }

    override fun clearSharedPrefData(key: String) {
        
    }

}