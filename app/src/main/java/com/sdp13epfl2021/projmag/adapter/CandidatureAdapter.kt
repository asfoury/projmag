package com.sdp13epfl2021.projmag.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.Candidature
import com.sdp13epfl2021.projmag.model.ImmutableProfile

class CandidatureAdapter(activity: Activity, private val utils: Utils, private val projectId: ProjectId) :
    RecyclerView.Adapter<CandidatureAdapter.CandidatureHolder>() {

    private var candidatures: MutableList<Candidature> = ArrayList()

    init {
        utils.candidatureDatabase.getListOfCandidatures(
            projectId,
            {
                candidatures.addAll(it)
                notifyDataSetChanged()
            },
            { showToast("Failed to load applications.\n$it") }
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatureHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.waiting_candidature, parent, false)
        return CandidatureHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CandidatureHolder, position: Int) {
        val candidature = candidatures[position]

        holder.firstnameView.text = candidature.profile.firstName
        holder.lastnameView.text = candidature.profile.lastName
        holder.sectionView.text = "IC" // TODO change when added : candidature.profile.section

        val context = holder.view.context
        holder.profileButton.setOnClickListener { openProfile(context, candidature.profile) }
        holder.cvButton.setOnClickListener { openCV(context, candidature.cv) }
        holder.acceptButton.setOnClickListener {
            utils.candidatureDatabase.acceptCandidature(
                projectId,
                candidature.userID,
                {
                    showToast("Candidature accepted.")
                    (holder.itemView as? CardView)?.setCardBackgroundColor(Color.GREEN)
                },
                { showToast("An error has occurred while accepting.\n$it") }
            )
        }
        holder.rejectButton.setOnClickListener {
            utils.candidatureDatabase.rejectCandidature(
                projectId,
                candidature.userID,
                {
                    showToast("Candidature rejected.")
                    (holder.itemView as? CardView)?.setCardBackgroundColor(Color.RED)
                },
                { showToast("An error has occurred while rejecting.\n$it") }
            )
        }
    }

    override fun getItemCount(): Int {
        return candidatures.size
    }

    private fun openProfile(context: Context, profile: ImmutableProfile) {
        val intent = Intent(context, MainActivity::class.java) //TODO change to profile view
        intent.putExtra(MainActivity.profile, profile)
        context.startActivity(intent)
    }

    private fun openCV(context: Context, cv: CurriculumVitae) {
        val intent = Intent(context, MainActivity::class.java) //TODO change to CV view
        intent.putExtra(MainActivity.cv, cv)
        context.startActivity(intent)
    }

    private val showToast: (String) -> Unit = { msg: String ->
        activity.runOnUiThread {
            Toast.makeText(
                activity,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    class CandidatureHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val firstnameView: TextView = view.findViewById(R.id.waiting_firstname)
        val lastnameView: TextView = view.findViewById(R.id.waiting_lastname)
        val sectionView: TextView = view.findViewById(R.id.waiting_section)
        val profileButton: Button = view.findViewById(R.id.waiting_profile)
        val cvButton: Button = view.findViewById(R.id.waiting_cv)
        val acceptButton: Button = view.findViewById(R.id.waiting_accept)
        val rejectButton: Button = view.findViewById(R.id.waiting_reject)
    }
}