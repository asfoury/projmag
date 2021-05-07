package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R

/**
 * A specific adapter to be used with various kind of UI list views inside the
 * CV creation process. Represent some data set
 */
class CVListAdapter<T>(private val dataSet: MutableList<T>, private val remove: (T) -> Unit) :
    RecyclerView.Adapter<CVListAdapter<T>.ViewHolder>() {

    /**
     * ViewHolder for various CardViews
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * the text view of the CardView
         */
        val textView: TextView = view.findViewById(R.id.cv_cardview_textview)

        /**
         * The button of the card view
         * (usually used to delete the card)
         */
        val button: ImageButton = view.findViewById(R.id.cv_item_cardview_button)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CVListAdapter<T>.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CVListAdapter<T>.ViewHolder, position: Int) {
        val pr = dataSet[position]
        holder.textView.text = pr.toString()
        holder.button.setOnClickListener {
            remove(pr)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}