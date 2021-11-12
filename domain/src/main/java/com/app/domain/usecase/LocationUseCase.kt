package com.app.domain.usecase

import com.app.domain.entity.LocationEntity
import com.app.domain.repository.LocationDataRepo
import com.app.domain.util.LocationOrder
import com.app.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class InsertLocationUseCase(
    private val locationDataRepo: LocationDataRepo
) {

    suspend operator fun invoke(locationEntity: LocationEntity) {
        locationDataRepo.insertLocationData(locationEntity)
    }
}


class GetAllLocationUseCase(
    private val locationDataRepo: LocationDataRepo
) {
    operator fun invoke(
        locationOrder: LocationOrder = LocationOrder.Date(OrderType.Descending)
    ): Flow<List<LocationEntity>> {
        return locationDataRepo.getAllLocationData().map { locations ->
            when (locationOrder.orderType) {
                is OrderType.Ascending -> {
                    when (locationOrder) {
                        is LocationOrder.Date -> locations.sortedBy { it.time }
                    }
                }
                is OrderType.Descending -> {
                    when (locationOrder) {
                        is LocationOrder.Date -> locations.sortedByDescending { it.time }
                    }
                }
            }
        }
    }
}

class GetLocationByCountUseCase(
    private val locationDataRepo: LocationDataRepo
) {
    operator fun invoke(
        locationOrder: LocationOrder = LocationOrder.Date(OrderType.Descending)
    ): Flow<List<LocationEntity>> {
        return locationDataRepo.getLocationDataByCount(50).map { locations ->
            when (locationOrder.orderType) {
                is OrderType.Ascending -> {
                    when (locationOrder) {
                        is LocationOrder.Date -> locations.sortedBy { it.time }
                    }
                }
                is OrderType.Descending -> {
                    when (locationOrder) {
                        is LocationOrder.Date -> locations.sortedByDescending { it.time }
                    }
                }
            }
        }
    }
}

class DeleteAllLocationUseCase(
    private val locationDataRepo: LocationDataRepo
) {
    suspend operator fun invoke() {
        locationDataRepo.deleteAllLocationData()
    }
}

class DeleteLocationByCountUseCase(
    private val locationDataRepo: LocationDataRepo
) {
    suspend operator fun invoke(data: Int) {
        locationDataRepo.deleteLocationDataByCount(data)
    }
}

