package com.sdp13epfl2021.projmag.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * A Firebase Firestore Database of Projects
 */
class FirebaseProjectsDatabase(private val firestore: FirebaseFirestore) : ProjectsDatabase {
    /**
     * the Root collection in Firebase
     */
    companion object {
        const val ROOT = "projects"
    }

    /**
     * Take a `DocumentSnapshot` from Firebase and return a `Project`
     *
     * @param doc the Firebase document
     * @return a Project built from the given document
     */
    @Suppress("UNCHECKED_CAST")
    private fun documentToProject(doc: DocumentSnapshot?): Project =
        doc?.let {
            DummyProject(
                name = doc["name"] as String,
                lab = doc["lab"] as String,
                teacher = doc["teacher"] as String,
                TA = doc["TA"] as String,
                nbParticipant = (doc["nbParticipant"] as Long).toInt(),
                assigned = (doc["assigned"] as? List<String>) ?: listOf(),
                masterProject = doc["masterProject"] as Boolean,
                bachelorProject = doc["bachelorProject"] as Boolean,
                tags = (doc["tags"] as? List<String>) ?: listOf(),
                isTaken = doc["isTaken"] as Boolean,
                description = doc["description"] as String
            )
        }

    /**
     * Perform a firebase query filtering from a specific `field`
     * that is a list of strings with the given `elements` (any of them match).
     * Pass the successful result to `onSuccess`. Otherwise, pass an exception to `onFailure`.
     *
     * @param elements elements to filter with (if any of them match)
     * @param field the field to filter
     * @param onSuccess called with successful result
     * @param onFailure called with exception
     */
    private fun getProjectsFrom(
        elements: List<String>,
        field: String,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val docRef = firestore.collection(ROOT)
            .whereArrayContainsAny(field, elements)
        docRef
            .get()
            .addOnSuccessListener { query ->
                val project = query?.map { documentToProject(it) } ?: listOf()
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getAllIds(onSuccess: (List<ProjectId>) -> Unit, onFailure: (Exception) -> Unit) {
        val colRef = firestore.collection(ROOT)
        colRef
            .get()
            .addOnSuccessListener { query ->
                val project = query
                    .map { doc -> doc.id }
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getProjectFromId(
        id: ProjectId,
        onSuccess: (Project) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(ROOT).document(id)
        docRef
            .get()
            .addOnSuccessListener { query ->
                val project = if (query != null && query.exists()) {
                    documentToProject(query)
                } else {
                    null
                }
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getAllProjects(
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val colRef = firestore.collection(ROOT)
        colRef
            .get()
            .addOnSuccessListener { query ->
                val project =
                    query?.map { doc ->
                        documentToProject(doc)
                    } ?: listOf()
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getProjectsFrom(
            name.toLowerCase(Locale.ROOT).split(" "),
            "name-search",
            onSuccess,
            onFailure
        )
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val listOfTags = tags.flatMap { tag -> tag.toLowerCase(Locale.ROOT).split(" ") }
        getProjectsFrom(
            listOfTags,
            "tags-search",
            onSuccess,
            onFailure
        )
    }

    override fun pushProject(
        project: Project,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        project?.let {
            firestore.collection(ROOT).add(
                it.toMapString()
            )
                .addOnSuccessListener { id -> onSuccess(id.id) }
                .addOnFailureListener { ex -> onFailure(ex) }
        }
    }

    override fun deleteProjectWithId(
        id: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection(ROOT)
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}