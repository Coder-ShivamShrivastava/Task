package com.task.network


sealed class NetworkProcess<out T>() {
    class Success<T>(var data:T) : NetworkProcess<T>()
    class Error<T>(var message: String, var data:T? = null) : NetworkProcess<T>()
    object Loading : NetworkProcess<Nothing>()
}
