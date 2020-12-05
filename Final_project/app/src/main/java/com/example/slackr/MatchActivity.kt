package com.example.slackr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_create_group.*

class MatchActivity : AppCompatActivity() {

    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseGroups: DatabaseReference
    private var mDatabaseReference: DatabaseReference? = null
    internal lateinit var matches: MutableList<StudyGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val mSubject:Spinner = findViewById(R.id.subject)
        val mCode:EditText = findViewById(R.id.code)
        val mFindMatch:Button = findViewById(R.id.findButton)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseGroups = mDatabase!!.reference.child("Groups")
        mDatabaseReference = mDatabase!!.reference.child("Users")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MatchActivity,
            android.R.layout.simple_spinner_item, subjectArr
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSubject.adapter = adapter

        mFindMatch!!.setOnClickListener {

            var subject= mSubject.selectedItem.toString()
            var code = mCode.text.toString()

            val uid = mAuth!!.currentUser?.uid
            matches= ArrayList()
            uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object :
                ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    val learningStyle = snapshot.child("style_of_learning").value as String
                    val key: String =subject+code+learningStyle

                    Log.i(TAG, key)
                     databaseGroups.orderByChild("searchKey").equalTo(key)
                        .addListenerForSingleValueEvent((object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                matches.clear()
                                var group: StudyGroup? = null
                                var count=0
                                for (postSnapshot in dataSnapshot.children) {
                                    try {
                                        group = postSnapshot.getValue(StudyGroup::class.java)

                                    } catch (e: Exception) {

                                    } finally {

                                        if(!group!!.createdBy.equals(mAuth!!.currentUser!!.uid)){
                                            matches.add(group!!)

                                        } else{
                                            count++
                                        }

                                    }
                                }
                                if (matches.isEmpty() && count ==0){
                                    Log.i(TAG, "here 3")
                                    val newFragment = PromptCreateGroupFragment.newInstance(subject, code)
                                    newFragment.show(supportFragmentManager, "prompt_create_group")
                                } else if(matches.isEmpty() && count >=1){
                                    Toast.makeText(applicationContext, "There is currently no group other than the one your group matching the search. Please Try again later", Toast.LENGTH_LONG).show()
                                } else{
                                    Log.i(TAG, "here 4")
                                    /**
                                     *  display matches
                                     * **/
                                    val intent= Intent(this@MatchActivity, GroupListActivity::class.java)
                                    Log.i(TAG, matches.toString())
                                    intent.putExtra("matching_groups", matches as ArrayList<StudyGroup>)
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