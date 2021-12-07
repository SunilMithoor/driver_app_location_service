package com.app.domain.entity

sealed class MQTTCallResponse<out T> {
    data class Success<out T>(val data: T?) : MQTTCallResponse<T>()
    data class Failure<out T>(val throwable: Throwable?) : MQTTCallResponse<T>()
}