package com.sdp13epfl2021.projmag.database

import android.os.Parcelable
import com.sdp13epfl2021.projmag.model.Candidature
import kotlinx.parcelize.Parcelize
import java.io.File
import java.io.Serializable


private const val CANDIDATURE_FILENAME: String = "candidatures.data"

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
                .mapNotNull { arr -> arr.find { f -> f.parentFile != null && f.name == CANDIDATURE_FILENAME && f.isFile } }
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

    override fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.getListOfCandidatures(projectID, { remoteList ->
            //If we are offline, the remoteList could be empty or incomplete (cache).
            //We only keep them if they don't collide, with userID (priority to remote => up to date)
            val localList = candidatures[projectID]?.let {
                it.filter {
                    c -> remoteList.all { r -> r.userID != c.userID }
                }
            } ?: emptyList()
            onSuccess(localList + remoteList)
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
}