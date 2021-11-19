package com.app.vm.permission

import com.app.vm.BaseVM
import com.app.vm.UserEvent
import com.markodevcic.peko.PermissionsLiveData

class PermissionVM : BaseVM() {

    val permissionLiveData = PermissionsLiveData()

    override fun onAction(event: UserEvent) {
        when (event) {

        }
    }

    fun checkPermissionsData(vararg permissions: String) {
        permissionLiveData.checkPermissions(*permissions)
    }

}