package com.app.helpers.firebase_api

interface DBValueEventListener<T> {
    fun onDataChange(data: T)
    fun onCancelled(error: Error?)
}