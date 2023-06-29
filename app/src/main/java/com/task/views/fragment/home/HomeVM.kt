package com.task.views.fragment.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.R
import com.task.adapter.RecyclerAdapter
import com.task.database.DataDao
import com.task.database.RoomDB
import com.task.models.ResponseModel
import com.task.network.NetworkProcess
import com.task.network.Repository
import com.task.network.RetrofitApi
import com.task.utils.showToast
import com.task.workmanager.FetchDataWM
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repository: Repository,
    private val retrofitApi: RetrofitApi,
    @ApplicationContext context: Context,
    private val dao:DataDao,
) : ViewModel() {

    val adapter by lazy { RecyclerAdapter<ResponseModel.Facility>(R.layout.item_layout_facility) }

    private val adapterClick = RecyclerAdapter.OnItemClick { view, position, type ->
        when (view.id) { }
    }

    init {
        adapter.setOnItemClick(adapterClick)
        FetchDataWM.fetchDataWorkManager(context,this)
        getDataFromDB()
    }

     fun callApi() {
        viewModelScope.launch {
            repository.makeApiCall {
                retrofitApi.detailsApi()
            }.collect{
                when (it) {
                    is NetworkProcess.Loading -> {}
                    is NetworkProcess.Success -> {
                        dao.addData(it.data)
                    }

                    is NetworkProcess.Error -> {
                    }
                }

            }
        }

    }

   private fun getDataFromDB(){
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllNotes().catch {
                Log.d("exception","${it.printStackTrace()}")
                it.printStackTrace()
            }.collect {
                Log.d("dataDB", "${it}")
                CoroutineScope(Dispatchers.Main).launch {
                if (it != null) {
                    it.facilities?.let { it1 ->
                        it1.forEach { facility ->
                            facility.childAdapter =
                                RecyclerAdapter<ResponseModel.Facility.Option>(R.layout.item_layout_options)
                            facility.options?.let { it2 -> facility.childAdapter!!.addItems(it2) }
                            setInnerAdapterClick(facility.childAdapter!!)
                        }
                        adapter.addItems(it1)
                    }
                }
            }
            }
        }
    }


    private fun setInnerAdapterClick(innerAdapter: RecyclerAdapter<ResponseModel.Facility.Option>) {
        val adapterClick = RecyclerAdapter.OnItemClick { view, position, type ->
            when (view.id) {
                R.id.cvOptions -> {
                    view.context.showToast("$position")
                }
            }
        }

        innerAdapter.setOnItemClick(adapterClick)
    }

}