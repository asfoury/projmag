package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import android.os.PowerManager
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Candidature(
    val projectId: ProjectId,
    val userID: String,
    val profile: ImmutableProfile,
    val cv: CurriculumVitae,
    val state: State
) : Parcelable, Serializable {

    enum class State {
        Waiting, Accepted, Rejected;
        companion object {
            fun enumOf(s: String?): State? {
                return when (s) {
                    Waiting.name -> Waiting
                    Accepted.name -> Accepted
                    Rejected.name -> Rejected
                    else -> null
                }
            }
        }
    }
}

