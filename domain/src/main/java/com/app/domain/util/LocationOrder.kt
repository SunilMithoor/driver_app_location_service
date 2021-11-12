package com.app.domain.util


sealed class LocationOrder(val orderType: OrderType) {
    class Date(orderType: OrderType) : LocationOrder(orderType)

    fun copy(orderType: OrderType): LocationOrder {
        return when (this) {
            is Date -> Date(orderType)
        }
    }
}

sealed class LocationOrderByCount(val orderType: OrderType, data: Int) {
    class Date(orderType: OrderType, data: Int) : LocationOrderByCount(orderType, data)

    fun copy(orderType: OrderType, data: Int): LocationOrderByCount {
        return when (this) {
            is Date -> Date(orderType, data)
        }
    }
}