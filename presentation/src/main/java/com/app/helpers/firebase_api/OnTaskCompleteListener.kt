package com.app.helpers.firebase_api

interface OnTaskCompleteListener {
    fun onTaskSuccessful()
    fun onTaskCompleteButFailed(errMsg: String?)
    fun onTaskFailed(e: Exception?)
}