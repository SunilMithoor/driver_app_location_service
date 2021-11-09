package com.app.vm.location

import android.content.Context
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.LocationEntity
import com.app.domain.extention.parseDate
import com.app.domain.usecase.GetLocationUseCase
import com.app.domain.usecase.GetLocationUseCaseASC
import com.app.domain.usecase.InsertLocationUseCase
import com.app.domain.util.LocationOrder
import com.app.domain.util.OrderType
import com.app.exception.InvalidLocationException
import com.app.services.locations.LocationLiveData
import com.app.utils.LocationState
import com.app.vm.BaseVM
import com.app.vm.LocationEvent
import com.app.vm.UserEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class LocationVM(
    context: Context,
    private val insertLocationUseCase: InsertLocationUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getLocationUseCaseASC: GetLocationUseCaseASC,

    ) : BaseVM() {

    private val _locationEvent = MutableLiveData<Location>()
    private val _eventFlow = MutableSharedFlow<UiEvent>()

    private val _state = mutableStateOf(LocationState())
    val state: State<LocationState> = _state

    private var getLocationJob: Job? = null

    var result = MutableLiveData<List<LocationEntity>>()

    val getLocationData: LiveData<Location> = LocationLiveData(context)


    init {
        getLocation(LocationOrder.Date(OrderType.Descending))
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
                                time = _locationEvent.value?.time?.parseDate().toString(),
                                latitude = _locationEvent.value?.latitude,
                                longitude = _locationEvent.value?.longitude,
                                accuracy = _locationEvent.value?.accuracy,
                                altitude = _locationEvent.value?.altitude,
                                speed = _locationEvent.value?.speed,
                                bearing = _locationEvent.value?.bearing,
                                provider = _locationEvent.value?.provider
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveLocation)
                    } catch (e: InvalidLocationException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save location"
                            )
                        )
                    }
                }
            }
            is LocationEvent.GetAllLocation -> {
                viewModelScope.launch {
                    try {
                        getLocationUseCase.invoke()
                        _eventFlow.emit(UiEvent.GetLocation)
                    } catch (e: InvalidLocationException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't get location"
                            )
                        )
                    }
                }
            }
            is LocationEvent.GetAllLocationASC -> {
                viewModelScope.launch {
                    try {
                        getLocationUseCaseASC.invoke()
                        _eventFlow.emit(UiEvent.GetLocationASC)
                    } catch (e: InvalidLocationException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't get location"
                            )
                        )
                    }
                }
            }

            is LocationEvent.Order -> {
                getLocation(event.locationOrder)
            }
        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveLocation : UiEvent()
        object GetLocation : UiEvent()
        object GetLocationASC : UiEvent()
    }


    private fun getLocation(locationOrder: LocationOrder) {
        getLocationJob?.cancel()
        getLocationJob = getLocationUseCase.invoke(locationOrder)
            .onEach { location ->
                _state.value = state.value.copy(
                    locations = location,
                    locationOrder = locationOrder
                )
            }
            .launchIn(viewModelScope)
    }



}