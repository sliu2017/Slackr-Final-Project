package com.example.slackr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DisplayGroupFragment : DialogFragment() {

    private lateinit var  groupNameTV: TextView
    private lateinit var groupDescriptionTV:TextView
    private lateinit var groupLogisiticsTV:TextView
    private lateinit var groupParticipantTV:TextView
    private lateinit var closeTV : TextView



    companion object {

        fun newInstance(group: StudyGroup): DialogFragment {
            val fragment = DisplayGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable("group", group)
            fragment.arguments=bundle
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var createView=inflater.inflate(R.layout.fragment_display_group, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        groupNameTV = createView.findViewById(R.id.groupName)
        groupDescriptionTV = createView.findViewById(R.id.group_descrition)
        groupLogisiticsTV = createView.findViewById(R.id.group_logistics)
        groupParticipantTV = createView.findViewById(R.id.group_participant)
        closeTV = createView.findViewById(R.id.close_create)
        groupNameTV.text = arguments!!.getParcelable<StudyGroup>("group")!!.groupName
        groupDescriptionTV.text= arguments!!.getParcelable<StudyGroup>("group")!!.groupDescription
        groupLogisiticsTV.text= arguments!!.getParcelable<StudyGroup>("group")!!.groupLogistics
        groupParticipantTV.text= arguments!!.getParcelable<StudyGroup>("group")!!.groupParticipantLimit

        closeTV!!.setOnClickListener{
            dismiss()
        }


        return createView
    }


}