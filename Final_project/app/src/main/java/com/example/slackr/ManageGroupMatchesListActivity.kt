package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class ManageGroupMatchesListActivity : AppCompatActivity() {

    private lateinit var matches: ArrayList<StudyGroup>
    private lateinit var listViewMatches: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_manage_activity)
        matches= intent.getParcelableArrayListExtra<StudyGroup>("matching_groups") as ArrayList<StudyGroup>
        listViewMatches = findViewById<View>(R.id.list_manage_matches) as ListView
        listViewMatches.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val group = adapterView.getItemAtPosition(i)
            val newFragment = DisplayGroupFragment.newInstance(group as StudyGroup)
            newFragment.show(supportFragmentManager, "display_group")

        }


        val groupListAdapter = GroupList(this@ManageGroupMatchesListActivity, matches)
        listViewMatches.adapter = groupListAdapter



    }
}