package com.example.slackr

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PromptNoGroupFragment : DialogFragment() {

    companion object {

        fun newInstance(): DialogFragment {
            return PromptNoGroupFragment()

        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

                .setPositiveButton("Ok"
                ) { _, _ ->
                    dismiss()
                    activity!!.finish()
                }
                .setCancelable(false)
                .setTitle("Result")
                .setMessage("There is currently no group other than your personal group(s) matching " +
                        "the search. Please Try again later")
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}