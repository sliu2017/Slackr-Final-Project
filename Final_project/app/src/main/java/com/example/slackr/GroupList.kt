package com.example.slackr

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView

class GroupList(private val context: Activity, private var matches: List<StudyGroup>) : ArrayAdapter<StudyGroup>(context,
    R.layout.layout_group_list, matches) {

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_group_list, null, true)

        val textViewName = listViewItem.findViewById<View>(R.id.textViewName) as TextView


        val group = matches[position]
        textViewName.text = group.groupName

        return listViewItem
    }


}