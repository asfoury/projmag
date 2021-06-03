package com.sdp13epfl2021.projmag.model

/**
 * This Class is used to define a Message that can be send as a comment
 * createdAt parameter is the time in milliseconds check Date()
 */

data class Message(val messageContent: String, val sender: ImmutableProfile, val createdAt: Long) {

}