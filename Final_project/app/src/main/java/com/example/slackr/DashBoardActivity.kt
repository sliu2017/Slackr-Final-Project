package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dashboard_activity.*

class DashBoardActivity : AppCompatActivity() {
    
    private var logOutCard : CardView? = null
    private var profileCard : CardView? = null
    private var styleCard : CardView? = null
    private var matchesCard : CardView? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
            

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        logOutCard = findViewById(R.id.logout)
        profileCard = findViewById(R.id.profile)
        styleCard = findViewById(R.id.style)
        matchesCard = findViewById(R.id.matches)

        logOutCard!!.setOnClickListener {
            mAuth!!.signOut()
            finish()
            //val intent = Intent(this@DashBoardActivity, LoginActivity::class.java)
            //startActivity(intent)
        }
        styleCard!!.setOnClickListener {

            val intent = Intent(this@DashBoardActivity, QuizActivity::class.java)
            startActivity(intent)

        }




    }
    companion object {
        const val TAG = "Final_Project"
        const val TYPE = "type"

    }
}