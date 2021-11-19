package com.app.model

import com.app.domain.entity.db.LocationEntity
import com.app.domain.util.LocationOrder
import com.app.domain.util.OrderType


data class LocationState(
    val locations: List<LocationEntity> = emptyList(),
    val locationOrder: LocationOrder = LocationOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
