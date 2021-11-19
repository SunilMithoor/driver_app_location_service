package com.app.domain.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,
    @ColumnInfo(name = "time")
    val time: String?,
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
    @ColumnInfo(name = "longitude")
    val longitude: Double?,
    @ColumnInfo(name = "accuracy")
    val altitude: Double?,
    @ColumnInfo(name = "altitude")
    val accuracy: Float?,
    @ColumnInfo(name = "speed")
    val speed: Float?,
    @ColumnInfo(name = "bearing")
    val bearing: Float?,
    @ColumnInfo(name = "provider")
    val provider: String?
)
