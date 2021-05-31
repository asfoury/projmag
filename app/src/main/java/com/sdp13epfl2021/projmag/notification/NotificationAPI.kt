package com.sdp13epfl2021.projmag.notification



import com.sdp13epfl2021.projmag.notification.Cons.Companion.CONTENT_TYPE
import com.sdp13epfl2021.projmag.notification.Cons.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationAPI {
    @Headers("Authorization:key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    //    @Headers("Authorization:key=$Resources.getSystem().getString(R.string.projmag_api_key_1)","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}