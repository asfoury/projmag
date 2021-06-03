package com.sdp13epfl2021.projmag.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sdp13epfl2021.projmag.BuildConfig
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"
private const val tokenString:String = "token"
private const val title:String = "title"
private const val msgString:String = "message"
private const val descriptionString:String= "My channel description"
private const val channelNameString:String = "channelName"
class ProjectNotificatonService: FirebaseMessagingService() {
     companion object{
        var sharedPref: SharedPreferences? = null
        var token :String?
        get(){
            return sharedPref?.getString(tokenString,"")
        }
        set(value){
            sharedPref?.edit()?.putString(tokenString,value)?.apply()
        }
    }


   override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(msg.data[title])
            .setContentText(msg.data[msgString])
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

       notificationManager.notify(notificationID,notification)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun createNotificationChannel(notificationManger: NotificationManager){
        val channelName = channelNameString
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description = descriptionString
        }

        notificationManger.createNotificationChannel(channel)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }
}
