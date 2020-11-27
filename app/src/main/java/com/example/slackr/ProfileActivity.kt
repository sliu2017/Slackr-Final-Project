package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseError
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*


class ProfileActivity : AppCompatActivity() {
    // add option to link to fb/twitter, and if no linked, send email when connecting

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()


        val mProfileText = findViewById<TextView>(R.id.nameText)
        val mBackButton = findViewById<Button>(R.id.backbutton)
        val mSecurityButton = findViewById<CardView>(R.id.security)
        val mPersonalButton = findViewById<CardView>(R.id.personal)

        val uid = mAuth!!.currentUser?.uid
        var first = ""
        var last = ""
        uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                first = snapshot.child("fname").value as String
                last = snapshot.child("lname").value as String
                mProfileText.text = "$first $last's Profile"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        mProfileText.text = "$first $last's Profile"



        mBackButton.setOnClickListener{
            finish()
        }
        mSecurityButton.setOnClickListener{
            val intent = Intent(this@ProfileActivity, SecurityActivity::class.java)
            startActivity(intent)
        }
        mPersonalButton.setOnClickListener{
            val intent = Intent(this@ProfileActivity, PersonalActivity::class.java)
            startActivity(intent)
        }
    }
}

class SecurityActivity: AppCompatActivity(){

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    val TAG = "Profile-Security"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_security)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        val mBackButton = findViewById<Button>(R.id.backbutton)

        val mUpdateButton = findViewById<Button>(R.id.update)

        val mOldEmailText = findViewById<EditText>(R.id.oldEmail)
        val mOldPwText = findViewById<EditText>(R.id.oldPw)
        val mNewEmailText = findViewById<EditText>(R.id.newEmail)
        val mNewPwText = findViewById<EditText>(R.id.newPw)

        mBackButton.setOnClickListener{
            finish()
        }

        mUpdateButton.setOnClickListener{
            val email = mOldEmailText.text.toString()
            val pw = mOldPwText.text.toString()

            if(email.isEmpty()|| pw.isEmpty()){
                return@setOnClickListener
            }

            val credential = EmailAuthProvider
                .getCredential(email, pw)
            mAuth!!.currentUser?.reauthenticate(credential)!!.addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                }
            }

            if(!mNewEmailText.text.isBlank()) {
                mAuth!!.currentUser?.updateEmail(mNewEmailText.text.toString())
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Email change successful", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            if(!mNewPwText.text.isBlank()) {
                mAuth!!.currentUser?.updatePassword(mNewPwText.text.toString())?.addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Password change successful", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            finish()
        }
    }
}

class PersonalActivity : AppCompatActivity(){
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_personal)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        val uid = mAuth!!.currentUser?.uid
        val currUser = uid?.let { mDatabaseReference!!.child(it) }

        var first = ""
        var last = ""
        var major = ""
        var minor = ""
        var school = ""
        var learningStyle = ""
        val mFirstName = findViewById<EditText>(R.id.firstName)
        val mLastName = findViewById<EditText>(R.id.lastName)
        val mCurrentMajor = findViewById<EditText>(R.id.currentMajors)
        val mCurrentMinor = findViewById<EditText>(R.id.currentMinors)
        val mCurrentSchool = findViewById<EditText>(R.id.currentSchool)
        val mLearningStyle = findViewById<TextView>(R.id.learningStyle)

        val mBackButton = findViewById<Button>(R.id.backbutton)
        val mUpdateButton = findViewById<Button>(R.id.update)

        mBackButton.setOnClickListener{
            finish()
        }


        mUpdateButton.setOnClickListener{
            currUser!!.child("fname").setValue(mFirstName.text.toString())
            currUser!!.child("lname").setValue(mLastName.text.toString())
            currUser!!.child("major").setValue(mCurrentMajor.text.toString())
            currUser!!.child("minor").setValue(mCurrentMinor.text.toString())
            currUser!!.child("school").setValue(mCurrentSchool.text.toString())
            finish()
        }

        uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                first = default(snapshot.child("fname")?.value)
                last = default(snapshot.child("lname")?.value)
                major = default(snapshot.child("major")?.value)
                minor = default(snapshot.child("minor")?.value)
                school = default(snapshot.child("school")?.value)
                learningStyle = default(snapshot.child("style_of_learning")?.value)
                mFirstName.setText(first)
                mLastName.setText(last)
                mCurrentMajor.setText(major)
                mCurrentMinor.setText(minor)
                mCurrentSchool.setText(school)
                mLearningStyle.text = learningStyle
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun default(value : Any?) :String{
        if(value != null){
            return value as String
        }
        return ""
    }

}