package com.example.fourthapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleService(view: View) {
        when(view.id){
            R.id.btnStart -> {startMyService()}
            R.id.btnStop  -> {stopMyService()}
        }
    }

    private fun stopMyService() {
        val  serviceIntent = Intent(this,MyService::class.java)
        stopService(serviceIntent)
    }

    private fun startMyService() {
        val  serviceIntent = Intent(this,MyService::class.java)
        serviceIntent.putExtra("fileurl","https://fileurl.com")
        startService(serviceIntent)

    }

    fun handleBoundService(view: View) {
        when(view.id){
            R.id.btnBind -> {bindLocalService()}
            R.id.btnUnbind -> { unBindLocalService()}
        }
    }

    private fun unBindLocalService() {
        //unbind this activity from the service
        unbindService(serviceConnection)
    }

    private fun bindLocalService() {
        val bindIntent = Intent(this,LocalService::class.java)
        //BIND_AUTO_CREATE -- if the service is not running and some activity tries to bind to it please create an instance
        bindService(bindIntent,serviceConnection, BIND_AUTO_CREATE)
    }

    private val  serviceConnection = object : ServiceConnection{

        override fun onServiceConnected(p0: ComponentName?, lclBinder: IBinder?) {
            //im not instantiating or creating an object of LocalService
            //instead i'll pull the instance of the local service through lclBinder
            val binder = lclBinder as LocalService.LocalBinder
            //as =  typecasting
            mService = binder.getService()
           val c =  mService.randomNumber
            Log.i(TAG,"random no = "+c)
            Log.i(TAG,"sum is = "+mService.add(10,20))

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.i(TAG,"service disconnected")
        }
    }
    private lateinit var mService: LocalService

    companion object {
        var TAG = MainActivity::class.java.simpleName
    }

}