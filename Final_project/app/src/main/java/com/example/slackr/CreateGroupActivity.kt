package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity

class CreateGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

       /* var mGroupName: EditText = findViewById(R.id.groupName)
        var mGroupDescription: EditText = findViewById(R.id.groupParticipantLimit)
        var mGroupLogistics: EditText = findViewById(R.id.groupLogistics)
        var mGroupParticipantLimit: EditText = findViewById(R.id.groupParticipantLimit)
        var mCancelGroupButton: Button = findViewById(R.id.cancelGroupButton)
        var mCreateGroupButton: Button = findViewById(R.id.createGroupButton)



        mCancelGroupButton!!.setOnClickListener {
            val intent = Intent(this@CreateGroupActivity, DashBoardActivity::class.java)
            startActivity(intent)
        }

        mCreateGroupButton!!.setOnClickListener {
            val intent = Intent(this@CreateGroupActivity, DashBoardActivity::class.java)
            startActivity(intent)
        }*/

    }
}