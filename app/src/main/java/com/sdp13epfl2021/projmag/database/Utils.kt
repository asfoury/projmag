package com.sdp13epfl2021.projmag.database

import com.google.firebase.firestore.FirebaseFirestore

object Utils {

    val projectsDatabase = CachedProjectsDatabase(FirebaseProjectsDatabase(FirebaseFirestore.getInstance()))

}