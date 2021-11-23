package com.app.domain.entity


sealed class FirebaseAuthResponse<out T> {
    class Loading<out T> : FirebaseAuthResponse<T>()
    data class Success<out T>(val data: T) : FirebaseAuthResponse<T>()
    data class Failure<out T>(val throwable: Throwable) : FirebaseAuthResponse<T>()
}


sealed class FirebaseCallResponse<out T> {
    data class Success<out T>(val data: T) : FirebaseCallResponse<T>()
    data class Failure<out T>(val throwable: Throwable) : FirebaseCallResponse<T>()
}

sealed class FirebaseDatabaseCallResponse<out T> {
    data class Success<out T>(val data: T) : FirebaseDatabaseCallResponse<T>()
    data class Failure<out T>(val throwable: Throwable) : FirebaseDatabaseCallResponse<T>()
}

