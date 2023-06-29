package com.task.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Singleton
    @Provides
    fun getDB(
        @ApplicationContext context: Context
    ): RoomDB {
        return  Room.databaseBuilder(
            context,
            RoomDB::class.java, "RoomDB"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun getDao(
        getDB: RoomDB
    ): DataDao {
        return  getDB.getDataDao()
    }

}