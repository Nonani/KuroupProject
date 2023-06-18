package com.example.kuroupproject.activitys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.kuroupproject.R
import com.example.kuroupproject.databinding.ActivityCreateTeamBinding

class CreateTeamActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityCreateTeamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initSpinner()
        initActivity()
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
        viewBinding.backCreate.setOnClickListener{
            setResult(Activity.RESULT_OK)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}