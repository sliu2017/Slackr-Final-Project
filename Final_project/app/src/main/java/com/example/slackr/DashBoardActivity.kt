package com.example.slackr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
        styleCard = findViewById(R.id.academics)
        matchesCard = findViewById(R.id.matches)

        logOutCard!!.setOnClickListener {
            mAuth!!.signOut()
            val intent = Intent(this@DashBoardActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        profileCard!!.setOnClickListener{
            val intent = Intent(this@DashBoardActivity, ProfileActivity::class.java)

            startActivity(intent)
        }

        styleCard!!.setOnClickListener{
            val intent = Intent(this@DashBoardActivity, QuizActivity::class.java)

            startActivity(intent)
        }

        matchesCard!!.setOnClickListener{
            val intent = Intent(this@DashBoardActivity, MatchActivity::class.java)

            startActivity(intent)
        }



    }
}