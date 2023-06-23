package com.example.fourthapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*


class LocalService : Service() {

    //binder -- glue between activities and services = pipe
    private val localBinder = LocalBinder()

    private val mGenerator = Random()
    val randomNumber: Int
                get() = mGenerator.nextInt(100)

    fun add( a:Int,  b:Int) = a+b


    override fun onBind(p0: Intent?): IBinder? {
        return localBinder
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocalService = this@LocalService
    }
}


