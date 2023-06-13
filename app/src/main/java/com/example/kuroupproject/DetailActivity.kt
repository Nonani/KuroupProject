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
    }

    private fun init() {
        viewBinding.createTeamButton.setOnClickListener {
            val intent = Intent(this, CheckTeamActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        viewBinding.backDetail.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }
    }
}