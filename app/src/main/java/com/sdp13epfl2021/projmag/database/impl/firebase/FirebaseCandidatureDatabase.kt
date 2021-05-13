package com.sdp13epfl2021.projmag.database.impl.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.Candidature
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * An implementation of a candidature database
 * using Google Firebase/FireStore
 */
class FirebaseCandidatureDatabase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userdataDatabase: UserdataDatabase
) : CandidatureDatabase {

    companion object {
        /**
         * Root collection for user-data
         */
        const val ROOT = "candidatures"

    }

    private fun getDoc(projectID: ProjectId): DocumentReference {
        return firestore
            .collection(ROOT)
            .document(projectID)
    }

    /**
     * Construct the list of candidatures, it will load each candidature asynchronously.
     *
     * @param dataMap a map containing the userID and the State of candidatures.
     * @param projectID the id of the project.
     * @return a list of candidatures.
     */
    private fun buildCandidature(
        dataMap: Map<String, Any>,
        projectID: ProjectId
    ): List<Candidature> {
        val candidatures = ConcurrentLinkedQueue<Candidature>()
        runBlocking {
            dataMap.map {
                launch(Dispatchers.IO) {
                    val userID = it.key
                    val state = Candidature.State.enumOf(it.value as? String)
                    if (state != null) {
                        addCandidature(candidatures, projectID, userID, state)
                    }
                }
            }.forEach { it.join() }
        }
        return candidatures.toList()
    }

    /**
     * Load profile and cv for a particular candidature, then add the resulting candidature to the collection of candidatures if valid.
     *
     * @param candidatures a queue where candidature will be inserted.
     * @param projectID the id of the project.
     * @param userID the id of the user of this candidature.
     * @param state the state of this candidature.
     */
    private suspend fun addCandidature(
        candidatures: Queue<Candidature>,
        projectID: ProjectId,
        userID: String,
        state: Candidature.State
    ) {
        var waiting = true
        var profile: ImmutableProfile? = null
        var cv: CurriculumVitae? = null
        userdataDatabase.getProfile(userID, { profile = it }, { waiting = false })
        userdataDatabase.getCv(userID, { cv = it }, { waiting = false })

        while (waiting && (profile == null || cv == null)) {
            delay(10)
        }
        if (profile != null && cv != null) {
            candidatures.add(Candidature(projectID, userID, profile!!, cv!!, state))
        }
    }


    override fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDoc(projectID)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    doc?.data?.let {
                        onSuccess(buildCandidature(it, projectID))
                    }
                        ?: onFailure(Exception("Candidature document invalid for projectID : $projectID"))
                } else {
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener(onFailure)
    }

    override fun pushCandidature(
        projectId: ProjectId,
        userId: String,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDoc(projectId)
            .set(mapOf(userId to newState), SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun removeCandidature(
        projectId: ProjectId,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDoc(projectId)
            .update(userId, FieldValue.delete())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun addListener(
        projectID: ProjectId,
        onChange: (ProjectId, List<Candidature>) -> Unit
    ) {
        getDoc(projectID)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.data?.let {
                    val candidatures: List<Candidature> = buildCandidature(it, projectID)
                    onChange(projectID, candidatures)
                }
            }
    }

}