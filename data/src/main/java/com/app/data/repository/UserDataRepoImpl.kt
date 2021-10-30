package com.app.data.repository

import android.content.SharedPreferences
import com.app.domain.extention.get
import com.app.domain.extention.set
import com.app.domain.repository.UserDataRepo
import kotlinx.serialization.json.Json

class UserDataRepoImpl(private val sharedPreferences: SharedPreferences, private val json: Json) :
    UserDataRepo {

    override var isFirstTimeAppLaunch: Boolean
        get() = sharedPreferences["isFirstTimeAppLaunch", true]
        set(value) {
            sharedPreferences["isFirstTimeAppLaunch"] = value
        }

    override var isPermissionGranted: Boolean
        get() = sharedPreferences["isPermissionGranted", false]
        set(value) {
            sharedPreferences["isPermissionGranted"] = value
        }

    override var isUserLoggedIn: Boolean
        get() = sharedPreferences["isUserLoggedIn", false]
        set(value) {
            sharedPreferences["isUserLoggedIn"] = value
        }


    override var userId: Int?
        get() = sharedPreferences["userId", 0]
        set(value) {
            sharedPreferences["userId"] = value
        }

    override var uId: String?
        get() = sharedPreferences["uId", ""]
        set(value) {
            sharedPreferences["uId"] = value
        }

    override var mobile: String?
        get() = sharedPreferences["mobile", ""]
        set(value) {
            sharedPreferences["mobile"] = value
        }

    override var email: String?
        get() = sharedPreferences["email", ""]
        set(value) {
            sharedPreferences["email"] = value
        }

    override var deviceToken: String?
        get() = sharedPreferences["deviceToken", ""]
        set(value) {
            sharedPreferences["deviceToken"] = value
        }

    override var fireBaseToken: String?
        get() = sharedPreferences["fireBaseToken", ""]
        set(value) {
            sharedPreferences["fireBaseToken"] = value
        }

    override fun logOut() {
        sharedPreferences.edit().clear().apply()
    }

    override fun clearSharedPrefData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

}