package com.sdp13epfl2021.projmag.model

class SectionBaseManager {
    companion object{
        private val sectionsList = listOf<String>(
            "Math", "Chemistry","Physics","CS", "Systems Communication","EE","Mechanical","Microtechnics"
            ,"Material Eng","Life Sciences","Architecture", "Civil Eng","Environmental Eng"
        )
    }

    fun sectionList(): List<String> {
        return sectionsList
    }

    fun isListValid(listToBeChecked : List<String>) : Boolean = listToBeChecked.all(sectionsList::contains)


}