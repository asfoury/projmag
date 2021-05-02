package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.Candidature
import java.io.File
import java.io.Serializable


private const val CANDIDATURE_FILENAME: String = "candidatures.data"

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

    private val candidatures: MutableMap<ProjectId, List<Candidature>> = HashMap()

    init {
        try {
            projectsDir.listFiles()?.let { it
                .filter(File::isDirectory)
                .mapNotNull(File::listFiles)
                .mapNotNull { arr -> arr.find { f -> f.parentFile != null && f.isFile && f.name == CANDIDATURE_FILENAME } }
                .forEach { f ->
                    candidatures[f.parentFile!!.name] = LocalFileUtils.loadFromFile(f, SerializableCandidatureListWrapper::class)?.list ?: emptyList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private data class SerializableCandidatureListWrapper(val list: List<Candidature>) :  Serializable

    private fun saveCandidature(projectId: ProjectId) {
        try {
            val projectDir: File = File(projectsDir, projectId)
            projectDir.mkdirs()
            LocalFileUtils.saveToFile(
                File(projectDir, CANDIDATURE_FILENAME),
                SerializableCandidatureListWrapper(candidatures[projectId]!!)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun merge(projectID: ProjectId, remoteList: List<Candidature>): List<Candidature> {
        val localList = candidatures[projectID]?.let {
            it.filter {
                    c -> remoteList.all { r -> r.userID != c.userID }
            }
        } ?: emptyList()
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
        }, onFailure)
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