package com.task.network

import com.task.models.ResponseModel
import retrofit2.Response
import retrofit2.http.GET


interface RetrofitApi {
    @GET("iranjith4/ad-assignment/db")
    suspend fun detailsApi():Response<ResponseModel>
}