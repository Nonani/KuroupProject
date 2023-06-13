package com.example.kuroupproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.example.kuroupproject.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class MyPageFragment : Fragment() {
    private lateinit var viewBinding : FragmentMyPageBinding
    var bookmarks:ArrayList<BookmarkData> = ArrayList()
    lateinit var adapter: BookmarkAdapter
    lateinit var firestore: FirebaseFirestore
    lateinit var userId : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyPageBinding.inflate(layoutInflater)
        initRecyclerView()
        initMyData()
        return viewBinding.root
    }

    private fun initMyData() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
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
)
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
        //데이터 초기화
        bookmarks.add(BookmarkData("2023 버블탭 아이디어 공모전","고용노동부,한국산업인력공단",3))

        viewBinding.recyclerviewBookmark.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter= BookmarkAdapter(bookmarks)
        viewBinding.recyclerviewBookmark.adapter=adapter

        //찜추가 정보 들어오면 데이터 추가 작업
//        userId = "사용자의 고유 식별자"
//        firestore.collection("users").document(userId)
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                val user = documentSnapshot.data
//                user?.let {
//                    // 다른 필드들 가져오는 코드 생략
//                    val favorites = user["favorites"] as? ArrayList<String>
//                    for(favorite in favorites!!){
//                        bookmarks.add(BookmarkData("2023 버블탭 아이디어 공모전",
//                            "고용노동부,한국산업인력공단",
//                            3))
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                // 사용자 데이터를 불러오는 중에 오류가 발생했습니다.
//            }
    }

}