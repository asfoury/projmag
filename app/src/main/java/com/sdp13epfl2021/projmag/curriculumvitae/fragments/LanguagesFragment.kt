package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae

class LanguagesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_language, container, false)
        val spinner: Spinner = view.findViewById(R.id.cv_language_spinner)
        val values = CurriculumVitae.Companion.LanguageLevel.Companion.Level.values()
        spinner.adapter = activity?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.spinner_layout_cv_language,
                values
            )
        }
        return view
    }

    fun get(): String {
        TODO("Not yet implemented")
    }
}