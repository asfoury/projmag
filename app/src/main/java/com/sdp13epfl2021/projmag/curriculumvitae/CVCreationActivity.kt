@file:Suppress("MemberVisibilityCanBePrivate")

package com.sdp13epfl2021.projmag.curriculumvitae

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.fragments.CVFragmentCollection

class CVCreationActivity : AppCompatActivity() {

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
        /* DO SOMETHING WITH IT */
        finish()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = cvFrags.getItemCount()

        override fun createFragment(position: Int): Fragment = cvFrags[position]
    }
}