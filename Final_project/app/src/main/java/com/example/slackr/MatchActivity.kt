package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MatchActivity : AppCompatActivity() {

    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseGroups: DatabaseReference
    private lateinit var databaseMatches: DatabaseReference
    private var mDatabaseReference: DatabaseReference? = null
    internal lateinit var matches: MutableList<StudyGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val mSubject: Spinner = findViewById(R.id.subject)
        val mCode: EditText = findViewById(R.id.code)
        val mFindMatch: Button = findViewById(R.id.findButton)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseGroups = mDatabase!!.reference.child("Groups")
        mDatabaseReference = mDatabase!!.reference.child("Users")
        databaseMatches = mDatabase!!.reference.child("Matches").child(mAuth!!.currentUser!!.uid)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MatchActivity,
            android.R.layout.simple_spinner_item, subjectArr
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSubject.adapter = adapter

        mFindMatch!!.setOnClickListener {

            var subject = mSubject.selectedItem.toString()
            var code = mCode.text.toString()

            val uid = mAuth!!.currentUser?.uid
            matches = ArrayList()
            uid?.let { mDatabaseReference!!.child(it) }?.addListenerForSingleValueEvent(object :
                ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    val learningStyle = snapshot.child("style_of_learning").value as String

                    if (learningStyle.isEmpty()) {

                        Toast.makeText(
                            applicationContext, "you don\'t have a learning" +
                                    "style set. Please take this short quiz to find" +
                                    "your learning style", Toast.LENGTH_LONG
                        ).show()
                        finish()
                        val intent = Intent(this@MatchActivity, QuizActivity::class.java)
                        startActivity(intent)
                    } else {
                        val key: String = subject + code + learningStyle

                        Log.i(TAG, key)
                        databaseGroups.orderByChild("searchKey").equalTo(key)
                            .addListenerForSingleValueEvent((object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    Log.i(TAG, "here 1")
                                    matches.clear()
                                    var group: StudyGroup? = null
                                    var count = 0
                                    var mGroupId: String? = null
                                    for (postSnapshot in dataSnapshot.children) {
                                        try {
                                            Log.i(TAG, "here 2")
                                            group = postSnapshot.getValue(StudyGroup::class.java)
                                            group!!.groupName?.let { it1 -> Log.i(TAG, it1) }
                                            mGroupId = postSnapshot.key

                                        } catch (e: Exception) {

                                        } finally {

                                            if (!group!!.createdBy.equals(mAuth!!.currentUser!!.uid)) {
                                                Log.i(TAG, "here 3")
                                                matches.add(group!!)

                                                if (mGroupId != null) {
                                                    databaseMatches.child(mGroupId).setValue(group)
                                                }

                                            } else {
                                                Log.i(TAG, "here 4")
                                                count++
                                            }

                                        }
                                    }
                                    Log.i(TAG, matches.toString())
                                    Log.i(TAG, count.toString())
                                    Log.i(TAG, "here 5")
                                    if (matches.isEmpty() && count == 0) {

                                        val newFragment =
                                            PromptCreateGroupFragment.newInstance(subject, code)
                                        newFragment.show(
                                            supportFragmentManager,
                                            "prompt_create_group"
                                        )
                                    } else if (matches.isEmpty() && count >= 1) {
                                        Log.i(TAG, "here 6")
                                        val newFragment = PromptNoGroupFragment.newInstance()
                                        newFragment.show(supportFragmentManager, "no_group")
                                    } else {
                                        Log.i(TAG, "here 7")
                                        /**
                                         *  display matches
                                         * **/
                                        val intent = Intent(
                                            this@MatchActivity,
                                            GroupListActivity::class.java
                                        )
                                        Log.i(TAG, matches.toString())
                                        intent.putExtra(
                                            "matching_groups",
                                            matches as ArrayList<StudyGroup>
                                        )
                                        intent.putExtra("code", code)
                                        intent.putExtra("subject", subject)
                                        startActivity(intent)
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // do nothing
                                }
                            }))
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }

    }

    companion object {

        const val TAG = "Final_Project"

        val subjectArr = arrayOf(
            "AASP",
            "AAST",
            "AGNR",
            "AMSC",
            "AMST",
            "ANSC",
            "ANTH",
            "AOSC",
            "ARAB",
            "ARCH",
            "AREC",
            "ARHU",
            "ARMY",
            "ARSC",
            "ARTH",
            "ARTT",
            "ASTR",
            "BCHM",
            "BEES",
            "BIOE",
            "BIOL",
            "BIOM",
            "BIPH",
            "BISI",
            "BMGT",
            "BMSO",
            "BSCI",
            "BSCV",
            "BSGC",
            "BSOS",
            "BSST",
            "BUAC",
            "BUDT",
            "BUFN",
            "BULM",
            "BUMK",
            "BUSI",
            "BUSM",
            "BUSO",
            "CBMG",
            "CCJS",
            "CHBE",
            "CHEM",
            "CHIN",
            "CHPH",
            "CHSE",
            "CLAS",
            "CLFS",
            "CMLT",
            "CMSC",
            "COMM",
            "CPBE",
            "CPET",
            "CPGH",
            "CPJT",
            "CPMS",
            "CPPL",
            "CPSA",
            "CPSD",
            "CPSF",
            "CPSG",
            "CPSN",
            "CPSP",
            "CPSS",
            "DANC",
            "DATA",
            "EALL",
            "ECON",
            "EDCP",
            "EDHD",
            "EDHI",
            "EDMS",
            "EDSP",
            "EDUC",
            "ENAE",
            "ENCE",
            "ENCH",
            "ENCO",
            "ENEB",
            "ENEE",
            "ENES",
            "ENFP",
            "ENGL",
            "ENMA",
            "ENME",
            "ENPM",
            "ENRE",
            "ENSE",
            "ENSP",
            "ENST",
            "ENTM",
            "ENTS",
            "EPIB",
            "FGSM",
            "FILM",
            "FIRE",
            "FMSC",
            "FREN",
            "GEMS",
            "GEOG",
            "GEOL",
            "GERM",
            "GREK",
            "GVPT",
            "HACS",
            "HDCC",
            "HEBR",
            "HEIP",
            "HESI",
            "HESP",
            "HHUM",
            "HISP",
            "HIST",
            "HLSA",
            "HLSC",
            "HLTH",
            "HNUH",
            "HONR",
            "IDEA",
            "IMDM",
            "IMMR",
            "INAG",
            "INFM",
            "INST",
            "ISRL",
            "ITAL",
            "JAPN",
            "JOUR",
            "JWST",
            "KNES",
            "KORA",
            "LARC",
            "LASC",
            "LATN",
            "LBSC",
            "LGBT",
            "LING",
            "MAIT",
            "MATH",
            "MEES",
            "MIEH",
            "MITH",
            "MLAW",
            "MLSC",
            "MSBB",
            "MSML",
            "MUED",
            "MUSC",
            "NACS",
            "NAVY",
            "NEUR",
            "NFSC",
            "NIAP",
            "NIAV",
            "PEER",
            "PERS",
            "PHIL",
            "PHPE",
            "PHSC",
            "PHYS",
            "PLCY",
            "PLSC",
            "PORT",
            "PSYC",
            "RDEV",
            "RELS",
            "RUSS",
            "SLAA",
            "SLLC",
            "SMLP",
            "SOCY",
            "SPAN",
            "SPHL",
            "STAT",
            "SURV",
            "TDPS",
            "THET",
            "TLPL",
            "TLTC",
            "UMEI",
            "UNIV",
            "URSP",
            "USLT",
            "VMSC",
            "WMST"
        )

    }

}