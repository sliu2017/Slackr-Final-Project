package com.example.slackr

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


open class QuizResultFragment : DialogFragment() {
    // Dialog fragment for displaying the result of the learning style quiz.
    // The user can either close the quiz, or tap the 'Tips' button for pointers on their learning style.
    // Learning style tips are taken from http://www.educationplanner.org/students/self-assessments/learning-styles-quiz.shtml


    companion object {

        // Result of the quiz is sent as a dialog
        fun newInstance(type:String): QuizResultFragment {
            val fragment = QuizResultFragment()
            val bundle = Bundle()
            bundle.putString("msg", type) // set msg here
            fragment.arguments=bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create the dialog fragment

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.setMessage(R.string.tips)
                .setPositiveButton(R.string.tips
                ) { _, _ ->
                    val urlIntent= Intent(Intent.ACTION_VIEW)
                    urlIntent.data= Uri.parse("http://www.educationplanner.org/students/self-assessments/learning-styles-styles.shtml")
                    startActivity(urlIntent)
                }
                .setCancelable(false)
                .setNegativeButton(R.string.close
                ) { _, _ ->
                    activity!!.finish()
                }
                .setTitle("Quiz result")
                .setMessage("You're a(n) "+ arguments!!.get("msg") + " Learner/n " +
                        "Learn more about your learning style by clicking on tips.")
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
