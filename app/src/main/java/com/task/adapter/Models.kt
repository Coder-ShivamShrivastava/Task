package com.task.adapter


data class DummyModel(
    var text: String = "",
    var costText: String = "",
    var ids: Int = 0,
    var isPast: Boolean = false,
    var isSelected: Boolean = false,
    var quantity: Int = 1,
    var parentType: String = "",
    var childAdapter: RecyclerAdapter<DummyModel>? = null
) : AbstractModel()


