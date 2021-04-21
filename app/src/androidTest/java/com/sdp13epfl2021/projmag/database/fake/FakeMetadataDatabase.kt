package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.database.MetadataDatabase
import java.lang.Exception

class FakeMetadataDatabase : MetadataDatabase {
    override fun addSubtitlesToVideo(
        videoUri: String,
        language: String,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getSubtitlesFromVideo(
        videoUri: String,
        language: String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}