package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.Companion.PeriodDescription

class PeriodFragment() : Fragment() {

    private val listPeriod: MutableList<PeriodDescription> = mutableListOf()

    private val periodAdapter = CVListAdapter(listPeriod, ::removeFromListPeriod)

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
        view?.findViewById<TextView>(R.id.cv_period_title)?.apply {
            text = arguments?.getString(TITLE_KEY) ?: DEFAULT_TITLE
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.cv_period_recyclerview)
        recyclerView?.apply {
            adapter = periodAdapter
        }

        view?.findViewById<Button>(R.id.cv_period_add)?.setOnClickListener(::onAddButtonClicked)

        return view
    }

    /**
     * Get the data given by the user
     */
    fun get(): List<PeriodDescription> = listPeriod.toList()


    private fun getStringFromTextView(id: Int): String? =
        view?.findViewById<EditText>(id)?.text?.toString()


    private fun getIntFromTextView(id: Int): Int? =
        try {
            getStringFromTextView(id)?.toInt()
        } catch (e: NumberFormatException) {
            null
        }


    private fun updateListPeriod(perDesc: PeriodDescription) {
        if (perDesc.isValid()) {
            listPeriod.add(perDesc)
            listPeriod.sortBy { p -> p.from }
            listPeriod.distinct()
            periodAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(activity, "Invalid data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun removeFromListPeriod(perDesc: PeriodDescription) {
        listPeriod.remove(perDesc)
        periodAdapter.notifyDataSetChanged()
    }


    private fun onAddButtonClicked(v: View) {
        val school: String? = getStringFromTextView(R.id.cv_period_name)
        val description: String? = getStringFromTextView(R.id.cv_period_description)
        val location: String? = getStringFromTextView(R.id.cv_period_location)
        val from = getIntFromTextView(R.id.cv_period_from)
        val to = getIntFromTextView(R.id.cv_period_to)
        if (school != null && description != null && location != null && from != null && to != null) {
            updateListPeriod(PeriodDescription(school, description, location, from, to))
        } else {
            Toast.makeText(activity, "Couldn't get data", Toast.LENGTH_SHORT).show()
        }
    }
}