package com.task.adapter

abstract class AbstractModel{
    var adapterPosition: Int = -1
    var onItemClick: RecyclerAdapter.OnItemClick? = null
}