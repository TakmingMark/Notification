package com.example.notification

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MarkService:Service(){
    companion object {
        private const val TAG="MarkService"
    }

    private var mBinder=MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
       return mBinder
    }

    inner class MyBinder:Binder(){
        fun getService():MarkService=this@MarkService
    }

    override fun onCreate() {
        Log.d(TAG,"onCreate")
        super.onCreate()
    }
}