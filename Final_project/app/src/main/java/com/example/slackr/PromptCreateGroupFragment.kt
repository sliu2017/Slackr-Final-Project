package com.example.slackr

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PromptCreateGroupFragment : DialogFragment() {

    companion object {

        fun newInstance(subject:String, code:String): DialogFragment {
            val fragment = PromptCreateGroupFragment()
            val bundle = Bundle()
            bundle.putString("subject", subject)// set msg here,
            bundle.putString("code",code)
            fragment.arguments=bundle
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

                .setPositiveButton("Yes"
                ) { _, _ ->
                    val newFragment = CreateGroupFragment.newInstance(arguments!!.get("subject") as String,
                    arguments!!.get("code") as String)
                    newFragment.show(activity!!.supportFragmentManager, "create_group")
                }
                .setCancelable(false)
                .setNegativeButton("No"
                ) { _, _ ->
                    activity!!.finish()
                }
                .setTitle("Create a Group")
                .setMessage("A study group for this class doesn't already exist... Would you like to create a new study group?")
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}