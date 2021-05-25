package com.sdp13epfl2021.projmag

import android.content.Intent
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.database.di.FirebaseDynamicLinksModule
import com.sdp13epfl2021.projmag.database.di.UserIdModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Named

@UninstallModules(
    UserIdModule::class,
    FirebaseDynamicLinksModule::class
)
@HiltAndroidTest
class MainActivityTest {
    @BindValue
    @Named("currentUserId")
    val userId: String = "uid"

    @BindValue
    val firebaseDynLinks: FirebaseDynamicLinks = Mockito.mock(FirebaseDynamicLinks::class.java)


    @Suppress("UNCHECKED_CAST")
    val mockTask: Task<PendingDynamicLinkData> =
        (Mockito.mock(Task::class.java) as Task<PendingDynamicLinkData>)

    private val mockPenDynLink: PendingDynamicLinkData =
        Mockito.mock(PendingDynamicLinkData::class.java)

    private val mockUri: Uri = Mockito.mock(Uri::class.java)


    @Test
    fun handleLinkWorks() {
        // TODO : `11` is and implementation detail, should be fixed
        Mockito.`when`(mockUri.path).thenReturn("0".repeat(11) + "userID")
        Mockito.`when`(mockPenDynLink.link).thenReturn(mockUri)
        Mockito.`when`(mockTask.addOnSuccessListener(anyObject())).then {
            @Suppress("UNCHECKED_CAST")
            val f = it.arguments[0] as Function1<PendingDynamicLinkData, Unit>
            f(mockPenDynLink)
            mockTask
        }
        Mockito.`when`(mockTask.addOnFailureListener(anyObject())).thenReturn(mockTask)
        Mockito.`when`(firebaseDynLinks.getDynamicLink(anyObject<Intent>())).thenReturn(mockTask)
    }
}