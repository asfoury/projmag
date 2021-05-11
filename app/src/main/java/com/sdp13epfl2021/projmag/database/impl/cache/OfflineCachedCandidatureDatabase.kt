package com.sdp13epfl2021.projmag.database.impl.cache

import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.loadFromFile
import com.sdp13epfl2021.projmag.database.saveToFile
import com.sdp13epfl2021.projmag.model.Candidature
import java.io.File
import java.io.Serializable


/**
 * This is an implementation of CandidatureDatabase that keep candidatures both in persistent storage and memory.
 * If we are offline and firebase cache is empty/disabled, it will use the data stored locally.
 * If we are offline and firebase cache is enabled, it will use both the data stored locally and in the cache (priority to `db`).
 * If we are not offline, it will use the data return by `db` (up to date).
 */
class OfflineCachedCandidatureDatabase(
    private val db: CandidatureDatabase,
    private val projectsDir: File
) : CandidatureDatabase {

    private val candidatureFilename: String = "candidatures.data"
    private val candidatures: MutableMap<ProjectId, List<Candidature>> = HashMap()

    /**
     * Load candidatures from the local storage.
     */
    init {
        try {
            projectsDir.listFiles()?.let {
                it
                    .filter(File::isDirectory)
                    .mapNotNull(File::listFiles)
                    .mapNotNull { arr -> arr.find { f -> f.parentFile != null && f.isFile && f.name == candidatureFilename } }
                    .forEach { f ->
                        candidatures[f.parentFile!!.name] = loadFromFile(
                            f,
                            SerializableCandidatureListWrapper::class
                        )?.list ?: emptyList()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * This serializable class is used to wrap a list of Candidature.
     */
    private data class SerializableCandidatureListWrapper(val list: List<Candidature>) :
        Serializable

    /**
     * Save the current candidatures for a given projectId to the local storage.
     *
     * @param projectId the id of the project.
     */
    private fun saveCandidature(projectId: ProjectId) {
        try {
            val projectDir: File = File(projectsDir, projectId)
            projectDir.mkdirs()
            saveToFile(
                File(projectDir, candidatureFilename),
                SerializableCandidatureListWrapper(candidatures[projectId]!!)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get the current candidatures for a given projectId in the cache.
     *
     * @param projectID the id of the project.
     *
     * @return a list of candidatures
     */
    private fun getLocalCandidatures(projectID: ProjectId): List<Candidature> {
        return candidatures[projectID] ?: emptyList()
    }

    /**
     * Update the local cache with the remoteList merge to the local candidatures.
     * Then save the new candidatures to local storage.
     * @param projectID the id of the project.
     * @param remoteList the remote list of candidatures.
     */
    private fun merge(projectID: ProjectId, remoteList: List<Candidature>): List<Candidature> {
        val localList: List<Candidature> = getLocalCandidatures(projectID).filter { c ->
            remoteList.all { r -> r.userID != c.userID }
        }
        val totalList = localList + remoteList
        candidatures[projectID] = totalList
        saveCandidature(projectID)
        return totalList
    }

    override fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.getListOfCandidatures(projectID, { remoteList ->
            //If we are offline, the remoteList could be empty or incomplete (cache).
            //We only keep them if they don't collide, with userID (priority to remote => up to date)
            val totalList: List<Candidature> = merge(projectID, remoteList)
            onSuccess(totalList)
        }, {
            //If the db failed to load (for example offline mode), we return the local cached list
            onSuccess(getLocalCandidatures(projectID))
        })
    }

    override fun pushCandidature(
        candidature: Candidature,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val projectId: ProjectId = candidature.projectId
        val oldList: List<Candidature> = candidatures[projectId] ?: emptyList()
        candidatures[projectId] = oldList + candidature
        saveCandidature(projectId)
        db.pushCandidature(candidature, newState, onSuccess, onFailure)
    }

    override fun removeCandidature(
        projectId: ProjectId,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun addListener(
        projectID: ProjectId,
        onChange: (ProjectId, List<Candidature>) -> Unit
    ) {
        db.addListener(projectID) { _, newCandidatures ->
            val totalList: List<Candidature> = merge(projectID, newCandidatures)
            onChange(projectID, totalList)
        }
        getListOfCandidatures(projectID, {}, {}) //preload the candidatures
    }
}