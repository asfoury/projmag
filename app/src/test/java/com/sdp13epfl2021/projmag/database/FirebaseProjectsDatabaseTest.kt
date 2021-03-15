package com.sdp13epfl2021.projmag.database

import org.junit.Test

/**
 *  Simple tests as this is just an abstraction of the
 *  Firebase/Firestore API
 */
class FirebaseProjectsDatabaseTest {
    /**
     * test that no unexpected behaviour when passing null
     */
    @Test
    fun pushNullProjectShouldNotCrash(){
        FirebaseProjectsDatabase.pushProject(null,
            {},
            {})
    }

    @Test
    fun removeProjectsChangeListenerShouldNotCrash(){
        FirebaseProjectsDatabase.removeProjectsChangeListener {  }
    }
}