package com.example.kuroupproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding
    var contests: ArrayList<ContestData> = ArrayList()
    lateinit var contests_adapter: ContestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        init()
        return viewBinding.root
    }


    private fun init() {
        //데이터 인입
        init_data()

        //리사이클러뷰 어뎁터 생성
        viewBinding.contestList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        contests_adapter = ContestAdapter(contests)
        contests_adapter.itemClickListener = object : ContestAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: ContestAdapter.ViewHolder,
                view: View,
                data: ContestData,
                position: Int
            ) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

        }
        viewBinding.boomOrder.setOnClickListener {
            contests.sortBy { it.date }
            viewBinding.contestList.adapter?.notifyDataSetChanged()
        }
        viewBinding.contestList.adapter = contests_adapter

    }

    private fun init_data() {
        contests.add(ContestData("2023 버블탭 아이디어 공모전~~~~~~~", "고용노동부,한국산업인력공단", 6, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 4, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 7, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 8, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 11, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 3, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 2, false))
        contests.add(ContestData("2023 버블탭 아이디어 공모전", "고용노동부,한국산업인력공단", 10, false))
    }
}