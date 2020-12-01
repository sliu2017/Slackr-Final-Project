package com.example.slackr

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
            return
        }
        if (lname.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter a valid last name...", Toast.LENGTH_LONG).show()
            lnameTV!!.requestFocus()
            return
        }



        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...", Toast.LENGTH_LONG).show()
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(applicationContext, "Password must be at least 8 characters\nand have at least a letter and a number", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = User(fname, lname, email)


                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    /*FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profilepic").setValue(
                                            BitmapFactory.decodeResource(resources, R.drawable.user).toString())*/
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
                        Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()

                    }
                }
    }
}
