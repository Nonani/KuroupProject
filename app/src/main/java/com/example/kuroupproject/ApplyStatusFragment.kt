package com.example.kuroupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kuroupproject.databinding.FragmentStatusApplyBinding



class ApplyStatusFragment : Fragment() {

    private lateinit var viewBinding : FragmentStatusApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentStatusApplyBinding.inflate(layoutInflater)
        init()
        return viewBinding.root
    }

    private fun init() {

    }
}