package com.task.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.task.models.ResponseModel

@Database(entities = [ResponseModel::class], version = 1, exportSchema = false)
@TypeConverters(ExclusionArrayConverter::class,FacilityArrayConverter::class)
abstract class RoomDB :RoomDatabase(){
    abstract fun getDataDao(): DataDao
}