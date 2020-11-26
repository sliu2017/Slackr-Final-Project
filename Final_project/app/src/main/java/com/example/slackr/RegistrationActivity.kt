package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegistrationActivity : AppCompatActivity() {

    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var fnameTV: EditText ? = null
    private var lnameTV: EditText ? = null
    private var regBtn: Button? = null
    private var validator = Validators()

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        fnameTV = findViewById(R.id.fname)
        lnameTV = findViewById(R.id.lname)
        regBtn = findViewById(R.id.register)


        regBtn!!.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {


        val email: String = emailTV!!.text.toString()
        val password: String = passwordTV!!.text.toString()
        val fname : String = fnameTV!!.text.toString().trim()
        val lname : String = lnameTV!!.text.toString().trim()

        if (fname.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter a valid first name...", Toast.LENGTH_LONG).show()
            fnameTV!!.requestFocus()
            Log.i(TAG, "pass here3")
            return
        }
        if (lname.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter a valid last name...", Toast.LENGTH_LONG).show()
            lnameTV!!.requestFocus()
            Log.i(TAG, "pass here4")
            return
        }



        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...", Toast.LENGTH_LONG).show()
            Log.i(TAG, "pass here5")
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(applicationContext, "Please enter a valid password!", Toast.LENGTH_LONG).show()
            Log.i(TAG, "pass here6")
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(TAG, "pass here")
                        val user = User(fname, lname, email)
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()

                                    val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val user = FirebaseAuth.getInstance().currentUser!!
                                    user.delete()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.i("error", "User account deleted.")
                                            }
                                        }
                                    Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Log.i(TAG, "failed here1")
                        Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()

                    }
                }
    }
    companion object {
        const val TAG = "Final_Project"


    }
}
