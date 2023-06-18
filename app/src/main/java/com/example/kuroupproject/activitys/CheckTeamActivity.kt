package com.example.kuroupproject.activitys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.datas.TeamCheckData
import com.example.kuroupproject.adapters.TeamAdapter
import com.example.kuroupproject.databinding.ActivityCheckTeamBinding
import com.google.firebase.firestore.FirebaseFirestore

class CheckTeamActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCheckTeamBinding
    var teams: ArrayList<TeamCheckData> = ArrayList()
    lateinit var teams_adapter: TeamAdapter
    lateinit var db : FirebaseFirestore
    lateinit var title : String
    lateinit var adapter: TeamAdapter
    private lateinit var checkTeamActivityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCheckTeamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val intent= intent
        title = intent.getStringExtra("contestTitle")!!
        init_data()
        init()
        checkTeamActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 데이터를 다시 로드합니다.
                init_data()
                init()
            }
        }
    }

    private fun init() {
        viewBinding.teamList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teams_adapter = TeamAdapter(teams)
        viewBinding.teamList.adapter = teams_adapter

        viewBinding.createTeamButton.setOnClickListener {
            val intent = Intent(this, CreateTeamActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        viewBinding.backCheck.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(0, 0)
        }

    }
    // 지원하기 눌렀을때 Activity 구현해야됨

    private fun init_data() {
        db = FirebaseFirestore.getInstance()
        val postsCollection = db.collection("posts")

        postsCollection.whereEqualTo("contest_title",title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val content = document.getString("content")
                    val title = document.getString("title")
                    val teamMaxNumber = document.getLong("team_max_number")
                    val membersInfo = document.get("members_info") as List<*>
                    val membersInfoSize = membersInfo.filter { memberInfo ->
                        val memberState = (memberInfo as Map<String, String>).get("state") as String
                        memberState == "accepted"
                    }.size

                    teams.add(TeamCheckData(documentId!!, title!!,content!!,membersInfoSize,teamMaxNumber!!))
                }
                teams_adapter.notifyDataSetChanged()
            }
    }
}