package com.example.kuroupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.example.kuroupproject.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private lateinit var viewBinding : FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyPageBinding.inflate(layoutInflater)
        return viewBinding.root
    }

}