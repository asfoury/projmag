package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.database.UserDataDatabase
import com.sdp13epfl2021.projmag.model.Candidature

class FakeUserDataDatabase(
    val userID: String = "",
    var favorites: MutableSet<ProjectId> = HashSet(),
    var cvs: MutableMap<String, CurriculumVitae> = HashMap(),
    var applied: MutableSet<ProjectId> = HashSet(),
) : UserDataDatabase {


    override fun pushFavoriteProject(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        favorites.add(projectID)
        onSuccess()
    }

    override fun pushListOfFavoriteProjects(
        projectIDs: List<ProjectId>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        favorites.addAll(projectIDs)
        onSuccess()
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
        onSuccess()
    }

    override fun pushCv(
        cv: CurriculumVitae,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        cvs[userID] = cv
        onSuccess()
    }

    override fun getCv(
        userID: String,
        onSuccess: (CurriculumVitae?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(cvs[userID])
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
        onSuccess()
    }

    override fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(applied.toList())
    }
}