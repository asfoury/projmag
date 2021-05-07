package com.sdp13epfl2021.projmag.database.interfaces

import java.lang.Exception

/**
 * An interface for a Database of various Metadata
 */
interface MetadataDatabase {
    /**
     * Link subtitles, corresponding to a language, to a videoUri
     * @param videoUri the uri to which the subtitles will be related
     * @param language the language code to link with the video
     * @param content the subtitles content in WEBVTT format
     * @param onSuccess called when operation succeed
     * @param onFailure called with an exception in case of failure
     */
    fun addSubtitlesToVideo(
        videoUri: String,
        language: String,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Get subtitles, corresponding to a language, to a videoUri
     * @param videoUri the uri which the subtitles is related to
     * @param language the language code linked with the video. (Found in VideoUtils.kt)
     * @param onSuccess called with the subtitles when operation succeed or with null if nothing could be found
     * @param onFailure called with an exception in case of failure
     */
    fun getSubtitlesFromVideo(
        videoUri: String,
        language: String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    )
}