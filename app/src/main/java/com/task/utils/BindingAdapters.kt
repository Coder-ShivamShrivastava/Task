package com.task.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.R


/** Binding Adapters */
object BindingAdapters {


    @BindingAdapter(value = ["setRecyclerAdapter"], requireAll = false)
    @JvmStatic
    fun setRecyclerAdapter(
        recyclerView: RecyclerView?,
        adapter: RecyclerView.Adapter<*>?
    ) {
        recyclerView?.adapter = adapter
    }

    @BindingAdapter(value = ["setImageUrl"], requireAll = false)
    @JvmStatic
    fun setImageUrl(
        imageView: ImageView,
        url: String?
    ) {
        try {
//            Picasso.get().load(url).placeholder(R.drawable.ic_profile).into(imageView)
        }catch (e:Exception){}

    }

    @BindingAdapter(value = ["setDrawerImage"], requireAll = false)
    @JvmStatic
    fun setDrawerImage(
        imageView: ImageView,
        type: String?
    ) {
        try {
            when(type){
                "apartment" -> {
                    imageView.setImageResource(
                        R.drawable.apartment)
                }

                "condo" -> {
                    imageView.setImageResource(
                        R.drawable.condo)
                }
                "boat" -> {
                    imageView.setImageResource(
                        R.drawable.boat)
                }
                "land" -> {
                    imageView.setImageResource(
                        R.drawable.land)
                }
                "rooms" -> {
                    imageView.setImageResource(
                        R.drawable.rooms)
                }
                "no-room" -> {
                    imageView.setImageResource(
                        R.drawable.no_room)
                }
                "swimming" -> {
                    imageView.setImageResource(
                        R.drawable.swimming)
                }
                "garden" -> {
                    imageView.setImageResource(
                        R.drawable.garden)
                }
                "garage" -> {
                    imageView.setImageResource(
                        R.drawable.garage)
                }
                else ->{
                    imageView.setImageResource(
                        R.drawable.apartment)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }



}