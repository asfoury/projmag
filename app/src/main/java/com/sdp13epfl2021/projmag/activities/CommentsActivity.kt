package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.MessageListAdapter
import com.sdp13epfl2021.projmag.database.interfaces.CommentsDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CommentsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageListAdapter


    @Inject
    lateinit var  commentsDB : CommentsDatabase

    @Inject
    lateinit var userDB: UserdataDatabase

    @Inject
    @Named("currentUserId")
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        //get the users profileId
        val profileId : String = userId
        // send a message
        // get the project id
        val projectId = intent.extras?.getString("projectId")
        setUpSendButton(profileId, projectId)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_comments)
        commentsDB.getCommentsOfProject(projectId!!, {
            recyclerView.adapter = MessageListAdapter(commentsDB,this, it, userDB)
            recyclerView.setHasFixedSize(false)
            recyclerView.scrollToPosition(it.size - 1)
        }, {})

        commentsDB.addListener(projectId) { _: ProjectId, _: List<Message> ->
            this.runOnUiThread {
                commentsDB.getCommentsOfProject(projectId, {
                    recyclerView.adapter = MessageListAdapter(commentsDB,this, it, userDB)
                    recyclerView.setHasFixedSize(false)
                    recyclerView.scrollToPosition(it.size - 1)
                }, {})
            }
        }
    }

    private fun setUpSendButton(profileId : String?, projectId : String?) {
        if(profileId != null) {
            val sendCommentButton = findViewById<ImageButton>(R.id.comments_send_button)
            sendCommentButton.setOnClickListener {
                val editTextView = findViewById<EditText>(R.id.comments_edit_text)
                val messageText = editTextView.text.toString()
                when (val message = Message.build(messageText, profileId, Date().time) ) {
                    is Success ->
                    {
                        commentsDB.addCommentToProjectComments(message.value, (projectId ?: ""),{}, {})
                        editTextView.text.clear()
                    }
                    is Failure -> Log.d("MYTEST","Error occurred when sending message : ${message.reason}")
                }
            }
        }
    }
}
