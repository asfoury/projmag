package com.sdp13epfl2021.projmag.database.impl.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.*
import javax.inject.Inject


/**
 * An implementation of a user-data database
 * using Google Firebase/FireStore
 */
class FirebaseUserdataDatabase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserdataDatabase {

    companion object {
        /**
         * Root collection for user-data
         */
        const val ROOT = "user-data"

        /**
         *  The field containing favorites
         */
        const val FAVORITES_FIELD = "favorites"

        /**
         * The field containing applied to
         */
        const val APPLIED_TO_FIELD = "applied to"

        /**
         *  The field containing cv
         */
        const val CV_FIELD = "cv"

        /**
         *  The field containing cv
         */
        const val PREF_FIELD = "preferences"

        const val USER_PROFILE = "user-profile"

        private val AUTH_EXCEPTION: Exception = SecurityException("User needs to be authenticated")
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
                .collection(ROOT)
                .document(user.uid)
        }

    override fun pushFavoriteProject(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        pushListOfFavoriteProjects(
            listOf(projectID),
            onSuccess,
            onFailure
        )
    }

    override fun pushListOfFavoriteProjects(
        projectIDs: List<ProjectId>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.let { doc ->
            getListOfFavoriteProjects(
                { ls ->
                    val newList: List<ProjectId> = (ls.toSet().union(projectIDs.toSet())).toList()
                    doc.set(
                        hashMapOf(
                            "favorites" to newList
                        ), SetOptions.merge()
                    ).addOnSuccessListener { onSuccess() }.addOnFailureListener(onFailure)
                },
                onFailure
            )
        } ?: onFailure(AUTH_EXCEPTION)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getListOfFavoriteProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.run {
            get()
                .addOnSuccessListener { doc ->
                    onSuccess((doc[FAVORITES_FIELD] as? List<ProjectId>) ?: listOf())
                }.addOnFailureListener(onFailure)
        } ?: onFailure(AUTH_EXCEPTION)
    }

    override fun removeFromFavorite(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.let { doc ->
            getListOfFavoriteProjects(
                {
                    val newList = it.filter { pid -> pid != projectID }
                    doc.update(FAVORITES_FIELD, newList)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener(onFailure)
                },
                onFailure
            )
        } ?: onFailure(AUTH_EXCEPTION)
    }

    override fun pushCv(
        cv: CurriculumVitae,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.set(
            hashMapOf(CV_FIELD to cv),
            SetOptions.merge()
        )?.addOnSuccessListener { onSuccess() }?.addOnFailureListener(onFailure)
            ?: onFailure(AUTH_EXCEPTION)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getCv(
        userID: String,
        onSuccess: (CurriculumVitae?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection(ROOT)
            .document(userID)
            .get()
            .addOnSuccessListener {
                try {
                    val cvMap = it[CV_FIELD] as Map<String, Any>
                    val summary = cvMap["summary"] as String
                    val education = cvMap["education"] as List<CurriculumVitae.PeriodDescription>
                    val jobExperience =
                        cvMap["jobExperience"] as List<CurriculumVitae.PeriodDescription>
                    val languages = cvMap["languages"] as List<CurriculumVitae.Language>
                    val skills = cvMap["skills"] as List<CurriculumVitae.SkillDescription>
                    onSuccess(CurriculumVitae(summary, education, jobExperience, languages, skills))
                } catch (e: Exception) {
                    onFailure(e)
                }
            }
            .addOnFailureListener(onFailure)
    }

    override fun applyUnapply(
        apply: Boolean,
        projectId: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.let { doc ->
            val fieldValue = if (apply) {
                FieldValue.arrayUnion(projectId)
            } else {
                FieldValue.arrayRemove(projectId)
            }
            doc.update(APPLIED_TO_FIELD, fieldValue)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener(onFailure)
        } ?: onFailure(AUTH_EXCEPTION)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()?.run {
            get()
                .addOnSuccessListener { doc ->
                    onSuccess((doc[APPLIED_TO_FIELD] as? List<ProjectId>) ?: listOf())
                }.addOnFailureListener(onFailure)
        } ?: onFailure(AUTH_EXCEPTION)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getPreferences(
        onSuccess: (ProjectFilter?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()
            ?.get()
            ?.addOnSuccessListener { doc ->
                onSuccess(doc?.get(PREF_FIELD)?.let { ProjectFilter(it as Map<String, Any>) })
            }?.addOnFailureListener(onFailure)
            ?: onFailure(AUTH_EXCEPTION)
    }

    override fun pushPreferences(
        pf: ProjectFilter,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getUserDoc()
            ?.set(hashMapOf(PREF_FIELD to pf))
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener(onFailure)
            ?: onFailure(AUTH_EXCEPTION)
    }

    override fun uploadProfile(
        profile: ImmutableProfile,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val id = getUser()?.uid
        if (id != null) {
            firestore
                .collection(USER_PROFILE)
                .document(id)
                .set(profile)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener(onFailure)
        } else {
            onFailure(AUTH_EXCEPTION)
        }
    }

    override fun getProfile(
        userID: String,
        onSuccess: (profile: ImmutableProfile?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection(USER_PROFILE)
            .document(userID)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val firstName = (document["firstName"] as? String)
                    val lastName = (document["lastName"] as? String)
                    val age = (document["age"] as? Long)?.toInt()
                    val sciper = (document["sciper"] as? Long)?.toInt()
                    val phoneNumber = (document["phoneNumber"] as? String)

                    val gender = when (document["gender"] as? String) {
                        Gender.MALE.name -> Gender.MALE
                        Gender.FEMALE.name -> Gender.FEMALE
                        else -> Gender.OTHER
                    }

                    val role = when (document["role"] as? String) {
                        Role.TEACHER.name -> Role.TEACHER
                        Role.STUDENT.name -> Role.STUDENT
                        else -> Role.OTHER
                    }

                    if (firstName != null && lastName != null && age != null && phoneNumber != null) {
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
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener(onFailure)

    }
}
