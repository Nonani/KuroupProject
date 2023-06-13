package com.example.kuroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.databinding.ActivityCheckTeamBinding
import com.example.kuroupproject.databinding.FragmentHomeBinding

class CheckTeamActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCheckTeamBinding
    var teams: ArrayList<TeamCheckData> = ArrayList()
    lateinit var teams_adapter: TeamAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCheckTeamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        init()
    }

    private fun init() {
        init_data()
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
            val intent = Intent(this, DetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

    }
    // 지원하기 눌렀을때 Activity 구현해야됨

    private fun init_data() {
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "서울", 3, 5))
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "부산", 1, 5))
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "대구", 3, 6))
    }
}