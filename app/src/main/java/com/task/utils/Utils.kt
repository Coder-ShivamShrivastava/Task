package com.task.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast


fun Context.showToast(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}


fun <T> List<T>.toArrayList():ArrayList<T>{
    val arrayList = ArrayList<T>()
    this.forEach{
        arrayList.add(it)
    }
    return arrayList
}

fun Context.alertBox(title:String="Alert",message:String,onClick:(Boolean)->Unit){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(true)

    builder.setPositiveButton("Yes",
        DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
            onClick(true)
            dialog?.dismiss()
        } as DialogInterface.OnClickListener)

    builder.setNegativeButton("No",
        DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
            onClick(false)
            dialog.dismiss()
        } as DialogInterface.OnClickListener)


    val alertDialog = builder.create()
    alertDialog.show()

}