package com.example.slackr

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PromptManageGroupFragment : DialogFragment() {

    // Dialog fragment for taking the user to manage groups that he/she created, or groups that he/she matches with

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseGroups: DatabaseReference
    private lateinit var databaseMatches: DatabaseReference
    internal lateinit var groups: MutableList<StudyGroup>

    companion object {

        fun newInstance(): DialogFragment {
            return PromptManageGroupFragment()
        }
    }

    private var mContext : Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseGroups = mDatabase!!.reference.child("Groups")
        mDatabaseReference = mDatabase!!.reference.child("Users")
        databaseMatches = mDatabase!!.reference.child("Matches").child(mAuth!!.currentUser!!.uid)
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

                .setPositiveButton("My matches"
                ) { _, _ ->
                    groups= ArrayList()

                    databaseMatches.orderByKey()
                        .addListenerForSingleValueEvent((object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                groups.clear()
                                var group: StudyGroup? = null

                                for (postSnapshot in dataSnapshot.children) {
                                    try {
                                        group = postSnapshot.getValue(StudyGroup::class.java)


                                    } catch (e: Exception) {

                                    } finally {
                                        groups.add(group!!)
                                    }
                                }

                                // Checks if user has matched with any groups

                                if (groups.isEmpty()) {

                                    Toast.makeText(
                                            mContext, "You Currently " +
                                            "Have No Matches " +
                                            "Please Find a Match", Toast.LENGTH_LONG).show()
                                    dismiss()

                                } else {
                                    /**
                                     *  display matches
                                     * **/
                                    //if (this@PromptManageGroupFragment.activity != null) {
                                        val intent = Intent(mContext, ManageGroupMatchesListActivity::class.java)
                                        intent.putExtra("matching_groups", groups as ArrayList<StudyGroup>)
                                        mContext?.startActivity(intent)
                                    //}

                                }

                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // do nothing
                            }
                        }))

                }
                .setCancelable(false)
                .setNegativeButton("Personal groups"
                ) { _, _ ->
                    groups= ArrayList()

                    databaseGroups.orderByChild("createdBy").equalTo(mAuth!!.currentUser!!.uid)
                        .addListenerForSingleValueEvent((object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                groups.clear()
                                var group: StudyGroup? = null

                                for (postSnapshot in dataSnapshot.children) {
                                    try {
                                        group = postSnapshot.getValue(StudyGroup::class.java)


                                    } catch (e: Exception) {

                                    } finally {
                                        groups.add(group!!)
                                    }
                                }

                                // Checks if user has created a group

                                if (groups.isEmpty()) {

                                    Toast.makeText(mContext, "You Currently " +
                                            "Have No Personal Groups " +
                                            "Please Find a Match or Create One", Toast.LENGTH_LONG).show()
                                    dismiss()

                                } else {
                                    /**
                                     *  display matches
                                     * **/
                                    //if (this@PromptManageGroupFragment.activity != null) {
                                        val intent = Intent(mContext, ManagePersonalGroupListActivity::class.java)
                                        intent.putExtra("matching_groups", groups as ArrayList<StudyGroup>)
                                        mContext?.startActivity(intent)
                                    //}

                                }

                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // do nothing
                            }
                        }))
                }
                .setTitle("Manage Groups")
                .setMessage("What groups do you want to manage?")
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}