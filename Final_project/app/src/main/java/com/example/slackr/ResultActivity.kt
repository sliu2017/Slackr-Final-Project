package com.example.slackr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {

    private var typeImg : ImageView? =null
    private var typeTv : TextView? = null
    private var retryBtn : Button? = null
    private var tipsBtn : Button? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        typeImg = findViewById(R.id.type_learner_img)
        typeTv= findViewById(R.id.quiz_result)
        retryBtn = findViewById(R.id.retry)
        tipsBtn = findViewById( R.id.tips)
        val type= intent.getStringExtra(TYPE)
        if (type != null) {
            Log.i(TAG, type)
        } else{
            Log.i(TAG, "was null")
        }
        typeTv!!.text = "You're a(n) ${type} Learner"
        // store the result in the database
        mDatabaseReference!!.child(mAuth!!.currentUser!!.uid).child("style_of_learning").setValue(type)

        retryBtn!!.setOnClickListener {
            startActivity(Intent(this@ResultActivity, QuizActivity::class.java ))
        }
        tipsBtn!!.setOnClickListener {
            val urlIntent= Intent(android.content.Intent.ACTION_VIEW)
            urlIntent.data= Uri.parse("http://www.educationplanner.org/students/self-assessments/learning-styles-styles.shtml")
            startActivity(urlIntent)
        }


    }
    companion object {
        const val TAG = "Final_Project"
        const val TYPE = "type"

    }
}