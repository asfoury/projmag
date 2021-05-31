package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.Language
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.Language.Level
import dagger.hilt.android.AndroidEntryPoint


/**
 * A Fragment requesting the user to input its spoken language along the respective level at it
 */
@AndroidEntryPoint
class LanguagesFragment : Fragment() {

    private val listLanguage: MutableList<Language> = mutableListOf()

    private val listLangAdapter = CVListAdapter(listLanguage, ::removeFromListLang)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_language, container, false)
        val spinner = view?.findViewById<Spinner>(R.id.cv_language_spinner)
        val values = Level.values()
        spinner?.adapter = activity?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.spinner_layout_cv,
                values
            )
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.cv_language_recyclerview)
        recyclerView?.adapter = listLangAdapter

        val addButton = view?.findViewById<Button>(R.id.cv_language_add)
        addButton?.setOnClickListener(::buttonIsClicked)

        return view
    }


    /**
     * Return the data given by the user
     *
     * @return the result of the Fragment, i.e. a list of Language
     */
    fun get() = listLanguage


    private fun addToListLang(lang: Language) {
        if (lang.isValid()) {
            listLanguage.addIfNotExisting(lang)
            listLanguage.sortBy { -(it.level.ordinal) }
            listLangAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(activity, "Invalid data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun removeFromListLang(lang: Language) {
        listLanguage.remove(lang)
        listLangAdapter.notifyDataSetChanged()
    }


    private fun buttonIsClicked(v: View) {
        val name = view?.findViewById<EditText>(R.id.cv_language_name)?.text?.toString()
        val level = view?.findViewById<Spinner>(R.id.cv_language_spinner)?.selectedItem as? Level

        if (name != null && level != null) {
            addToListLang(Language(name, level))
        } else {
            Toast.makeText(activity, "Couldn't get data", Toast.LENGTH_SHORT).show()
        }
    }
}