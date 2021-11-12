package com.app.vm

import android.location.Location
import com.app.domain.util.LocationOrder


sealed class LocationEvent {

    data class SaveLocation(val location: Location) : LocationEvent()

    data class Order(val locationOrder: LocationOrder) : LocationEvent()

    object GetAllLocation : LocationEvent()

    object DeleteAllLocation : LocationEvent()

    data class DeleteLocationByCount(val count: Int) : LocationEvent()

}
