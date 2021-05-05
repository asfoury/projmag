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

    /**
     * Uploads the user profile to firebase
     * @param profile the user profile to upload
     * @param onSuccess the closure that's when a profile is uploaded successfully
     * @param onFailure the closure that's called if the upload fails
     */
    public fun uploadProfile(
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
          onFailure(Exception("user id is null"))
        }
    }

    /**
     * Gets the user profile from firebase if it exists
     * @param onSuccess the closure that's when a profile is downloaded successfully with the fetched profile passed to it
     * @param onFailure the closure that's called if the download fails
     */
    public fun getProfile(
        onSuccess: (profile: ImmutableProfile?) -> Unit,
        onFailure: (Exception) -> kotlin.Unit
    ) {
        val userUid = getUser()?.uid
        if (userUid != null) {
            val docRef = firestore.collection(ROOT).document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = (document["firstName"] as? String)
                        val lastName = (document["firstName"] as? String)
                        val age = (document["age"] as? Long)?.toInt()
                        val sciper = (document["sciper"] as? Long)?.toInt()
                        val phoneNumber = (document["phoneNumber"] as? String)

                        val gender = when ((document["gender"] as? String)) {
                            Gender.MALE.name -> Gender.MALE
                            Gender.FEMALE.name -> Gender.FEMALE
                            else -> Gender.OTHER
                        }

                        val role = when (document["role"] as? String) {
                            Role.TEACHER.name -> Role.TEACHER
                            Role.STUDENT.name -> Role.STUDENT
                            else -> Role.OTHER
                        }

                        if (firstName != null && lastName != null && age != null && sciper != null && phoneNumber != null) {
                            when (val resProfile = ImmutableProfile.build(
                                lastName,
                                firstName,
                                age,
                                gender,
                                sciper,
                                phoneNumber,
                                role
                            )) {
                                is Success -> {
                                    onSuccess(resProfile.value)
                                }
                                is Failure -> {
                                    onFailure(Exception("Failure reason : ${resProfile.reason}"))
                                }
                            }
                        } else {
                            onFailure(Exception("At least one of the following fields is null: firstName = $firstName, lastName = $lastName, age = $age, sciper = $sciper, phoneNumber = $phoneNumber."))
                        }
                    }
                }
                .addOnFailureListener(onFailure)
        } else {
            onFailure(Exception("Document is null"))
        }
    }
}