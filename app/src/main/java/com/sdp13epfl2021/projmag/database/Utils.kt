package com.sdp13epfl2021.projmag.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object Utils {

    val projectsDatabase =
        CachedProjectsDatabase(FirebaseProjectsDatabase(FirebaseFirestore.getInstance()))

    val fileDatabase = FirebaseFileDatabase(
        FirebaseStorage.getInstance(),
        FirebaseAuth.getInstance().currentUser.uid
    )
}