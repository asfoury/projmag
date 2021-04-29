package com.sdp13epfl2021.projmag.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.model.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.*

/**
 * An implementation of a candidature database
 * using Google Firebase/FireStore
 */
class FirebaseCandidatureDatabase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userDataDatabase: UserDataDatabase
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

    private fun buildCandidature(dataMap: Map<String, Any>, projectID: ProjectId): List<Candidature> {
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

    private suspend fun addCandidature(candidatures: Queue<Candidature>, projectID: ProjectId, userID: String, state: Candidature.State) {
        var waiting = true
        var profile: ImmutableProfile? = null
        var cv: CurriculumVitae? = null
        /*userDataDatabase.getProfile(userID, { profile = it }, { waiting = false})*/ //TODO add when implemented
        /*userDataDatabase.getCV(userID, { cv = it }, { waiting = false })*/ //TODO add when implemented
        profile = dummyProfile(userID)
        cv = dummyCV(userID)

        while (waiting && (profile == null || cv == null)) {
            delay(10)
        }
        if (profile != null && cv != null) {
            candidatures.add(Candidature(projectID, userID, profile, cv, state))
        }
    }

    //TODO Remove after Profile/CV are implemented
    private fun dummyProfile(userID: String): ImmutableProfile {
        return (ImmutableProfile.build(
            userID,
            userID,
            21,
            Gender.MALE,
            123456,
            "021 123 45 67", Role.STUDENT) as Success).value
    }

    //TODO Remove after Profile/CV are implemented
    private fun dummyCV(userID: String): CurriculumVitae {
        return CurriculumVitae(
            "summary of $userID",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
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
                    } ?: onFailure(Exception("Candidature document invalid for projectID : $projectID"))
                } else {
                    onSuccess(emptyList())
                }
            }
            .addOnFailureListener(onFailure)
    }

    override fun pushCandidature(
        candidature: Candidature,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDoc(candidature.projectId)
            .set(mapOf(candidature.userID to newState), SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

}