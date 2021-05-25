@file:Suppress("MemberVisibilityCanBePrivate")

package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.fragments.CVFragmentCollection
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity in which one can create and submit a CV.
 */
@AndroidEntryPoint
class CVCreationActivity : AppCompatActivity() {

    @Inject
    lateinit var userDB: UserdataDatabase

    private lateinit var viewPager: ViewPager2
    private val cvFrags = CVFragmentCollection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_v_creation)

        viewPager = findViewById(R.id.cv_pager)

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        cvFrags.addCallbackOnSubmission(::onFinish)
    }

    override fun onBackPressed() { /* DO NOTHING TO AVOID LOOSING WORK */
    }

    fun buildCV() = cvFrags.buildCV()

    private fun onFinish() {
        val cv = buildCV()
        userDB.pushCv(cv, {
            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(this, getString(R.string.failure), Toast.LENGTH_LONG).show()
        })
        finish()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = cvFrags.getItemCount()

        override fun createFragment(position: Int): Fragment = cvFrags[position]
    }
}
