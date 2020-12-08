package com.example.slackr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("NAME_SHADOWING")
class QuizActivity : AppCompatActivity() {

    // Activity for taking the learning style quiz

    private var btnNext: Button? = null
    private var questionTv: TextView? =null
    private var questionCountTv: TextView? =null
    private var mTactile : RadioButton? =null
    private var mVisual : RadioButton? =null
    private var mAudible : RadioButton? =null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val questionList= Constants.getQuestions()
        btnNext = findViewById(R.id.next_submit)
        questionTv = findViewById(R.id.question)
        mTactile = findViewById(R.id.tactile)
        mVisual = findViewById(R.id.visual)
        mAudible = findViewById(R.id.audible)
        questionCountTv = findViewById(R.id.question_number)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        var count =0
        var audibleRes=0
        var visualRes=0
        var tactileRes=0

        val que= questionList[count]
        var id= que.id
        questionCountTv!!.text= id.toString() +  " of 10"
        questionTv!!.text= que.question
        mTactile!!.text = que.tactile
        mVisual!!.text= que.visual
        mAudible!!.text= que.audible

        btnNext!!.setOnClickListener {
            count++

            if( mTactile!!.isChecked){
                tactileRes++
            }
            if ( mAudible!!.isChecked){
                audibleRes++
            }
            if( mVisual!!.isChecked){
                visualRes++
            }
            if(id < 10){
                val que = questionList[id]
                questionCountTv!!.text= que.id.toString() +  " of 10"
                id= que.id
                questionTv!!.text= que.question
                mTactile!!.text = que.tactile
                mVisual!!.text= que.visual
                mAudible!!.text= que.audible
            }
            if (id==10) {
                btnNext!!.text = "Submit"
                id+=1
            } else {
                if (id==11){
                    val type: String = if(tactileRes >= audibleRes && tactileRes >= visualRes){
                        "Tactile"
                    } else if(visualRes >= audibleRes && visualRes >= tactileRes){
                        "Visual"

                    } else{
                        "Audible"
                    }
                    val uid= mAuth!!.currentUser!!.uid
                    val childUpdates = hashMapOf<String, Any>(
                        "/Users/$uid/style_of_learning" to type
                        
                    )
                    mDatabase!!.reference.updateChildren(childUpdates)
                    Log.i(TAG, type)
                    val newFragment = QuizResultFragment.newInstance(type)
                    newFragment.show(supportFragmentManager, "quiz_result")
                }
            }
            Log.i(TAG, id.toString())
        }

    }
    companion object {
        const val TAG = "Final_Project"
        const val TYPE = "type"

    }
}