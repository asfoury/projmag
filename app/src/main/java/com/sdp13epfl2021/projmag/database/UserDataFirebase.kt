package com.sdp13epfl2021.projmag.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.model.ProjectFilter

/**
 * An implementation of a user-data database
 * using Google Firebase/FireStore
 */
class UserDataFirebase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserDataDatabase {

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
        }
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
        }
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
        }
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
        }
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
        }
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
    }
}
