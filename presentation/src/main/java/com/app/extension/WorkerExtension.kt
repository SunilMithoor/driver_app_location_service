package com.app.extension

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.app.workmanager.LocationWorkManager
import java.util.concurrent.TimeUnit

fun Context.startWorker() {
    try {
        val workRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(
                LocationWorkManager::class.java,
                15,
                TimeUnit.MINUTES
            ).addTag("locationWorkManager").build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "LocationWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, workRequest
        )
    } catch (e: Exception) {
    }
}

fun Context.stopWorker() {
    WorkManager.getInstance(this).cancelAllWorkByTag("locationWorkManager")
}
