package com.sdp13epfl2021.projmag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.Message
import java.util.*

/**
 * Adapter for a message to recycler view. Allows messages to be displayed in comments
 */
class MessageListAdapter(
    private val messages: List<Message>,
    private val userDatabase: UserdataDatabase
) : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    /**
     * Holder of project fields to display in list.
     */
    class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.message_content)
        val messageDate: TextView = view.findViewById(R.id.message_date)
        val messageSender: TextView = view.findViewById(R.id.message_sender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.message, parent, false)
        return MessageViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.messageContent
        holder.messageDate.text = Date(message.createdAt).toString()
        userDatabase.getProfile(message.userId, { senderProfile ->
            senderProfile?.let { profile ->
                holder.messageSender.text = profile.firstName
            }
        }, {})
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
