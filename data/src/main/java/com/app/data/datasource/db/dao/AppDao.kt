package com.app.data.datasource.db.dao

import androidx.room.*
import com.app.domain.entity.LocationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao {


    @Query("SELECT * FROM locations")
    fun getAllLocationData(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations ORDER BY time ASC")
    fun getAllLocationDataByAsc(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations")
    fun getAllLocationDatas(): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocationData(locationEntity: LocationEntity)

    @Delete
    suspend fun deleteLocationData(locationEntity: LocationEntity)

    @Query("DELETE FROM locations")
    suspend fun deleteAllData()

    @Query("DELETE FROM locations WHERE id IN (SELECT id FROM locations ORDER BY time ASC LIMIT :count)")
    suspend fun deleteLocationDataByCount(count: Int)


}