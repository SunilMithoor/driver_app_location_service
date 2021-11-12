package com.app.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.extension.startService
import com.app.services.locations.LocationService


class LocationWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        try {
            applicationContext.startService(LocationService::class.java)
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }

}

