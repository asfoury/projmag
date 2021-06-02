package com.sdp13epfl2021.projmag.model
/**
 * This Class is used to define a Message that can be send as a comment
 * createdAt parameter is the time in milliseconds check Date()
 */

data class Message(val messageContent: String, val sender : ImmutableProfile, val createdAt : Long) {
    companion object {
        object FieldNames {
            const val MESSAGE_CONTENT = "message"
            const val SENDER = "sender"
            const val CREATION_DATE = "creationDate"
        }
        private const val MAX_MESSAGE_SIZE = 200
        /**
         * Function that creates a Message
         * returns a message wrapped in a success wrapper, or a failure with the explanation wrapped as a string
         *
         * @param messageContent : content of the message
         * @param sender : profile of the user that send the message
         * @param createdAt : when the message was send
         */
        fun build(
            messageContent: String,
            sender : ImmutableProfile,
            createdAt: Long
        ) : Result<Message> {
            return when {
                messageContent.length > MAX_MESSAGE_SIZE || messageContent.isEmpty() -> Failure("Message is very long or empty")
                else -> Success(Message(messageContent,sender,createdAt))
            }
        }
    }

    fun toMapString() = hashMapOf(
        FieldNames.MESSAGE_CONTENT to messageContent,
        FieldNames.SENDER to sender.firstName,
        FieldNames.CREATION_DATE to createdAt
    )
}