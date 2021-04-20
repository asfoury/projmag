package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.ProjectId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Candidature(
    val projectId: ProjectId,
    val userID: String,
    val profile: ImmutableProfile,
    val cv: CurriculumVitae,
    val state: State
) : Parcelable {

    enum class State { Waiting, Accepted, Rejected }
}

