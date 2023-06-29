package com.task.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.models.ResponseModel
import kotlinx.coroutines.flow.Flow


@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(data:ResponseModel)

    @Query("SELECT * FROM ResponseData")
    fun getAllNotes(): Flow<ResponseModel>



}