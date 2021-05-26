package com.sdp13epfl2021.projmag.model

data class Message(val messageContent: String, val sender : ImmutableProfile, val createdAt : Long) {

}