package com.example.slackr

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.IOException

//Set of classes responsible for the profile page

class ProfileActivity : AppCompatActivity() {

    // Main page for user profile
    // Contains profile picture and buttons to go to security settings and personal info settings

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var mStorageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        mStorageReference = FirebaseStorage.getInstance().reference


        val mProfileText = findViewById<TextView>(R.id.nameText)
        val mSecurityButton = findViewById<CardView>(R.id.security)
        val mPersonalButton = findViewById<CardView>(R.id.personal)
        val buttonLoadImage = findViewById<ImageView>(R.id.profilepic)
        val uid = mAuth!!.currentUser?.uid

        // Load profile picture

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultUri = Uri.parse("android.resource://com.example.slackr/drawable/profile_user_white")

        buttonLoadImage.setImageURI(Uri.parse(sharedPref.getString(uid, defaultUri.toString())))
        buttonLoadImage.setOnClickListener{
                val i = Intent(
                    Intent.ACTION_OPEN_DOCUMENT,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )
                startActivityForResult(i, 1)
        }

        mSecurityButton.setOnClickListener{
            val intent = Intent(this@ProfileActivity, SecurityActivity::class.java)
            startActivity(intent)
        }
        mPersonalButton.setOnClickListener{
            val intent = Intent(this@ProfileActivity, PersonalActivity::class.java)
            startActivity(intent)
        }


        var first = ""
        var last = ""



        uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object :
            ValueEventListener {

            // Get first/last name of user

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

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedImage = data?.data
            try {

                if (selectedImage != null) {




                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(selectedImage.toString()))
                        .build()

                    mAuth!!.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT)
                            }
                        })


                }
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }


}

class SecurityActivity: AppCompatActivity(){

    // Allows user to change email and password

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

        val mUpdateButton = findViewById<Button>(R.id.update)

        val mOldEmailText = findViewById<EditText>(R.id.oldEmail)
        val mOldPwText = findViewById<EditText>(R.id.oldPw)
        val mNewEmailText = findViewById<EditText>(R.id.newEmail)
        val mNewPwText = findViewById<EditText>(R.id.newPw)


        mUpdateButton.setOnClickListener{

            // Updates email and password fields, given that the correct login information is provided

            val email = mOldEmailText.text.toString()
            val pw = mOldPwText.text.toString()

            if(email.isEmpty()|| pw.isEmpty()){
                return@setOnClickListener
            }

            val credential = EmailAuthProvider
                .getCredential(email, pw)
            mAuth!!.currentUser?.reauthenticate(credential)!!.addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                }
            }

            if(!mNewEmailText.text.isBlank()) {
                mAuth!!.currentUser?.updateEmail(mNewEmailText.text.toString())
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Email Change Successful", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            if(!mNewPwText.text.isBlank()) {
                mAuth!!.currentUser?.updatePassword(mNewPwText.text.toString())?.addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Password Change Successful", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            finish()
        }
    }
}

class PersonalActivity : AppCompatActivity(){

    // Displays the personal information of each user

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

        val mUpdateButton = findViewById<Button>(R.id.update)

        // Update user fields

        mUpdateButton.setOnClickListener{
            currUser!!.child("fname").setValue(mFirstName.text.toString())
            currUser!!.child("lname").setValue(mLastName.text.toString())
            currUser!!.child("major").setValue(mCurrentMajor.text.toString())
            currUser!!.child("minor").setValue(mCurrentMinor.text.toString())
            currUser!!.child("school").setValue(mCurrentSchool.text.toString())

            finish()
        }

        uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object :
            ValueEventListener {

            // Retrieve user data from storage

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


    fun default(value: Any?) :String{
        if(value != null){
            return value as String
        }
        return ""
    }

}