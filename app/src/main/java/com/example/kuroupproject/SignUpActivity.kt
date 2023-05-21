package com.example.kuroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.kuroupproject.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initSpinner()
    }

    private fun initSpinner() {
        val locations = resources.getStringArray(R.array.spinnerLocation)
        val languages = resources.getStringArray(R.array.spinnerLanguage)
        val parts = resources.getStringArray(R.array.spinnerPart)

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, parts)

        viewBinding.spinnerLocation.adapter=adapter1
        viewBinding.spinnerLanguage.adapter=adapter2
        viewBinding.spinnerMainPart.adapter=adapter3
    }
}