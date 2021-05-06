package com.sdp13epfl2021.projmag.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.model.*

class UserProfileDatabase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
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
    fun uploadProfile(
        profile: ImmutableProfile,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val profile = hashMapOf(
            "firstName" to profile.firstName,
            "lastName" to profile.lastName,
            "age" to profile.age,
            "gender" to profile.gender.name,
            "phoneNumber" to profile.phoneNumber,
            "role" to profile.role.name,
            "sciper" to profile.sciper
        )
        val id = getUser()?.uid
        if (id != null) {
            firestore.collection(ROOT).document(id)
                .set(profile)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it)
                }
        } else {
            Log.d(TAG, "Unable to get the uid from firebase")
        }
    }

    fun getProfile(onSuccess: (profile: ImmutableProfile?) -> Unit) {
        val userUid = getUser()?.uid
        if (userUid != null) {
            val docRef = firestore.collection(ROOT).document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = document["firstName"] as? String
                        val lastName = document["lastName"] as? String
                        val ageLong = document["age"]

                        val gender = when (document["gender"] as? String) {
                            Gender.MALE.name -> Gender.MALE
                            Gender.FEMALE.name -> Gender.FEMALE
                            else -> Gender.OTHER
                        }
                        val phoneNumber = document["phoneNumber"] as? String
                        val roleString = document["role"] as? String
                        val sciperLong = document["sciper"]

                        val role = when (roleString) {
                            Role.TEACHER.name -> Role.TEACHER
                            Role.STUDENT.name -> Role.STUDENT
                            else -> Role.OTHER
                        }


                        var age: Int = try {
                            Integer.valueOf(ageLong.toString())
                        } catch (excep: NumberFormatException) {
                            0
                        }

                        var sciper: Int = try {
                            Integer.valueOf(sciperLong.toString())
                        } catch (exep: NumberFormatException) {
                            0
                        }


                        when (val resProfile = ImmutableProfile.build(
                            lastName ?: "null",
                            firstName ?: "null",
                            age,
                            gender,
                            sciper,
                            phoneNumber ?: "null",
                            role
                        )) {
                            is Success -> {
                                onSuccess(resProfile.value)
                            }
                            is Failure -> {
                                Log.d(TAG, "Failure reason : ${resProfile.reason}")
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
}