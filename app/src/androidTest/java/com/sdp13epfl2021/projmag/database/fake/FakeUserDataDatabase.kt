package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.database.UserDataDatabase
import com.sdp13epfl2021.projmag.model.ProjectFilter
import java.util.Collections.emptyList

class FakeUserDataDatabase : UserDataDatabase {
    override fun pushFavoriteProject(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun pushListOfFavoriteProjects(
        projectIDs: List<ProjectId>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getListOfFavoriteProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun removeFromFavorite(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun pushCv(
        cv: CurriculumVitae,
        onSuccess: () -> Unit,
        onFailure: (java.lang.Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun applyUnapply(
        applied: Boolean,
        project: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

    }

    override fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(ArrayList<ProjectId>())
    }

    override fun getPreferences(
        onSuccess: (ProjectFilter?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun pushPreferences(
        pf: ProjectFilter,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}