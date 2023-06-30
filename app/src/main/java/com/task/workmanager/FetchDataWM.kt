package com.task.workmanager

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.task.views.fragment.home.HomeVM
import java.util.concurrent.TimeUnit

class FetchDataWM(val context: Context,workerParams: WorkerParameters): Worker(context,workerParams) {

    companion object{
        private lateinit var homeVM: HomeVM
        fun fetchDataWorkManager(context: Context,viewModel:HomeVM){
            try {
                homeVM = viewModel
                val periodicWorkRequest = PeriodicWorkRequestBuilder<FetchDataWM>(24,TimeUnit.HOURS).build() // set api calling time, first time immediately call
                WorkManager.getInstance(context).enqueueUniquePeriodicWork("FetchDataWM",ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest)
            }catch (e:Exception){
                Log.d("exception","${e.printStackTrace()}")
                e.printStackTrace()
                Result.failure()
            }
        }
    }

    override fun doWork(): Result {
        return try {
            homeVM.callApi() // make Api request
            Result.success()
        }catch (e:Exception){
            Log.d("exception","${e.printStackTrace()}")
            e.printStackTrace()
            Result.failure()
        }
    }


}