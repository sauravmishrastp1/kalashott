package com.bizbrolly.wayja.utils.handelOffline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.db.CheckNetworkCallBack

class NetworkChangeReceiver : BroadcastReceiver() {
    private var configOrientation = 0

    companion object {
        var checkNetworkCallback: CheckNetworkCallBack? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (android.provider.Settings.System.getInt(
                context!!.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION, 0
            ) == 1
        ) {
            checkNetworkCallback!!.netWorkState(true)


        } else {
            checkNetworkCallback!!.netWorkState(false)
        }

    }
}