package com.example.fourthapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
  lateinit var   notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Trip"
                //getString(R.string.channel_name)
            val descriptionText = "trip related info"
                // getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

    }

    fun showNotification(view: View) {
        val intent = Intent(this, CalendarActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_electric_car_24)
            .setContentTitle("bosch title")
            .setContentText("text longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        createNotificationChannel()
        notificationManager.notify(1,builder.build())
        }
    }

