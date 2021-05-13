package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.Candidature

class FakeCandidatureDatabase(
    val candidaturesState: MutableMap<ProjectId, Map<String, Candidature.State>> = HashMap(),
    val candidatures: MutableMap<ProjectId, Map<String, Candidature>> = HashMap(),
    val onChanges: MutableMap<ProjectId, MutableList<(ProjectId, List<Candidature>) -> Unit>> = HashMap()
) : CandidatureDatabase {

    override fun getListOfCandidatures(
        projectId: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(candidatures[projectId]?.values?.toList() ?: emptyList())
    }

    override fun pushCandidature(
        projectId: ProjectId,
        userId: String,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
//        val projectId = candidature.projectId
//        val userId = candidature.userId
//        val newMap1 = candidaturesState[projectId] ?: emptyMap()
//        val newMap2 = candidatures[projectId] ?: emptyMap()
//        candidaturesState[projectId] = newMap1 + (userId to newState)
//        candidatures[projectId] = newMap2 + (userId to candidature)
//        onChanges[projectId]?.let { it.forEach { it(projectId, candidatures[projectId]?.values?.toList() ?: emptyList()) } }
        onSuccess()
    }

    override fun removeCandidature(
        projectId: ProjectId,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val temp = candidatures[projectId] ?: emptyMap()
        val newMap = temp.toMutableMap()
        newMap.remove(userId)
        candidatures[projectId] = newMap.toMap()
        onChanges[projectId]?.let {
            it.forEach {
                it(projectId, candidatures[projectId]?.values?.toList() ?: emptyList())
            }
        }
        onSuccess()
    }

    override fun addListener(
        projectId: ProjectId,
        onChange: (ProjectId, List<Candidature>) -> Unit
    ) {
        onChanges[projectId]?.let { it.add(onChange) } ?: run {
            onChanges[projectId] = mutableListOf(onChange)
        }
    }
}