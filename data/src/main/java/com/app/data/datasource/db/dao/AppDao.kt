package com.app.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.domain.entity.db.LocationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationData(locationEntity: LocationEntity)

    @Query("SELECT * FROM locations")
    fun getAllLocationData(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations ORDER BY time ASC LIMIT :count")
    fun getLocationDataByCount(count: Int): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocationData()

    @Query("DELETE FROM locations WHERE id IN (SELECT id FROM locations ORDER BY time ASC LIMIT :count)")
    suspend fun deleteLocationDataByCount(count: Int)

}