package com.sdp13epfl2021.projmag.notification

import com.sdp13epfl2021.projmag.notification.NotificationData

data class PushNotification (
    val data: NotificationData,
    val to:String
        )