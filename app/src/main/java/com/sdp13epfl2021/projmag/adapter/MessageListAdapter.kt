package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.Message

class MessageListAdapter( private val context : Context,  private val messages : List<Message>) : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    /**
     * Holder of project fields to display in list.
     */
    class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.message_content)
        val messageDate : TextView = view.findViewById(R.id.message_date)
        val messageSender : TextView = view.findViewById(R.id.message_sender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.message, parent, false)
        return MessageViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.messageContent
        holder.messageDate.text = message.createdAt.toString()
        holder.messageSender.text = message.sender.firstName

    }

    override fun getItemCount(): Int {
       return messages.size
    }

}