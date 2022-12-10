package com.dd

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

private var progressDialog: ProgressDialog? = null
private var progressCircularDialog: ProgressCircularDialog? = null
fun Context.getParentActivity(): AppCompatActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
fun Activity.showLoader(context: Context) {
    if (!isFinishing && !isDestroyed) {
        if (progressCircularDialog == null) {
            progressCircularDialog = ProgressCircularDialog(context)
        }
        progressCircularDialog?.let {
            it.setCancelable(false)
            it.show()
        }
    }
}

fun Activity.hideLoader() {
    if (!isFinishing && !isDestroyed) {
        if (progressCircularDialog != null)
            progressCircularDialog!!.dismiss()
    }
}

fun Fragment.toggleLoader(context: Context, flag: Boolean) {
    try {
        if (flag) {
            context.getParentActivity()?.showLoader(context)
        } else {
            context.getParentActivity()?.hideLoader()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}






