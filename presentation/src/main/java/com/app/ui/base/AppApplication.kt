package com.app.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.app.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber


class AppApplication : MultiDexApplication(), LifecycleObserver {

    var wasInBackground = false

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            Timber.plant(LineNumberDebugTree())
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // app moved to foreground
        wasInBackground = true
        Timber.d("onMoveToForeground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        wasInBackground = false
        Timber.d("onMoveToBackground")
    }
}

class LineNumberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${element.fileName}:${element.lineNumber}:${element.methodName}"
    }
}