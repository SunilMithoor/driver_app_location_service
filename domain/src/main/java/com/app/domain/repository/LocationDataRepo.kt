package com.app.domain.repository

import com.app.domain.entity.db.LocationEntity
import kotlinx.coroutines.flow.Flow


interface LocationDataRepo {

    suspend fun insertLocationData(locationEntity: LocationEntity)

    fun getAllLocationData(): Flow<List<LocationEntity>>

    fun getLocationDataByCount(data: Int): Flow<List<LocationEntity>>

    suspend fun deleteAllLocationData()

    suspend fun deleteLocationDataByCount(data: Int)

}
