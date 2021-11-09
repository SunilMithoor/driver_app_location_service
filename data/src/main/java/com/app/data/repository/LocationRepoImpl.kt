package com.app.data.repository

import android.provider.ContactsContract
import com.app.data.datasource.db.dao.AppDao
import com.app.domain.entity.LocationEntity
import com.app.domain.repository.LocationDataRepo
import kotlinx.coroutines.flow.Flow


class LocationRepoImpl(
    private val appDao: AppDao
) : LocationDataRepo {


    override suspend fun insertLocationData(locationEntity: LocationEntity) {
        appDao.saveLocationData(locationEntity)
    }

    override suspend fun deleteLocationData(locationEntity: LocationEntity) {
        appDao.deleteLocationData(locationEntity)
    }

    override fun getLocationData(): Flow<List<LocationEntity>> {
        return appDao.getAllLocationData()
    }

    override fun getAllLocationDataByAsc(): Flow<List<LocationEntity>> {
        return appDao.getAllLocationDataByAsc()
    }

}