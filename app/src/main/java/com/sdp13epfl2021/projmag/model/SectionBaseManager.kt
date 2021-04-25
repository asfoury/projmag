package com.sdp13epfl2021.projmag.model

class SectionBaseManager {
    companion object{
        private val sectionsList = listOf<String>(
            "Chemistry", "Mathematics", "Physics", "Computer Science", "Communication Systems",
            "Electrical engineering", "Mechanical Engineering", "MicroEngineering", "Life sciences",
            "Architecture", "Civil Engineering", "Environmental Sciences and Engineering"
        )
    }

    fun sectionList(): List<String> {
        return sectionsList
    }

    fun isListValid(listToBeChecked : List<String>) : Boolean = listToBeChecked.all(sectionsList::contains)


}