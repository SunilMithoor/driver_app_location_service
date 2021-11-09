package com.app.data.datasource.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.data.datasource.db.dao.AppDao
import com.app.domain.entity.LocationEntity


//@Database(entities = [LocationEntity::class],version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun appDao(): AppDao
//}

@Database(entities = [LocationEntity::class],version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val database_name = "app_db"

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, database_name).build()
                }
            }
            return instance!!
        }
    }
}