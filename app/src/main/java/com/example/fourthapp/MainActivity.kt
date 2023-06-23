package com.example.fourthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}