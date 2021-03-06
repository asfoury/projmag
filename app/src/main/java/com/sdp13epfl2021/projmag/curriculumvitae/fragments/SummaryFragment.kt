package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.sdp13epfl2021.projmag.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * A Fragment where the user should write a few sentence to introduce its CV,
 * a summary of its professional life.
 */
@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var content = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_summary, container, false)
        view.findViewById<EditText>(R.id.cv_summary_body)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    content = s?.toString() ?: ""
                }

            })

        return view
    }

    /**
     * Get the result of this Fragment
     *
     * @return the content of the summary.
     */
    fun get(): String = content

}