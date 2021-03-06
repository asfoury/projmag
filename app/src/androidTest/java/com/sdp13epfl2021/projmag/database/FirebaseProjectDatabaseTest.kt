package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import com.sdp13epfl2021.projmag.model.Success
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*


/**
 *  Simple tests as this is just an abstraction of the
 *  Firebase/Firestore API
 */
@Suppress("UNCHECKED_CAST")
class FirebaseProjectDatabaseTest {
    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    val mockColRef = Mockito.mock(CollectionReference::class.java)
    val mockDocRef = Mockito.mock(DocumentReference::class.java)
    val mockTaskCol: Task<QuerySnapshot> = Mockito.mock(Task::class.java) as Task<QuerySnapshot>
    val mockTaskDoc: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>
    val mockTaskVoid: Task<Void> =
        Mockito.mock(Task::class.java) as Task<Void>
    val mockQS: QuerySnapshot = Mockito.mock(QuerySnapshot::class.java)
    val mockDS: DocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    val mockQDS: QueryDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot::class.java)
    val mockQuery: Query = Mockito.mock(Query::class.java)

    val mockListener = Mockito.mock(ListenerRegistration::class.java)

    val mockFirebaseFirestoreEmtpy = Mockito.mock(FirebaseFirestore::class.java)
    val mockColRefEmpty = Mockito.mock(CollectionReference::class.java)
    val mockTaskColEmpty: Task<QuerySnapshot> =
        Mockito.mock(Task::class.java) as Task<QuerySnapshot>
    val mockQSEmpty: QuerySnapshot = Mockito.mock(QuerySnapshot::class.java)
    val mockDocRefEmpty = Mockito.mock(DocumentReference::class.java)
    val mockTaskDocEmpty: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>
    val mockDSEmpty: DocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    val mockQueryEmpty: Query = Mockito.mock(Query::class.java)

    val name = "epic roblox coding"
    val labName = "roblox labs"
    val authorId = "some author id"
    val projectManager = "kaou el roblox master"
    val teacher = "kaou el roblox master"
    val description = "epic roblox coding alll freaking day DAMN SON"
    val numberStudents = 3
    val listStudents = listOf("epic robloxxx programmer")
    val tagManager = TagsBaseManager()
    val tagList = tagManager.tagsListToStringList(tagManager.getAllTags())
    val sectionList = SectionBaseManager.sectionList().toList()
    val creationDate = Date()

    val ID = "some-id"

    val result = ImmutableProject.build(
        ID, name, labName, authorId, projectManager, teacher, numberStudents,
        listStudents, true, true, tagList, false, description,
        listOf(), sectionList, creationDate
    ) as Success<ImmutableProject>
    val project = result.value

    private fun newQDSIterator() = object : MutableIterator<QueryDocumentSnapshot> {
        private var nb = 1

        override fun hasNext(): Boolean = nb > 0

        override fun next(): QueryDocumentSnapshot {
            nb -= 1
            return mockQDS
        }

        override fun remove() {}
    }

    @Before
    fun setUpMocks() {

        // --- mockFirebaseFirestore ---
        Mockito
            .`when`(mockFirebaseFirestore.collection(FirebaseProjectDatabase.ROOT))
            .thenReturn(mockColRef)

        // --- mockColRef ---
        Mockito
            .`when`(mockColRef.get())
            .thenReturn(mockTaskCol)

        Mockito
            .`when`(mockColRef.document(ID))
            .thenReturn(mockDocRef)

        Mockito
            .`when`(
                mockColRef.whereArrayContainsAny(
                    Mockito.anyString(),
                    Mockito.anyListOf(String::class.java)
                )
            )
            .thenReturn(mockQuery)

        Mockito
            .`when`(mockColRef.addSnapshotListener(Mockito.any()))
            .thenReturn(mockListener)

        // --- mockDocRef ---
        Mockito
            .`when`(mockDocRef.get())
            .thenReturn(mockTaskDoc)

        Mockito
            .`when`(
                mockDocRef.update(
                    anyObject<String>(),
                    anyObject()
                )
            )
            .thenReturn(mockTaskVoid)

        // --- mockTaskCol ---
        Mockito
            .`when`(mockTaskCol.addOnSuccessListener(anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<QuerySnapshot>
                a.onSuccess(mockQS)
                mockTaskCol
            }

        Mockito
            .`when`(mockTaskCol.addOnFailureListener { Mockito.any(OnFailureListener::class.java) })
            .thenReturn(mockTaskCol)

        // --- mockTaskDoc ---
        Mockito
            .`when`(mockTaskDoc.addOnSuccessListener(anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<DocumentSnapshot>
                a.onSuccess(mockDS)
                mockTaskDoc
            }

        Mockito
            .`when`(mockTaskDoc.addOnFailureListener { Mockito.any(OnFailureListener::class.java) })
            .thenReturn(mockTaskDoc)

        // --- mockTaskDoc ---
        Mockito
            .`when`(mockTaskVoid.addOnSuccessListener(anyObject()))
            .then {
                mockTaskVoid
            }

        Mockito
            .`when`(mockTaskVoid.addOnFailureListener { Mockito.any(OnFailureListener::class.java) })
            .thenReturn(mockTaskVoid)

        // --- mockDS ---
        Mockito.`when`(mockDS.id).thenReturn(project.id)
        Mockito.`when`(mockDS.data).thenReturn(
            mapOf(
                "name" to project.name,
                "lab" to project.lab,
                "authorID" to project.authorId,
                "teacher" to project.teacher,
                "TA" to project.TA,
                "nbParticipant" to project.nbParticipant,
                "assigned" to project.assigned,
                "masterProject" to project.masterProject,
                "bachelorProject" to project.bachelorProject,
                "tags" to project.tags,
                "isTaken" to project.isTaken,
                "description" to project.description,
                "videoURI" to project.videoUri,
                "allowedSections" to project.allowedSections,
                "creationDate" to project.creationDate.time

            )
        )
        /*
        Mockito.`when`(mockDS["name"]).thenReturn(project.name)
        Mockito.`when`(mockDS["lab"]).thenReturn(project.lab)
        Mockito.`when`(mockDS["teacher"]).thenReturn(project.teacher)
        Mockito.`when`(mockDS["TA"]).thenReturn(project.TA)
        Mockito.`when`(mockDS["nbParticipant"]).thenReturn(project.nbParticipant.toLong())
        Mockito.`when`(mockDS["assigned"]).thenReturn(project.assigned)
        Mockito.`when`(mockDS["masterProject"]).thenReturn(project.masterProject)
        Mockito.`when`(mockDS["bachelorProject"]).thenReturn(project.bachelorProject)
        Mockito.`when`(mockDS["tags"]).thenReturn(project.tags)
        Mockito.`when`(mockDS["isTaken"]).thenReturn(project.isTaken)
        Mockito.`when`(mockDS["description"]).thenReturn(project.description)
        Mockito.`when`(mockDS["videoUri"]).thenReturn(project.videoUri)
        */

        // --- mockQS ---
        Mockito
            .`when`(mockQS.iterator())
            .thenReturn(newQDSIterator())

        // --- mockQDS ---
        Mockito
            .`when`(mockQDS.id)
            .thenReturn(ID)
        Mockito.`when`(mockQDS.data).thenReturn(
            mapOf(
                "name" to project.name,
                "lab" to project.lab,
                "authorID" to project.authorId,
                "teacher" to project.teacher,
                "TA" to project.TA,
                "nbParticipant" to project.nbParticipant,
                "assigned" to project.assigned,
                "masterProject" to project.masterProject,
                "bachelorProject" to project.bachelorProject,
                "tags" to project.tags,
                "isTaken" to project.isTaken,
                "description" to project.description,
                "videoURI" to project.videoUri,
                "allowedSections" to project.allowedSections,
                "creationDate" to project.creationDate.time
            )
        )
        /*
        Mockito.`when`(mockQDS["name"]).thenReturn(project.name)
        Mockito.`when`(mockQDS["lab"]).thenReturn(project.lab)
        Mockito.`when`(mockQDS["teacher"]).thenReturn(project.teacher)
        Mockito.`when`(mockQDS["TA"]).thenReturn(project.TA)
        Mockito.`when`(mockQDS["nbParticipant"]).thenReturn(project.nbParticipant.toLong())
        Mockito.`when`(mockQDS["assigned"]).thenReturn(project.assigned)
        Mockito.`when`(mockQDS["masterProject"]).thenReturn(project.masterProject)
        Mockito.`when`(mockQDS["bachelorProject"]).thenReturn(project.bachelorProject)
        Mockito.`when`(mockQDS["tags"]).thenReturn(project.tags)
        Mockito.`when`(mockQDS["isTaken"]).thenReturn(project.isTaken)
        Mockito.`when`(mockQDS["description"]).thenReturn(project.description)
        Mockito.`when`(mockQDS["videoUri"]).thenReturn(project.videoUri)
        */

        // ---  mockQuery ---
        Mockito
            .`when`(mockQuery.get())
            .thenReturn(mockTaskCol)


        // an empty firestore instance
        Mockito
            .`when`(mockFirebaseFirestoreEmtpy.collection(FirebaseProjectDatabase.ROOT))
            .thenReturn(mockColRefEmpty)
        Mockito
            .`when`(mockColRefEmpty.get())
            .thenReturn(mockTaskColEmpty)
        Mockito
            .`when`(mockTaskColEmpty.addOnSuccessListener(anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<QuerySnapshot>
                a.onSuccess(null)
                mockTaskColEmpty
            }
        Mockito
            .`when`(mockColRefEmpty.document(ID))
            .thenReturn(mockDocRefEmpty)
        Mockito
            .`when`(mockDocRefEmpty.get())
            .thenReturn(mockTaskDocEmpty)
        Mockito
            .`when`(mockTaskDocEmpty.addOnSuccessListener(anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<DocumentSnapshot>
                a.onSuccess(null)
                mockTaskDocEmpty
            }
        Mockito
            .`when`(
                mockColRefEmpty.whereArrayContainsAny(
                    Mockito.anyString(),
                    Mockito.anyListOf(String::class.java)
                )
            )
            .thenReturn(mockQueryEmpty)
        Mockito
            .`when`(mockQueryEmpty.get())
            .thenReturn(mockTaskColEmpty)
    }

    @Test
    fun getAllIdsIsCorrect() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getAllIds(
            { list -> assertEquals(listOf(ID), list) },
            {}
        )

        val dbEmpty: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestoreEmtpy)
        dbEmpty.getAllIds(
            { list -> assertEquals(emptyList<ProjectId>(), list) },
            {}
        )
    }

    @Test
    fun getProjectFromIdIsCorrect() {
        Mockito.`when`(mockDS.exists()).thenReturn(true)
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getProjectFromId(
            ID,
            { p -> assertEquals(project, p) },
            {}
        )

        val dbEmpty: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestoreEmtpy)
        dbEmpty.getProjectFromId(
            ID,
            { p -> assertEquals(null, p) },
            { assert(false) }
        )
    }

    @Test
    fun getProjectFromIdWhenDSDoNotExistsDoNotCrash() {
        Mockito.`when`(mockDS.exists()).thenReturn(false)
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getProjectFromId(
            ID,
            { p -> assertEquals(null, p) },
            {}
        )
    }

    @Test
    fun getAllProjectsIsCorrect() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getAllProjects(
            { lp -> assertEquals(listOf(project), lp) },
            {}
        )

        val dbEmpty: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestoreEmtpy)
        dbEmpty.getAllProjects(
            { lp -> assertEquals(emptyList<ImmutableProject>(), lp) },
            { assert(false) }
        )
    }

    @Test
    fun getProjectsFromNameIsCorrect() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getProjectsFromName(
            project.name,
            { lp -> assertEquals(listOf(project), lp) },
            {}
        )

        val dbEmpty: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestoreEmtpy)
        dbEmpty.getProjectsFromName(
            project.name,
            { lp -> assertEquals(emptyList<ImmutableProject>(), lp) },
            { assert(false) }
        )
    }

    @Test
    fun getProjectsFromTagsIsCorrect() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.getProjectsFromTags(
            project.tags,
            { lp -> assertEquals(listOf(project), lp) },
            {}
        )
    }

    @Test
    fun updateVideoWithProjectWorks() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        db.updateVideoWithProject(
            ID,
            "",
            {},
            {}
        )
    }

    /*
     * No test for deleteProjectWithId(...). It's implementation is only relying on Firebase calls,
     * and is very similar to the other methods.
     */

    @Test
    fun listenersShouldNotCrash() {
        val db: ProjectDatabase = FirebaseProjectDatabase(mockFirebaseFirestore)
        val listener: ((ProjectChange) -> Unit) = {}
        db.addProjectsChangeListener(listener)
        db.removeProjectsChangeListener(listener)
        db.removeProjectsChangeListener(listener)
    }
}