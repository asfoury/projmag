package com.sdp13epfl2021.projmag.database.impl.firebase

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ImmutableProject.Companion.FieldNames
import com.sdp13epfl2021.projmag.model.ImmutableProject.Companion.FieldNames.toSearchName
import java.util.*

/**
 * A Firebase Firestore Database of Projects
 */
class FirebaseProjectDatabase(private val firestore: FirebaseFirestore) : ProjectDatabase {
    /**
     * the Root collection in Firebase
     */
    companion object {
        const val ROOT = "projects"
    }

    private var listeners: Map<((ProjectChange) -> Unit), ListenerRegistration> = emptyMap()

    /**
     * Take a `DocumentSnapshot` from Firebase and return a `Project`
     *
     * @param doc the Firebase document
     * @return a Project built from the given document
     */

    private fun documentToProject(doc: DocumentSnapshot): ImmutableProject? =
        if (doc.data == null) {
            null
        } else {
            ImmutableProject.buildFromMap(doc.data!!, doc.id)
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
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val queryRef = firestore.collection(ROOT)
            .whereArrayContainsAny(field, elements)
        queryRef
            .get()
            .addOnSuccessListener { query ->
                val project = query?.mapNotNull { documentToProject(it) } ?: listOf()
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
                    ?.map { doc -> doc.id } ?: listOf()
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getProjectFromId(
        id: ProjectId,
        onSuccess: (ImmutableProject?) -> Unit,
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
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val colRef = firestore.collection(ROOT)
        colRef
            .get()
            .addOnSuccessListener { query ->
                val project =
                    query?.mapNotNull { doc ->
                        documentToProject(doc)
                    } ?: listOf()
                onSuccess(project)
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getProjectsFrom(
            name.toLowerCase(Locale.ROOT).split(" "),
            FieldNames.NAME.toSearchName(),
            onSuccess,
            onFailure
        )
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val listOfTags = tags.flatMap { tag -> tag.toLowerCase(Locale.ROOT).split(" ") }
        getProjectsFrom(
            listOfTags,
            FieldNames.TAGS.toSearchName(),
            onSuccess,
            onFailure
        )
    }


    override fun pushProject(
        project: ImmutableProject,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        project.let {
            if (project.id.isEmpty()) {
                firestore.collection(ROOT).add(
                    it.toMapString()
                ).addOnSuccessListener { doc -> onSuccess(doc.id) }
            } else {
                firestore.collection(ROOT).document(project.id).set(
                    it.toMapString()
                ).addOnSuccessListener { id -> onSuccess(project.id) }
            }
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

    override fun updateVideoWithProject(
        id: ProjectId,
        uri: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(ROOT).document(id)
        getProjectFromId(
            id,
            {
                it?.let { project ->
                    docRef
                        .update(
                            FieldNames.VIDEO_URI,
                            project.videoUri + uri
                        )
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener(onFailure)
                }
            },
            onFailure
        )
    }

    override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        val listener = firestore
            .collection(ROOT)
            .addSnapshotListener { snapshot, _ ->
                for (doc in snapshot!!.documentChanges) {
                    documentToProject(doc.document)?.let {
                        val project: ImmutableProject = it
                        val type = when (doc.type) {
                            DocumentChange.Type.ADDED -> ProjectChange.Type.ADDED
                            DocumentChange.Type.MODIFIED -> ProjectChange.Type.MODIFIED
                            DocumentChange.Type.REMOVED -> ProjectChange.Type.REMOVED
                        }
                        changeListener(ProjectChange(type, project))
                    }
                }
            }
        synchronized(this) {
            listeners = listeners + (changeListener to listener)
        }
    }

    @Synchronized
    override fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        val listener = listeners[changeListener]
        if (listener != null) {
            listener.remove()
            listeners = listeners - changeListener
        }
    }
}
