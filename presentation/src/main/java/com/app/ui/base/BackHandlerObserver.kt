package com.app.ui.base

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


class BackHandlerObserver(
    private val activity: AppCompatActivity,
    private val onBackHandler: OnBackHandler
) :
    LifecycleObserver {

    private lateinit var handleBackPress: OnBackPressedCallback

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun enableBackHandler() {
        handleBackPress = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackHandler.handleOnBackPressed(this@BackHandlerObserver)
            }
        }
        activity.onBackPressedDispatcher.addCallback(handleBackPress)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disableBackHandler() {
        handleBackPress.remove()
    }

    fun forceBack() {
        handleBackPress.isEnabled = false
        activity.onBackPressedDispatcher.onBackPressed()
    }
}

interface OnBackHandler {
    fun handleOnBackPressed(observer: BackHandlerObserver)
}