package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var loginBtn: Button? = null
    private var regBtn: Button? = null


    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)
        regBtn = findViewById<Button>(R.id.register)

        loginBtn!!.setOnClickListener { loginUserAccount() }
        regBtn!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    // TODO: Allow the user to log into their account
    // If the email and password are not empty, try to log in
    // If the login is successful, store info into intent and launch DashboardActivity
    private fun loginUserAccount() {

        val email: String = userEmail!!.text.toString()
        val password: String = userPassword!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val uid= mAuth!!.currentUser!!.uid
                    Log.i("log in successful", uid)
                    if (mAuth!!.currentUser!!.isEmailVerified){
                        val intent=Intent(this@LoginActivity, DashBoardActivity::class.java)
                        startActivity(intent)
                    } else{
                        Toast.makeText(
                            applicationContext,
                            "Please verify your account",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent=Intent(this@LoginActivity, VerifyActivity::class.java)
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


    }

    companion object {
        const val USER_EMAIL = "com.example.slackr.useremail"
        const val USER_ID = "user_id"

    }
}