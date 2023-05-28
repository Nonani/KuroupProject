package com.example.kuroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kuroupproject.databinding.ActivityCheckTeamBinding
import com.example.kuroupproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        init()
        //리사이클러뷰 firebase로 구현(정보가 다 달라서 row.xml으로 하나씩 받아야될듯?)
    }

    private fun init() {
        viewBinding.createTeamButton.setOnClickListener {
            val intent = Intent(this, CheckTeamActivity::class.java)
            startActivity(intent)
        }

        viewBinding.backDetail.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}