package com.sdp13epfl2021.projmag.database.impl.cache

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.SerializedStringListWrapper
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.database.loadFromFile
import com.sdp13epfl2021.projmag.database.saveToFile
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import com.sdp13epfl2021.projmag.model.ProjectFilter
import java.io.File
import javax.inject.Inject


/**
 * This is an implementation of UserdataDatabase that keep users' data both in persistent storage and memory.
 * If we are offline and firebase cache is empty/disabled, it will use the data stored locally.
 * If we are offline and firebase cache is enabled, it will use both the data stored locally and in the cache (priority to `db`).
 * If we are not offline, it will use the data return by `db` (up to date).
 *
 * Favorites/Applied will not use `db` (expect push) because it is only for the local user use.
 */
class OfflineCachedUserdataDatabase @Inject constructor(
    private val db: UserdataDatabase,
    private val localUserID: String,
    private val usersRootDir: File

) : UserdataDatabase {

    private val cvFilename: String = "cv.data"
    private val favoritesFilename: String = "favorites.data"
    private val appliedFilename: String = "applied.data"
    private val profileFilename: String = "profile.data"
    private val preferencesFilename: String = "preferences.data"

    private val cvs: MutableMap<String, CurriculumVitae> = HashMap()
    private val favorites: MutableSet<ProjectId> = HashSet()
    private val applied: MutableSet<ProjectId> = HashSet()
    private val profiles: MutableMap<String, ImmutableProfile> = HashMap()
    private var preferences: ProjectFilter? = null

    private val localUserDir: File = File(usersRootDir, localUserID)

    private val cvFile: File = File(localUserDir, cvFilename)
    private val favoritesFile: File = File(localUserDir, favoritesFilename)
    private val appliedFile: File = File(localUserDir, appliedFilename)
    private val preferencesFile: File = File(localUserDir, preferencesFilename)


    init {
        localUserDir.mkdirs()
        loadUsersData()

        loadFromFile(favoritesFile, SerializedStringListWrapper::class)?.let {
            favorites.addAll(it.list)
        }
        loadFromFile(appliedFile, SerializedStringListWrapper::class)?.let {
            applied.addAll(it.list)
        }
        loadFromFile(preferencesFile, ProjectFilter::class)?.let {
            preferences = it
        }
    }

    private fun loadUsersData() {
        try {
            usersRootDir.listFiles()?.forEach { userDir ->
                userDir.listFiles()?.forEach { file ->
                    file.parentFile?.name?.let { userID ->
                        when (file.name) {
                            cvFilename -> loadFromFile(
                                file,
                                CurriculumVitae::class
                            )?.let { cv ->
                                cvs[userID] = cv
                            }

                            profileFilename -> loadFromFile(
                                file,
                                ImmutableProfile::class
                            )?.let { profile ->
                                profiles[userID] = profile
                            }

                            //others
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFile(userID: String, filename: String): File {
        val userDir: File = File(usersRootDir, userID)
        userDir.mkdirs()
        return File(userDir, filename)
    }

    private fun saveFavorites() {
        saveToFile(favoritesFile, SerializedStringListWrapper(favorites.toList()))
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
        saveToFile(cvFile, cv)
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
            db.getCv(userID, { cv ->
                cv?.let {
                    cvs[userID] = cv
                    saveToFile(getFile(userID, cvFilename), cv)
                }
                onSuccess(cv)
            }, onFailure)
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
        saveToFile(appliedFile, SerializedStringListWrapper(applied.toList()))
        db.applyUnapply(apply, projectId, onSuccess, onFailure)
    }

    override fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(applied.toList())
    }

    override fun getPreferences(
        onSuccess: (ProjectFilter?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        preferences?.let {
            onSuccess(it)
        } ?: run {
            db.getPreferences({
                it?.let {
                    preferences = it
                    saveToFile(preferencesFile, it)
                }
                onSuccess(it)
            }, onFailure)
        }
    }

    override fun pushPreferences(
        pf: ProjectFilter,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        preferences = pf
        saveToFile(preferencesFile, pf)
        db.pushPreferences(pf, onSuccess, onFailure)
    }

    override fun uploadProfile(
        profile: ImmutableProfile,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        profiles[localUserID] = profile
        saveToFile(getFile(localUserID, profileFilename), profile)
        db.uploadProfile(profile, onSuccess, onFailure)
    }

    override fun getProfile(
        userID: String,
        onSuccess: (profile: ImmutableProfile?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        profiles[userID]?.let {
            onSuccess(it)
        } ?: run {
            db.getProfile(userID, { profile ->
                profile?.let {
                    profiles[userID] = profile
                    saveToFile(getFile(userID, profileFilename), profile)
                }
                onSuccess(profile)
            }, onFailure)
        }
    }
}