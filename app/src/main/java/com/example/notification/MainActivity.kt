package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MarkMainActivity"
        private const val CHANNEL_ID="Mark Service"
        private const val CHANNEL_ID_NAME="Hello Test"

        private const val TITLE="Mark"
        private const val TEXT="Test"
    }

    lateinit var mContext: Context

    var isBound = false
    var markService: MarkService? = null
        private set
    val mMarkServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            markService = (service as MarkService.MyBinder).getService()
            isBound = true
        }


        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            isBound = false
            markService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        connect_service_button.setOnClickListener {
            val serviceIntent = Intent(this, MarkService::class.java)
            bindService(serviceIntent, mMarkServiceConnection, Context.BIND_AUTO_CREATE)
        }

        disconnect_service_button.setOnClickListener {
            if (isBound)
                unbindService(mMarkServiceConnection)
        }

        start_foreground_button.setOnClickListener {
            startForegroundForConnection()
        }

        stop_foreground_button.setOnClickListener {
            stopForegroundForConnection()
        }

        notify_notification.setOnClickListener {
            notifyNotification()
        }
    }

    private fun startForegroundForConnection() {
        markService?.startForeground(1, setNotification(TITLE,TEXT).build())
    }

    private fun setNotification(title: String, text: String): NotificationCompat.Builder {
        var mBuilder=NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setOngoing(true) //User not clear notification
            .setWhen(System.currentTimeMillis()) //set time click
            .setShowWhen(true) //Show time click
            .setAutoCancel(false)// Click notification not disappear

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            var channel=NotificationChannel(CHANNEL_ID,CHANNEL_ID_NAME,NotificationManager.IMPORTANCE_HIGH)
            var notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return mBuilder
    }

    private fun stopForegroundForConnection() {
        markService?.stopForeground(true)
    }

    private fun notifyNotification(){
        var notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,setNotification(TITLE+"1",TEXT+"1").build())
    }
}
