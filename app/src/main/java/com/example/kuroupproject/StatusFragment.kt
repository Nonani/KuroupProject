package com.example.kuroupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.example.kuroupproject.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {
    private lateinit var viewBinding : FragmentStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentStatusBinding.inflate(layoutInflater)

        init()
        return viewBinding.root
    }

    private fun init() {

    }

}