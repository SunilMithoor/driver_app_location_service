package com.app.domain.repository

interface UserDataRepo {
    var isFirstTimeAppLaunch : Boolean
    var isPermissionGranted : Boolean
    var userId: Int?
    var uId: String?
    var mobile : String?
    var email : String?
    var deviceToken: String?
    var fireBaseToken: String?
    var isUserLoggedIn: Boolean
    fun logOut()
    fun clearSharedPrefData(key: String)
}