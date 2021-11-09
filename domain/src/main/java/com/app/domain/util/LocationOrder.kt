package com.app.domain.util


sealed class LocationOrder(val orderType: OrderType) {
    class Date(orderType: OrderType): LocationOrder(orderType)

    fun copy(orderType: OrderType): LocationOrder {
        return when(this) {
            is Date -> Date(orderType)
        }
    }
}