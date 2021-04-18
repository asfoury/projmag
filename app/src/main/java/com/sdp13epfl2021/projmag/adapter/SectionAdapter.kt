package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R


class SectionAdapter(private val context : Context, private val sections : List<String>)
    : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>(){
    class SectionViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.section)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        //tag item is not yet defined
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_section, parent, false)
        return SectionViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.textView.text =  section
    }



    override fun getItemCount(): Int = sections.size

}