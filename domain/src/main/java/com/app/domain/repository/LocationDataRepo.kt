package com.app.domain.repository

import android.provider.ContactsContract
import com.app.domain.entity.LocationEntity
import kotlinx.coroutines.flow.Flow


interface LocationDataRepo {

    suspend fun insertLocationData(locationEntity: LocationEntity)

    suspend fun deleteLocationData(locationEntity: LocationEntity)

    fun getLocationData(): Flow<List<LocationEntity>>

    fun getAllLocationDataByAsc(): Flow<List<LocationEntity>>

}
