package com.sdp13epfl2021.projmag

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.activities.ProfileEditPageActivity
import org.junit.Rule

class ProfilePageTestEdit {
    @get:Rule
    var activityRule: ActivityScenarioRule<ProfileEditPageActivity> =
        ActivityScenarioRule(ProfileEditPageActivity::class.java)


}