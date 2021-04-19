package com.sdp13epfl2021.projmag.model

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.ProjectId

data class Candidature(val projectId: ProjectId, val userID: String, val profile: ImmutableProfile, val cv: CurriculumVitae)