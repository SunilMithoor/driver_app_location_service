package com.app.domain.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult



interface FireBaseOnAuthResultTaskComplete {

    fun onComplete(task: Task<AuthResult>)

}