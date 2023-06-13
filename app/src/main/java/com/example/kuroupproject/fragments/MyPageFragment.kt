package com.example.kuroupproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.activitys.DetailActivity
import com.example.kuroupproject.datas.BookmarkData
import com.example.kuroupproject.adapters.BookmarkAdapter
import com.example.kuroupproject.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageFragment : Fragment() {
    private lateinit var viewBinding : FragmentMyPageBinding
    var bookmarks:ArrayList<BookmarkData> = ArrayList()
    lateinit var adapter: BookmarkAdapter
    lateinit var firestore: FirebaseFirestore
    lateinit var userId : String
    lateinit var auth : FirebaseAuth
    lateinit var currentUser : FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyPageBinding.inflate(layoutInflater)
        initMyData()
        initRecyclerView()
        return viewBinding.root
    }

    private fun initMyData() {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(userId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.data
                user?.let {
                    val name = user["name"] as String
                    val gender = user["gender"] as String
                    val location = user["location"] as String
                    val language = user["language"] as String
                    val mainPart = user["mainPart"] as String
                    val phonenum = user["phonenum"] as String
                    val onelineExplain = user["onelineExplain"] as String

                    viewBinding.myName.text = name
                    viewBinding.myGenderInfo.text = gender
                    viewBinding.myLocationInfo.text = location
                    viewBinding.myProgLangInfo.text = language
                    viewBinding.myPartInfo.text = mainPart
                    viewBinding.myPhoneNumInfo.text = phonenum
                    viewBinding.myOnelineInfo.text = onelineExplain
                }
            }
            .addOnFailureListener { e ->
                // 사용자 데이터를 불러오는 중에 오류가 발생
                Log.d("error","사용자 데이터를 불러오는 중에 오류가 발생하였습니다.")
            }
    }

    private fun initRecyclerView() {
        //찜추가 정보 들어오면 데이터 추가 작업
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.data
                user?.let {
                    val scrapDataList = user["scrap"] as? ArrayList<HashMap<String,String>>
                    if(scrapDataList!=null&&scrapDataList.isNotEmpty()){
                        for (scrapData in scrapDataList) {
                            val title = scrapData["title"] ?: ""
                            val dDay = scrapData["d_day"] ?: ""
                            val support = scrapData["support"] ?: ""

                            val bookmarkData = BookmarkData(title, support, dDay)

                            bookmarks.add(bookmarkData)
                        }
                    }
                }
                // 데이터를 추가한 후 RecyclerView 어댑터 설정 및 업데이트
                viewBinding.recyclerviewBookmark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = BookmarkAdapter(bookmarks)

                adapter.itemClickListener = object : BookmarkAdapter.OnItemClickListener {
                    override fun OnItemClick(data: BookmarkData) {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                // 해당 아이템 데이터를 삭제
                                firestore.collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        val user = documentSnapshot.data
                                        user?.let {
                                            val scrapDataList = user["scrap"] as? ArrayList<HashMap<String, String>>
                                            if (scrapDataList != null) {
                                                scrapDataList.removeIf { scrapData ->
                                                    scrapData["title"] == data.title &&
                                                            scrapData["d_day"] == data.d_day.toString() &&
                                                            scrapData["support"] == data.support
                                                }
                                                // 수정된 데이터를 Firestore에 업데이트
                                                firestore.collection("users").document(userId)
                                                    .update("scrap", scrapDataList)
                                                    .addOnSuccessListener {
                                                        // 데이터 업데이트 성공
                                                    }
                                                    .addOnFailureListener { e ->
                                                        // 데이터 업데이트 실패
                                                    }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // 사용자 데이터를 불러오는 중에 오류가 발생했습니다.
                                        // TODO: 오류 처리
                                    }

                            } catch (e: Exception) {
                                // TODO: 오류 처리
                            }
                        }
                    }
                }
                viewBinding.recyclerviewBookmark.adapter = adapter
            }

    }

}