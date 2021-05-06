package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import java.io.File
import java.io.Serializable

private const val CV_FILENAME: String = "cv.data"
private const val FAVORITES_FILENAME: String = "favorites.data"
private const val APPLIED_FILENAME: String = "applied.data"

/**
 * This is an implementation of UserDataDatabase that keep users' data both in persistent storage and memory.
 * If we are offline and firebase cache is empty/disabled, it will use the data stored locally.
 * If we are offline and firebase cache is enabled, it will use both the data stored locally and in the cache (priority to `db`).
 * If we are not offline, it will use the data return by `db` (up to date).
 *
 * Favorites/Applied will not use `db` (expect push) because it is only for the local user use.
 */
class OfflineCachedUserDataDatabase(
    private val db: UserDataDatabase,
    private val localUserID: String,
    private val usersDir: File

) : UserDataDatabase {

    private val favorites: MutableSet<ProjectId> = HashSet()
    private val applied: MutableSet<ProjectId> = HashSet()
    private val cvs: MutableMap<String, CurriculumVitae> = HashMap()

    private val localUserDir: File = File(usersDir, localUserID)

    private val favoritesFile: File = File(localUserDir, FAVORITES_FILENAME)
    private val appliedFile: File = File(localUserDir, APPLIED_FILENAME)
    private val cvFile: File = File(localUserDir, CV_FILENAME)

    private data class SerializedStringListWrapper(val list: List<String>) : Serializable

    init {
        localUserDir.mkdirs()
        loadUsersData()

        LocalFileUtils.loadFromFile(favoritesFile, SerializedStringListWrapper::class)?.let {
            favorites.addAll(it.list)
        }
        LocalFileUtils.loadFromFile(appliedFile, SerializedStringListWrapper::class)?.let {
            applied.addAll(it.list)
        }
    }

    private fun loadUsersData() {
        try {
            usersDir.listFiles()?.forEach { file ->
                when (file.name) {
                    CV_FILENAME -> LocalFileUtils.loadFromFile(file, CurriculumVitae::class)?.let { cv ->
                        file.parent?.let { userID ->
                            cvs.put(userID, cv)
                        }
                    }
                    //TODO load profiles when merge here
                    //others
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveFavorites() {
        LocalFileUtils.saveToFile(favoritesFile, SerializedStringListWrapper(favorites.toList()))
    }



    override fun pushFavoriteProject(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        favorites.add(projectID)
        saveFavorites()
        db.pushFavoriteProject(projectID, onSuccess, onFailure)
    }

    override fun pushListOfFavoriteProjects(
        projectIDs: List<ProjectId>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        projectIDs.forEach(favorites::add)
        saveFavorites()
        db.pushListOfFavoriteProjects(projectIDs, onSuccess, onFailure)
    }

    override fun getListOfFavoriteProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(favorites.toList())
    }

    override fun removeFromFavorite(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        favorites.remove(projectID)
        saveFavorites()
        db.removeFromFavorite(projectID, onSuccess, onFailure)
    }

    override fun pushCv(
        cv: CurriculumVitae,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        cvs[localUserID] = cv
        LocalFileUtils.saveToFile(cvFile, cv)
        db.pushCv(cv, onSuccess, onFailure)
    }

    override fun getCv(
        userID: String,
        onSuccess: (CurriculumVitae?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        cvs[userID]?.let {
            onSuccess(it)
        } ?: run {
            db.getCv(userID, onSuccess, onFailure)
        }
    }

    override fun applyUnapply(
        apply: Boolean,
        projectId: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (apply) {
            applied.add(projectId)
        } else {
            applied.remove(projectId)
        }
        LocalFileUtils.saveToFile(appliedFile, SerializedStringListWrapper(applied.toList()))
        db.applyUnapply(apply, projectId, onSuccess, onFailure)
    }

    override fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(applied.toList())
    }
}