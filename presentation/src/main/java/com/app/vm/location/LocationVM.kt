package com.app.vm.location

import android.content.Context
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.LocationEntity
import com.app.domain.extention.parseDate
import com.app.domain.usecase.DeleteAllLocationUseCase
import com.app.domain.usecase.DeleteLocationByCountUseCase
import com.app.domain.usecase.GetAllLocationUseCase
import com.app.domain.usecase.InsertLocationUseCase
import com.app.domain.util.LocationOrder
import com.app.exception.InvalidLocationException
import com.app.services.locations.LocationLiveData
import com.app.model.LocationState
import com.app.vm.BaseVM
import com.app.vm.LocationEvent
import com.app.vm.UserEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class LocationVM(
    context: Context,
    private val insertLocationUseCase: InsertLocationUseCase,
    private val getAllLocationUseCase: GetAllLocationUseCase,
    private val deleteAllLocationUseCase: DeleteAllLocationUseCase,
    private val deleteLocationByCountUseCase: DeleteLocationByCountUseCase
) : BaseVM() {

    private val _locationState = mutableStateOf(LocationState())
    var locationState: State<LocationState> = _locationState
    private var getLocationJob: Job? = null


    val getLocationData: LiveData<Location> = LocationLiveData(context)


    init {
//        getLocation(LocationOrder.Date(OrderType.Ascending))
        getLocation()
    }

    override fun onAction(event: UserEvent) {
        when (event) {


        }
    }

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.SaveLocation -> {
                viewModelScope.launch {
                    try {
                        insertLocationUseCase.invoke(
                            LocationEntity(
                                null,
                                time = event.location.time.parseDate().toString(),
                                latitude = event.location.latitude,
                                longitude = event.location.longitude,
                                accuracy = event.location.accuracy,
                                altitude = event.location.altitude,
                                speed = event.location.speed,
                                bearing = event.location.bearing,
                                provider = event.location.provider
                            )
                        )
                    } catch (e: InvalidLocationException) {
                        e.printStackTrace()
                    }
                }
            }
            is LocationEvent.Order -> {
                getLocation(event.locationOrder)
            }
            is LocationEvent.GetAllLocation -> {
                getLocation()
            }
            is LocationEvent.DeleteAllLocation -> {
                viewModelScope.launch {
                    try {
                        deleteAllLocationUseCase.invoke()
                    } catch (e: InvalidLocationException) {
                        e.printStackTrace()
                    }
                }
            }
            is LocationEvent.DeleteLocationByCount -> {
                viewModelScope.launch {
                    try {
                        deleteLocationByCountUseCase.invoke(event.count)
                    } catch (e: InvalidLocationException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    private fun getLocation(locationOrder: LocationOrder) {
        getLocationJob?.cancel()
        getLocationJob = getAllLocationUseCase.invoke(locationOrder)
            .onEach { location ->
                _locationState.value = locationState.value.copy(
                    locations = location
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getLocation() {
        getLocationJob?.cancel()
        getLocationJob = getAllLocationUseCase.invoke()
            .onEach { location ->
                _locationState.value = locationState.value.copy(
                    locations = location
                )
            }
            .launchIn(viewModelScope)
    }

}