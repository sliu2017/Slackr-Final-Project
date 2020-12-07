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


class ProfileActivity : AppCompatActivity() {
    // add option to link to fb/twitter, and if no linked, send email when connecting

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


        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultUri = Uri.parse("android.resource://com.example.slackr/drawable/user")

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


        //change to match with registrationactivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedImage = data?.data
            try {

                if (selectedImage != null) {


                    getBitmapFromUri(selectedImage)

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(selectedImage.toString()))
                        .build()

                    mAuth!!.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT)
                            }
                        })
                    /*mStorageReference!!.child("images/" + mAuth!!.currentUser?.uid).putFile(
                        selectedImage
                    )*/

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

    fun getBitmapFromUri(photoUri: Uri){
        var mBitmap: Bitmap? = null

        val imageStream =
            photoUri?.let { this@ProfileActivity.contentResolver.openInputStream(it) };
        mBitmap = BitmapFactory.decodeStream(imageStream);

        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(resources, R.drawable.user)
        }
        val buttonLoadImage = findViewById<ImageView>(R.id.profilepic)
        buttonLoadImage.setImageBitmap(mBitmap)

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(mAuth!!.currentUser?.uid, photoUri.toString())
            apply()
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

        val mUpdateButton = findViewById<Button>(R.id.update)

        val mOldEmailText = findViewById<EditText>(R.id.oldEmail)
        val mOldPwText = findViewById<EditText>(R.id.oldPw)
        val mNewEmailText = findViewById<EditText>(R.id.newEmail)
        val mNewPwText = findViewById<EditText>(R.id.newPw)

        val image = findViewById<ImageView>(R.id.imageButton2)
        //image.setImageResource(R.drawable.ic_lo)

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


        val mUpdateButton = findViewById<Button>(R.id.update)




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

    fun defaultInt(value: Any?) : Long {
        if(value != null){
            return value as Long
        }
        return 0
    }

    fun default(value: Any?) :String{
        if(value != null){
            return value as String
        }
        return ""
    }

}