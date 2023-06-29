package com.task.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.models.ResponseModel

class FacilityArrayConverter {

    @TypeConverter
    fun fromString(value: String?): List<ResponseModel.Facility?>? {
        val listType = object : TypeToken<List<ResponseModel.Facility?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<ResponseModel.Facility?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}