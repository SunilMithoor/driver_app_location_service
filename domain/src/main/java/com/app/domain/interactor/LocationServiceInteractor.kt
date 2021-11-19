package com.app.domain.interactor


import com.app.domain.entity.db.LocationEntity
import com.app.domain.usecase.*

class LocationServiceInteractor(
    private val insertLocationUseCase: InsertLocationUseCase,
    private val deleteLocationByCountUseCase: DeleteLocationByCountUseCase,
    private val deleteAllLocationUseCase: DeleteAllLocationUseCase,
    private val getAllLocationUseCase: GetAllLocationUseCase,
    private val getLocationByCountUseCase: GetLocationByCountUseCase
) {

    suspend fun insertLocationData(locationEntity: LocationEntity) {
        insertLocationUseCase.invoke(locationEntity)
    }

    fun getAllLocationData() {
        getAllLocationUseCase.invoke()
    }

    fun getLocationDataByCount(data: Int) {
        getLocationByCountUseCase.invoke()
    }

    suspend fun deleteAllLocationData() {
        deleteAllLocationUseCase.invoke()
    }

    suspend fun deleteLocationDataByCount(data: Int) {
        deleteLocationByCountUseCase.invoke(data)
    }

}
