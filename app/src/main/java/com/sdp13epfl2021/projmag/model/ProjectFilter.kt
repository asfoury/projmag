package com.sdp13epfl2021.projmag.model

import java.io.Serializable

/**
 *  An Immutable project Filter
 *  It's constructor takes filtering parameters and its `invoke` function operator
 *  takes a project and return `true`/`false`, whether it matches or not the constructor
 *  parameters
 *
 *  **If applied is used you should define a check function with `setApplicationCheck` otherwise
 *  no filtering will be applied**
 *
 * @property bachelor if it is true will require the project degree be bachelor, otherwise it has
 *                    no effect on filtering.
 * @property master if it is true will require the project degree to be master, otherwise it has
 *                  no effect on filtering.
 * @property applied if it is true will require the project to be one the user applied to
 *                   (i.e. `isAppliedProject` is true), otherwise it has no effect on filtering.
 * @property favorite if it is true will require the project to be in the favorite list to
 *                   (i.e. `favoriteProject` is true), otherwise it has no effect on filtering.
 */
data class ProjectFilter(
    val bachelor: Boolean = false,
    val master: Boolean = false,
    val applied: Boolean = false,
    val favorite: Boolean = false,
    val own: Boolean = false
) : Serializable {

    companion object {
        /**
         * Create a ProjectFilter from a Map of field name to the value
         *
         * @param data the map field name -> value
         * @return a ProjectFilter built from the value in the `data` map
         */
        operator fun invoke(data: Map<String, Any>): ProjectFilter =
            ProjectFilter(
                bachelor = data["bachelor"] as? Boolean ?: false,
                master = data["master"] as? Boolean ?: false,
                applied = data["applied"] as? Boolean ?: false,
                favorite = data["favorites"] as? Boolean ?: false,
                own = data["own"] as? Boolean ?: false
            )

    }

    /**
     * A function used to see if the user has applied to the project
     */
    @Transient private var isAppliedProject: ((ImmutableProject) -> Boolean)? = null
    @Transient private var isFavoriteProject : ((ImmutableProject) -> Boolean)? = null
    @Transient private var isOwnProject: ((ImmutableProject) -> Boolean)? = null

    /**
     * Tells if the given project match the constraints
     *
     * @param project the project to check constraint
     *
     * @return `true` if it matches, `false` otherwise
     */
    operator fun invoke(project: ImmutableProject): Boolean {
        var matches = true
        if (bachelor)
            matches = matches && project.bachelorProject
        if (master)
            matches = matches && project.masterProject
        if (applied)
            matches = matches && (isAppliedProject?.let { it(project) } ?: true)
        if (favorite)
            matches = matches && (isFavoriteProject?.let { it(project) } ?: true)
        if (own)
            matches = matches && (isOwnProject?.let { it(project) } ?: true)
        return matches
    }

    /**
     * Set the function that will check if the project is one the those the user applied to.
     *
     * @param appCheck Function that take a project and indicates if the user applied to it
     * @return this filter
     */
    fun setApplicationCheck(appCheck: (ImmutableProject) -> Boolean): ProjectFilter {
        isAppliedProject = appCheck
        return this
    }

    /**
     * Function that will check if the project is contained in the favorites
     *
     * @param favCheck : Function that will take a project and indicate if it's in the favorite list
     * @return this filter
     */
    fun setFavoriteCheck(favCheck: (ImmutableProject) -> Boolean): ProjectFilter {
        isFavoriteProject = favCheck
        return this
    }

    /**
     * Function that will check if the project was made by the user
     *
     * @param ownCheck function that takes a project and indicates whether it was made by the user
     * @return this filter
     */
    fun setOwnCheck(ownCheck: (ImmutableProject) -> Boolean): ProjectFilter {
        isOwnProject = ownCheck
        return this
    }


}
