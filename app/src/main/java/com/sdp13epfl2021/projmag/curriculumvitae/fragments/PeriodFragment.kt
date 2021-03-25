package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R

class PeriodFragment(private val titleName: String) : Fragment() {
    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_period, container, false)
        val title: TextView? = view.findViewById(R.id.cv_period_title)
        title?.apply {
            text = titleName
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun get(): String {
        TODO("Not yet implemented")
    }
}