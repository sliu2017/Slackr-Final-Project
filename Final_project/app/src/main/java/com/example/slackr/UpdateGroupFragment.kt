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


class UpdateGroupFragment : DialogFragment() {

    private lateinit var  groupNameET: EditText
    private lateinit var groupDescriptionET:EditText
    private lateinit var groupLogisiticsET:EditText
    private lateinit var groupParticipantET:EditText
    var closeTV : TextView?= null
    var updatebtn: Button? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseGroups: DatabaseReference


    companion object {

        fun newInstance(groupID: String): DialogFragment {
            val fragment = UpdateGroupFragment()
            val bundle = Bundle()
            bundle.putString("groupID", groupID)
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
        var createView=inflater.inflate(R.layout.fragment_update_group, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        updatebtn= createView.findViewById(R.id.update_group_btn)
        groupNameET = createView.findViewById(R.id.groupName)
        groupDescriptionET = createView.findViewById(R.id.group_descrition)
        groupLogisiticsET = createView.findViewById(R.id.group_logistics)
        groupParticipantET = createView.findViewById(R.id.group_participant)
        closeTV = createView.findViewById(R.id.close_create)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        databaseGroups = mDatabase!!.reference.child("Groups")
        val groupId=arguments!!.getString("groupID")

        closeTV!!.setOnClickListener{
            dismiss()
        }
        updatebtn!!.setOnClickListener{


            if (groupId != null) {

                databaseGroups.child(groupId).child("groupName").setValue(groupNameET.text.toString())
                databaseGroups.child(groupId).child("groupDescription").setValue(groupDescriptionET.text.toString())
                databaseGroups.child(groupId).child("groupLogistics").setValue(groupLogisiticsET.text.toString())
                databaseGroups.child(groupId).child("groupParticipantLimit").setValue(groupParticipantET.text.toString())
                Toast.makeText(activity, "Group Updated", Toast.LENGTH_LONG).show()
            }

            dismiss()
        }


        return createView
    }


}