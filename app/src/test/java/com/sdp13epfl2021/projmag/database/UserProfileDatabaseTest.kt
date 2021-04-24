package com.sdp13epfl2021.projmag.database



import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sdp13epfl2021.projmag.JavaToKotlinHelper
import com.sdp13epfl2021.projmag.model.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class UserProfileDatabaseTest {
//    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
//    val mockColRef = Mockito.mock(CollectionReference::class.java)
//    val mockDocRef = Mockito.mock(DocumentReference::class.java)
//    val mockFireBaseAuth = Mockito.mock(FirebaseAuth::class.java)
//
//    val mockTaskCol: Task<QuerySnapshot> = Mockito.mock(Task::class.java) as Task<QuerySnapshot>
//
//
//    val ID = "some-id"
//
//    val mockProfileDb = Mockito.mock(UserProfileDatabase::class.java)
//    val exampleUserProfile = when (val prof = ImmutableProfile.build(
//        "Last Name",
//        "First Name",
//        22,
//        Gender.MALE,
//        289982,
//        "01001001",
//        Role.STUDENT
//    )) {
//        is Success -> prof.value
//        is Failure -> null
//    }

//    @Suppress("UNCHECKED_CAST")
//    @Before
//    fun setupMocks() {
//        // --- mockFirebaseFirestore ---
//        Mockito
//            .`when`(mockFirebaseFirestore.collection(FirebaseProjectsDatabase.ROOT))
//            .thenReturn(mockColRef)
//
//        // --- mockColRef ---
//        Mockito
//            .`when`(mockColRef.get())
//            .thenReturn(mockTaskCol)
//
//        Mockito
//            .`when`(mockColRef.document(ID))
//            .thenReturn(mockDocRef)
//       Mockito
//           .`when`(mockProfileDb.getProfile(JavaToKotlinHelper.anyObject())).then {
//               exampleUserProfile
//           }
//
//    }    @Suppress("UNCHECKED_CAST")
//    @Before
//    fun setupMocks() {
//        // --- mockFirebaseFirestore ---
//        Mockito
//            .`when`(mockFirebaseFirestore.collection(FirebaseProjectsDatabase.ROOT))
//            .thenReturn(mockColRef)
//
//        // --- mockColRef ---
//        Mockito
//            .`when`(mockColRef.get())
//            .thenReturn(mockTaskCol)
//
//        Mockito
//            .`when`(mockColRef.document(ID))
//            .thenReturn(mockDocRef)
//       Mockito
//           .`when`(mockProfileDb.getProfile(JavaToKotlinHelper.anyObject())).then {
//               exampleUserProfile
//           }
//
//    }

//    @Test
//    fun checkThatGettingProfileWorks() {
//        val mockProfileDb = Mockito.mock(UserProfileDatabase::class.java)
//        Mockito.`when`(mockProfileDb.getProfile(JavaToKotlinHelper.anyObject())).then {
//
//        }
//    }




}