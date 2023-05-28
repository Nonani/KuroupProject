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
            startActivity(intent)
        }

        viewBinding.back.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init_data() {
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "서울", 3, 5))
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "부산", 1, 5))
        teams.add(TeamCheckData("같이 열심히 해볼 분 구해요~", "대구", 3, 6))
    }
}