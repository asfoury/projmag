package com.sdp13epfl2021.projmag.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.sdp13epfl2021.projmag.model.*

class UserProfileDatabase(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) {
    companion object {
        /**
         * Root collection for user-profile
         */
        const val ROOT = "user-profile"

        /**
         *  The field containing favorites
         */
        const val FAVORITES_FIELD = "profile"
    }

    /**
     * Return the current logged user or null if none
     *
     * @return current logged user or null
     */
    private fun getUser(): FirebaseUser? = auth.currentUser


    /**
     * Return the document associated to the user
     *
     * @return document associated to the user
     */
    private fun getUserDoc(): DocumentReference? =
        getUser()?.let { user ->
            firestore
                .collection(UserDataFirebase.ROOT)
                .document(user.uid)
        }

    public fun uploadProfile(profile : ImmutableProfile) {
        val profile = hashMapOf(
            "firstName" to profile.firstName,
            "lastName" to profile.lastName,
            "age" to profile.age,
            "gender" to profile.gender.toString(),
            "phoneNumber" to profile.phoneNumber,
            "role" to profile.role.toString(),
            "sciper" to profile.sciper
        )
        firestore.collection(ROOT).document(getUser()?.uid!!)
            .set(profile)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID:")
            }
            .addOnFailureListener {
            }
    }

    public fun getProfile(onSuccess: (profile : ImmutableProfile?) -> Unit) {
        val docRef =  firestore.collection(ROOT).document(getUser()?.uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val firstName = document.data?.get("firstName")
                    val lastName = document.data?.get("lastName")
                    val ageLong = document.data?.get("age")
                    val genderString = document.data?.get("gender")
                    val gender = if(genderString?.equals(Gender.MALE.toString())!!) Gender.MALE else if(genderString?.equals(Gender.FEMALE.toString())!!)  Gender.FEMALE else Gender.OTHER
                    val phoneNumber = document.data?.get("phoneNumber")
                    val roleString = document.data?.get("role")
                    val sciperLong = document.data?.get("sciper")
                    val role = if (roleString == "Teacher") Role.TEACHER else if (roleString == "Student") Role.STUDENT else Role.OTHER
                    val age = Integer.valueOf(ageLong.toString())
                    var sciper : Int = 0
                    try {
                        sciper = Integer.valueOf(sciperLong.toString())
                    } catch(exep : NumberFormatException) {
                        sciper = 123456
                    }


                    val resProfile = ImmutableProfile.build(lastName as String, firstName as String, age, gender, sciper, phoneNumber as String, role)
                    when(resProfile) {
                        is Success -> {
                            onSuccess(resProfile.value)
                        }
                        is Failure -> {
                            null
                        }
                    }

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }




}