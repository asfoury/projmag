package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * A Fragment simply displaying an introduction with instruction to CV creation process
 */
@AndroidEntryPoint
class IntroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cv_intro, container, false)
    }
}