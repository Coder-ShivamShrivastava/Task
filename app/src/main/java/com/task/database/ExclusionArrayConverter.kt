package com.task.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.models.ResponseModel


class ExclusionArrayConverter {

    @TypeConverter
    fun fromString(value: String?): List<List<ResponseModel.Exclusion?>?>? {
        val listType = object : TypeToken<List<List<ResponseModel.Exclusion?>?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<List<ResponseModel.Exclusion?>?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}