package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.database.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.model.Candidature

class FakeCandidatureDatabase(
    val candidaturesState: MutableMap<ProjectId, Map<String, Candidature.State>> = HashMap(),
    val candidatures: MutableMap<ProjectId, Map<String, Candidature>> = HashMap(),
    val onChanges: MutableMap<ProjectId, MutableList<(ProjectId, List<Candidature>) -> Unit>> = HashMap()
) : CandidatureDatabase {

    override fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(candidatures[projectID]?.values?.toList() ?: emptyList())
    }

    override fun pushCandidature(
        candidature: Candidature,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val projectID = candidature.projectId
        val userID = candidature.userID
        val newMap1 = candidaturesState[projectID] ?: emptyMap()
        val newMap2 = candidatures[projectID] ?: emptyMap()
        candidaturesState[projectID] = newMap1 + (userID to newState)
        candidatures[projectID] = newMap2 + (userID to candidature)
        onChanges[projectID]?.let { it.forEach { it(projectID, candidatures[projectID]?.values?.toList() ?: emptyList()) } }
        onSuccess()
    }

    override fun addListener(
        projectID: ProjectId,
        onChange: (ProjectId, List<Candidature>) -> Unit
    ) {
        onChanges[projectID]?.let { it.add(onChange) } ?: run { onChanges[projectID] = mutableListOf(onChange)}
    }
}