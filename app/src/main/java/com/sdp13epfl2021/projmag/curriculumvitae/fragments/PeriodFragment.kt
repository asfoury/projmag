package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R

class PeriodFragment() : Fragment() {

    companion object {
        private const val TITLE_KEY = "title"
        private const val DEFAULT_TITLE = "PERIOD"

        /**
         * Return an instance of PeriodFragment with the given title
         * This method is here, because fragment doesn't support direct
         * constructor argument
         *
         * @param title the title to be shown on the fragment
         * @return a PeriodFragment with the given title
         */
        fun getInstance(title: String): PeriodFragment {
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, title)
            val frag = PeriodFragment()
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_period, container, false)
        val title: TextView? = view?.findViewById(R.id.cv_period_title)
        title?.apply {
            text = arguments?.getString(TITLE_KEY) ?: DEFAULT_TITLE
        }
        return view
    }

    fun get(): String {
        TODO("Not yet implemented")
    }
}