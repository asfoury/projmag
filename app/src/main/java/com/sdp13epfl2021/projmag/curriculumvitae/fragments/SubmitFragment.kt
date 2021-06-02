package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * A Fragment where the user can finish its CV by submitting it
 */
@AndroidEntryPoint
class SubmitFragment : Fragment() {

    private var callbacks: MutableList<() -> Unit> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_submission, container, false)

        view?.findViewById<Button>(R.id.cv_submission_button)?.setOnClickListener {
            callbacks.forEach { func -> func() }
        }
        return view
    }

    /**
     * add a callback function called when submitting CV
     */
    fun addCallback(callback: () -> Unit) = callbacks.add(callback)
}