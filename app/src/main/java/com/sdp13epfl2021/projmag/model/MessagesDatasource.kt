package com.sdp13epfl2021.projmag.model

class MessagesDatasource {
    fun loadMessages(): List<Message> {
        val profile: ImmutableProfile? = when (val profile = ImmutableProfile.build(
            "lastName", "firstName", 23, Gender.MALE, 289473, "0100041067",
            Role.STUDENT
        )) {
            is Success -> profile.value
            is Failure -> null
        }
        return createMessageList(profile)
    }

    private fun createMessageList(profile: ImmutableProfile?): List<Message> {
        return listOf<Message>(
            Message(
                "Hello! I had a question, does this project require a solid background in image processing or can I learn as I work on it?",
                profile!!,
                10000
            ),
            Message(
                "The Internet (or internet) is the global system of interconnected computer networks that uses the Internet protocol suite (TCP/IP) to communicate between networks and devices. It is a network of networks that consists of private, public, academic, business, and government networks of local to global scope, linked by a broad array of electronic, wireless, and optical networking technologies. The Internet carries a vast range of information resources and services, such as the inter-linked hypertext documents and applications of the World Wide Web (WWW), electronic mail, telephony, and file sharing.",
                profile,
                10000
            ),
            Message("Hello!", profile, 10000),
            Message("Hello!", profile, 10000),
            Message("Hello!", profile, 10000),
            Message(
                "The Internet (or internet) is the global system of interconnected computer networks that uses the Internet protocol suite (TCP/IP) to communicate between networks and devices. It is a network of networks that consists of private, public, academic, business, and government networks of local to global scope, linked by a broad array of electronic, wireless, and optical networking technologies. The Internet carries a vast range of information resources and services, such as the inter-linked hypertext documents and applications of the World Wide Web (WWW), electronic mail, telephony, and file sharing.",
                profile,
                10000
            ),
            Message("Hello!", profile, 10000)
        )
    }

}