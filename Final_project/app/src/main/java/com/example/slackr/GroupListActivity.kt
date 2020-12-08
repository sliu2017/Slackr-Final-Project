package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class GroupListActivity : AppCompatActivity() {

    // Displays groups in a list

    private lateinit var matches: ArrayList<StudyGroup>
    private lateinit var listViewMatches: ListView
    private lateinit var createbtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_matches_activity)
        matches= intent.getParcelableArrayListExtra<StudyGroup>("matching_groups") as ArrayList<StudyGroup>
        listViewMatches = findViewById<View>(R.id.list_matches) as ListView
        listViewMatches.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val group = adapterView.getItemAtPosition(i)
            val newFragment = DisplayGroupFragment.newInstance(group as StudyGroup)
            newFragment.show(supportFragmentManager, "display_group")

        }


        val groupListAdapter = GroupList(this@GroupListActivity, matches)
        listViewMatches.adapter = groupListAdapter
        createbtn= findViewById<Button>(R.id.create_own_group)
        createbtn.setOnClickListener{
            val newFragment = CreateGroupFragment.newInstance(intent.getStringExtra("subject") as String,
                intent.getStringExtra("code") as String)
            newFragment.show(supportFragmentManager, "create_group")
        }


    }
}