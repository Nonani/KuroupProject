package com.example.kuroupproject.activitys

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.kuroupproject.R
import com.example.kuroupproject.databinding.ActivityCreateTeamBinding
import com.example.kuroupproject.datas.ContestData
import com.example.kuroupproject.datas.TeamData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class CreateTeamActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityCreateTeamBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var contest_title:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        contest_title = intent.getStringExtra("contestTitle")!!
        firestore = FirebaseFirestore.getInstance()
        viewBinding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initSpinner()
        initActivity()
    }
    private fun insertData() {
        val teamData = TeamData(
            detail_url = "",
            team_max_number = viewBinding.spinnerNumber.selectedItem.toString().replace("명", "").toInt(),
            content = viewBinding.content.text.toString(),
            contest_title = contest_title,
            leader_uid = auth.currentUser!!.uid,
            title = viewBinding.recruitTitle.text.toString(),
            members_info = mutableListOf(  mapOf("state" to "accepted", "uid" to auth.currentUser!!.uid))
        )

        firestore.collection("posts")
            .document() // 이 곳에 특정 문서 ID를 지정하거나, 빈 괄호로 무작위 ID 할당
            .set(teamData.toMap())
            .addOnSuccessListener {
                println("Data successfully added!")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

    }

    private fun TeamData.toMap(): Map<String, Any> {
        return mapOf(
            "detail_url" to detail_url,
            "team_max_number" to team_max_number,
            "content" to content,
            "contest_title" to contest_title,
            "leader_uid" to leader_uid,
            "title" to title,
            "members_info" to members_info
        )
    }
    private fun initSpinner() {
        val locations = resources.getStringArray(R.array.spinnerLocation)
        val nums = resources.getStringArray(R.array.spinnerRecruitNum)

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, nums)

        viewBinding.spinnerLocation.adapter=adapter1
        viewBinding.spinnerNumber.adapter=adapter2
    }

    private fun initActivity(){
        viewBinding.createTeamButton.setOnClickListener {
            insertData()
            setResult(Activity.RESULT_OK)
            finish()
        }
        viewBinding.backCreate.setOnClickListener{
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}