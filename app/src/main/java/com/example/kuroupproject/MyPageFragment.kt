package com.example.kuroupproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.example.kuroupproject.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private lateinit var viewBinding : FragmentMyPageBinding
    var bookmarks:ArrayList<BookmarkData> = ArrayList()
    lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyPageBinding.inflate(layoutInflater)
        initRecyclerView()
        return viewBinding.root
    }

    private fun initRecyclerView() {
        //데이터 초기화
        bookmarks.add(BookmarkData("2023 버블탭 아이디어 공모전","고용노동부,한국산업인력공단",3))

        viewBinding.recyclerviewBookmark.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter= BookmarkAdapter(bookmarks)
        viewBinding.recyclerviewBookmark.adapter=adapter

        //찜추가 정보 들어오면 데이터 추가 작업
    }

}