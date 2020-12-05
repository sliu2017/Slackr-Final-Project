package com.example.slackr

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ManagePersonalGroupListActivity : AppCompatActivity() {

    private lateinit var matches: ArrayList<StudyGroup>
    private lateinit var listViewMatches: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_manage_activity)

        matches= intent.getParcelableArrayListExtra<StudyGroup>("matching_groups") as ArrayList<StudyGroup>
        listViewMatches = findViewById<View>(R.id.list_matches) as ListView
        listViewMatches.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val group = adapterView.getItemAtPosition(i)
            (group as StudyGroup).groupID?.let {
                UpdateGroupFragment.newInstance(
                    it
                )
            }?.show(supportFragmentManager, "update_group")

        }


        val groupListAdapter = GroupList(this@ManagePersonalGroupListActivity, matches)
        listViewMatches.adapter = groupListAdapter



    }
}