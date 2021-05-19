package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase

class FakeMetadataDatabase(var subs: Map<String, String?> = emptyMap()) : MetadataDatabase {
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
        onSuccess(subs[videoUri])
    }
}