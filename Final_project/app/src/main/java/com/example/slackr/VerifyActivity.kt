package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class VerifyActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var verifyBtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_email_activity)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        verifyBtn = findViewById(R.id.verify)

        verifyBtn!!.setOnClickListener {

            val user = mAuth!!.currentUser
            user!!.sendEmailVerification().addOnCompleteListener {
                    task ->

                if(task.isSuccessful){

                        Toast.makeText(
                            applicationContext,
                            "Email Verification sent! Please check your email",
                            Toast.LENGTH_LONG
                        ).show()
                } else{
                    Toast.makeText(
                        applicationContext,
                        "Email Verification not sent! Please try again later!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            finish()
        }


    }



}