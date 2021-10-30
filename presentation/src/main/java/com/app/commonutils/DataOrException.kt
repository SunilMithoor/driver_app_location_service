package com.app.commonutils


class DataOrException<T, E : Exception?> {
    var data: T? = null
    var exception: E? = null
}