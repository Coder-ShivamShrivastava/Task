package com.task.views.fragment.home

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.task.R
import com.task.adapter.RecyclerAdapter
import com.task.database.DataDao
import com.task.database.RoomDB
import com.task.models.ResponseModel
import com.task.network.NetworkProcess
import com.task.network.Repository
import com.task.network.RetrofitApi
import com.task.utils.showToast
import com.task.utils.toArrayList
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
    private val dao: DataDao,
) : ViewModel() {

    val adapter by lazy { RecyclerAdapter<ResponseModel.Facility>(R.layout.item_layout_facility) }
    val isProgressShow by lazy { ObservableField(false) }

    private val adapterClick = RecyclerAdapter.OnItemClick { view, position, type ->
        when (view.id) {
        }
    }

    init {
        adapter.setOnItemClick(adapterClick)
        FetchDataWM.fetchDataWorkManager(context, this)
        getDataFromDB()
    }

    fun callApi() {
        viewModelScope.launch {
            repository.makeApiCall {
                retrofitApi.detailsApi()
            }.collect {
                when (it) {
                    is NetworkProcess.Loading -> {
                        isProgressShow.set(true)
                    }
                    is NetworkProcess.Success -> {
                        isProgressShow.set(false)
                        dao.addData(it.data)
                    }

                    is NetworkProcess.Error -> {
                        isProgressShow.set(false)
                    }
                }

            }
        }

    }

    private fun getDataFromDB() {
        isProgressShow.set(true)
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllNotes().catch {
                isProgressShow.set(false)
                Log.d("exception", "${it.printStackTrace()}")
                it.printStackTrace()
            }.collect {
                isProgressShow.set(false)
                Log.d("dataDB", "${it}")
                CoroutineScope(Dispatchers.Main).launch {
                    if (it != null) {
                        val hash = HashMap<String, MutableSet<String>>()
                        it.exclusions?.forEach { exclusionsList ->
                            var dataO: ResponseModel.Facility.Option? =
                                null // set default value null for getting last item
                            exclusionsList.forEach { exclusions ->
                                if (dataO == null) {
                                    val dataF = it.facilities?.find {
                                        it.facilityId == exclusions?.facilityId
                                    }
                                    if (dataF != null) {
                                        // dataO for current option
                                        dataO = dataF.options?.find {
                                            it.id == exclusions?.optionsId
                                        }
                                    }

                                } else {
                                    if (hash.containsKey(dataO?.id)) {
                                        val data: MutableSet<String>? = hash[exclusions?.optionsId]
                                        data?.add(exclusions?.optionsId ?: "")
                                    } else {
                                        hash[dataO?.id ?: ""] =
                                            mutableSetOf(exclusions?.optionsId ?: "")
                                    }

                                    if (hash.containsKey(exclusions?.optionsId)) {
                                        val data: MutableSet<String>? = hash[exclusions?.optionsId]
                                        data?.add(dataO?.id ?: "")
                                    } else {
                                        hash[exclusions?.optionsId ?: ""] =
                                            mutableSetOf(dataO?.id ?: "")
                                    }
                                    dataO!!.hash = hash

                                    val data = hash[dataO?.id]
                                    var str = ""
                                    data?.forEach {key ->
                                        it.facilities?.forEach {
                                           var opt =  it.options?.find {
                                                it.id == key || it.id == dataO?.id
                                            }
                                            if (opt!=null){
                                                str += opt.name+" and "
                                            }
                                        }
                                    }
                                    str += " can not select together"

                                    dataO?.upPairMessage = str
                                    val dataF = it.facilities?.find {
                                        it.facilityId == exclusions?.facilityId
                                    }
                                    if (dataF != null) {
                                        // dataN for next data option
                                        val dataN = dataF.options?.find {
                                            it.id == exclusions?.optionsId
                                        }
                                        if (dataN != null) {
                                            dataN.hash = hash

                                            val dataOpt = hash[dataN.id]
                                            var strOpt = ""
                                            dataOpt?.forEach {key ->
                                                it.facilities?.forEach {
                                                    var opt =  it.options?.find {
                                                        it.id == key || it.id == dataN.id
                                                    }
                                                    if (opt!=null){
                                                        strOpt += opt.name+", "
                                                    }
                                                }

                                            }
                                            strOpt += " can not select together"
                                            dataN.upPairMessage="$strOpt"
                                        }
                                    }
                                }

                            }
                        }

                        Log.d("dataFilter", Gson().toJson(it))

                        it.facilities?.let { it1 ->
                            it1.forEachIndexed { index, facility ->
                                facility.childAdapter =
                                    RecyclerAdapter<ResponseModel.Facility.Option>(R.layout.item_layout_options)
                                facility.options?.let { it2 -> facility.childAdapter!!.addItems(it2) }
                                setInnerAdapterClick(index, facility.childAdapter!!)
                            }
                            adapter.addItems(it1)
                        }
                    }
                }
            }

        }
    }


    private fun setInnerAdapterClick(
        parentPosition: Int,
        innerAdapter: RecyclerAdapter<ResponseModel.Facility.Option>
    ) {
        val adapterClick = RecyclerAdapter.OnItemClick { view, position, type ->
            when (view.id) {
                R.id.cvOptions -> {
                    if (adapter.items[parentPosition].options?.get(position)?.hash.isNullOrEmpty()) {
                        adapter.items[parentPosition].options?.forEach {
                            it.isSelected = false
                        }
                        adapter.items[parentPosition].options?.get(position)?.isSelected = true
                    } else {
                        val id = adapter.items[parentPosition].options?.get(position)?.id
                        val value =
                            adapter.items[parentPosition].options?.get(position)?.hash?.get(id)
                        var default = true
                        value?.forEach out@{ ids ->
                            adapter.items.forEach {
                                val data: ResponseModel.Facility.Option? = it.options?.find {
                                    it.id == ids
                                }
                                if (data != null) {
                                    if (data.isSelected) {
                                        default = false
                                        return@out
                                    } else {
                                        if (default)
                                            default = true
                                    }
                                }
                            }
                        }
                        if (default) {
                            adapter.items[parentPosition].options?.forEach {
                                it.isSelected = false
                            }
                            adapter.items[parentPosition].options?.get(position)?.isSelected = true
                        }else{
                            view.context.showToast(adapter.items[parentPosition].options?.get(position)?.upPairMessage?:"")
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
        innerAdapter.setOnItemClick(adapterClick)
    }
}