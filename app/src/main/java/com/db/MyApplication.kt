package com.db

import android.app.Application

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()



//        Constants.DeviceId = Settings.Secure.getString(
//                this.contentResolver,
//                Settings.Secure.ANDROID_ID
//        )
//
//        Constants.DeviceId = android.os.Build.MODEL



    }
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

}
