package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CreateGroupPromptActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group_prompt)

        val mNoButton: Button = findViewById(R.id.noButton)
        val mYesButton: Button = findViewById(R.id.yesButton)

        mNoButton!!.setOnClickListener {
            val intent = Intent(this@CreateGroupPromptActivity, DashBoardActivity::class.java)
            startActivity(intent)
        }

        mYesButton!!.setOnClickListener {
            val intent = Intent(this@CreateGroupPromptActivity, CreateGroupActivity::class.java)
            startActivity(intent)
        }

    }
}