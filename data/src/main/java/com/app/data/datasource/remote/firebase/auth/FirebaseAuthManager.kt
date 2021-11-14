package com.app.data.datasource.remote.firebase.auth

import android.app.Activity
import android.content.Context
import com.app.domain.extention.post
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel


class FirebaseAuthManager(
    private val context: Context,
) {
    private var instance: FirebaseAuthManager? = null
    private var mAuth: FirebaseAuth? = null
    val success = Channel<Any>()
    val error = Channel<Any>()
    val failure = Channel<Any>()


    init {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance()
        }
//        if (instance == null) {
//            instance = FirebaseAuthManager(context)
//        }
    }

    fun checkFirebaseSignIn(
        email: String, password: String
    ) {
        if (mAuth != null) {
            //logging in the user
            mAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener((context as Activity)) { task ->
                    success.post(task)
                }?.addOnFailureListener()
                {
                    failure.post(it)
                }?.addOnCanceledListener()
                {
                    error.post("Something went wrong, Pleasse try again later")
                }
        }
    }
}