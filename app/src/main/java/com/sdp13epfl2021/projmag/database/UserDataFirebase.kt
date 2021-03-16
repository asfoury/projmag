package com.sdp13epfl2021.projmag.database

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

/**
 * An implementation of a user-data database
 * using Google Firebase/FireStore
 */
object UserDataFirebase : UserDataDatabase {

    /**
     * Root collection for user-data
     */
    private const val ROOT = "user-data"

    /**
     *  The field containing favorites
     */
    private const val FAVORITES_FIELD = "favorites"

    /**
     * A new Firebase FireStore instance
     *
     * @return A new Firebase FireStore instance
     */
    private fun getDB(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Return the current logged user or null if none
     *
     * @return current logged user or null
     */
    private fun getUser(): FirebaseUser? = Firebase.auth.currentUser

    /**
     * Return the document associated to the user
     *
     * @return document associated to the user
     */
    private fun getUserDoc(): DocumentReference? =
        getUser()?.let { user ->
            getDB()
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
                    val newList = (ls.toSet().union(projectIDs.toSet())).toList()
                    doc.set(
                        hashMapOf(
                            "favorites" to newList
                        ), SetOptions.merge()
                    )?.addOnSuccessListener { onSuccess() }?.addOnFailureListener(onFailure)
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
}