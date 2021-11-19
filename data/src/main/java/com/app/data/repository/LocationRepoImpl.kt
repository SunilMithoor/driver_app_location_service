package com.app.data.repository

import com.app.data.datasource.db.dao.AppDao
import com.app.domain.entity.db.LocationEntity
import com.app.domain.repository.LocationDataRepo
import kotlinx.coroutines.flow.Flow


class LocationRepoImpl(
    private val appDao: AppDao
) : LocationDataRepo {


    override suspend fun insertLocationData(locationEntity: LocationEntity) {
        appDao.insertLocationData(locationEntity)
    }

    override fun getAllLocationData(): Flow<List<LocationEntity>> {
        return appDao.getAllLocationData()
    }

    override fun getLocationDataByCount(data: Int): Flow<List<LocationEntity>> {
        return appDao.getLocationDataByCount(data)
    }


    override suspend fun deleteAllLocationData() {
        appDao.deleteAllLocationData()
    }

    override suspend fun deleteLocationDataByCount(data: Int) {
        appDao.deleteLocationDataByCount(data)
    }

}