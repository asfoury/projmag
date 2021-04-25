package com.sdp13epfl2021.projmag.model

class SectionBaseManager {
    companion object{
        private val sectionsList = listOf<String>(
            "Math", "Chemistry","Physics","CS", "Systems Communication","EE","Mechanical","Microtechnics"
            ,"Material","Life Sciences","Architecture", "Civil","Environmental"
        )

        fun isListValid(listToBeChecked : List<String>) : Boolean = listToBeChecked.all(sectionsList::contains)

        fun sectionList(): List<String> {
            return sectionsList.toList() //safe copy
        }
    }






}