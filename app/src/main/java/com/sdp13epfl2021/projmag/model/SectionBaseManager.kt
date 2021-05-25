package com.sdp13epfl2021.projmag.model

class SectionBaseManager {
    companion object {
        private val sectionsList = listOf<String>(
            "Math",
            "Chemistry",
            "Physics",
            "CS",
            "Systems Communication",
            "EE",
            "Mechanical",
            "Microtechnics",
            "Material",
            "Life Sciences",
            "Architecture",
            "Civil",
            "Environmental"
        )

        /**
         * Check if a list of section is valid
         * return a boolean
         *
         * @param listToBeChecked : list of section need to be check
         */
        fun isListValid(listToBeChecked: List<String>): Boolean =
            listToBeChecked.all(sectionsList::contains)

        /**
         * Function is to access list of section
         * return a list of section
         */


        fun sectionList(): List<String> {
            return sectionsList
        }
    }


}