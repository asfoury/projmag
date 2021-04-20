package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.model.*


//TODO delete when a real CandidatureDatabase is created
class DummyCandidatureDatabase: CandidatureDatabase {
    override fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(
            listOf(
                Candidature(
                    "mLaBLchyYVzxkRQZDhew",
                    "userID001",
                    (ImmutableProfile.build(
                    "lastName1",
                    "firstName1",
                    21,
                    Gender.MALE,
                    123456,
                    "021 123 45 67", Role.STUDENT) as Success).value,
                    CurriculumVitae(
                        "summary 1",
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    ),
                    Candidature.State.Rejected
                ),
                Candidature(
                    "mLaBLchyYVzxkRQZDhew",
                    "userID001",
                    (ImmutableProfile.build(
                    "lastName2",
                    "firstName2",
                    21,
                    Gender.MALE,
                    123456,
                    "021 123 45 67", Role.STUDENT) as Success).value,
                    CurriculumVitae(
                        "summary 2",
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    ),
                    Candidature.State.Waiting
                )
            )
        )
    }

    override fun pushCandidature(
        projectID: ProjectId,
        candidature: Candidature,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun acceptCandidature(
        projectID: ProjectId,
        userID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess()
    }

    override fun rejectCandidature(
        projectID: ProjectId,
        userID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess()
    }
}