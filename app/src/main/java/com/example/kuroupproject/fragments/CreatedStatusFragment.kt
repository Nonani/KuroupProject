package com.example.kuroupproject.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.kuroupproject.adapters.AcceptedUserDataAdapter
import com.example.kuroupproject.datas.UserData
import com.example.kuroupproject.adapters.ApplyUserDataAdapter
import com.example.kuroupproject.databinding.FragmentStatusCreatedBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class CreatedStatusFragment : Fragment() {
    private lateinit var binding : FragmentStatusCreatedBinding
    lateinit var data1: ArrayList<UserData>
    lateinit var data2: ArrayList<UserData>
    lateinit var adapter1: ApplyUserDataAdapter
    lateinit var adapter2: AcceptedUserDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusCreatedBinding.inflate(layoutInflater)

        init()
        return binding.root
    }
    private fun initRecyclerView() {
        binding.contestList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.contestList2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter1 = ApplyUserDataAdapter(data1)

        adapter1.itemClickListener1 = object: ApplyUserDataAdapter.OnItemClickListener{
        //수락버튼 클릭 이벤트 추가
            override fun OnItemClick(data: UserData) {
            var auth = Firebase.auth
            val db = FirebaseFirestore.getInstance()
            val postsCollectionRef = db.collection("posts")
            postsCollectionRef.whereEqualTo("leader_uid", auth.currentUser!!.uid).get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val membersInfo = document["members_info"] as List<Map<String, String>>?

                        val mutableMembersInfo = membersInfo?.map { it.toMutableMap() }?.toMutableList()
                        mutableMembersInfo?.forEach { member ->
                            if (member["uid"] == data.uid && member["state"] == "waiting") {
                                member["state"] = "accepted"
                            }
                        }
                        document.reference.update("members_info", mutableMembersInfo)
                        init()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents: $exception")
                }
            }
        }
        //거절버튼 클릭 이벤트 추가
        adapter1.itemClickListener2 = object: ApplyUserDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: UserData) {
                var auth = Firebase.auth
                val db = FirebaseFirestore.getInstance()
                val postsCollectionRef = db.collection("posts")
                postsCollectionRef.whereEqualTo("leader_uid", auth.currentUser!!.uid).get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val membersInfo = document["members_info"] as List<Map<String, String>>?

                            val mutableMembersInfo = membersInfo?.map { it.toMutableMap() }?.toMutableList()
                            mutableMembersInfo?.forEach { member ->
                                if (member["uid"] == data.uid && member["state"] == "waiting") {
                                    member["state"] = "rejected"
                                }
                            }
                            document.reference.update("members_info", mutableMembersInfo)
                            init()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error getting documents: $exception")
                    }
            }
        }
        binding.contestList.adapter = adapter1


        adapter2 = AcceptedUserDataAdapter(data2)

        binding.contestList2.adapter = adapter2

    }


    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
    fun makePostRequest(url: String, uid: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        // Create the JSON object with the uid
        val jsonObject = JSONObject().apply {
            put("uid", uid)
        }

        // Create the JsonObjectRequest
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                onSuccess(response.toString())
            },
            Response.ErrorListener { error ->
                onError(error.message ?: "Unknown error occurred")
            })

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }

    private fun init() {
        var auth = Firebase.auth
        val url = "https://us-central1-kuroup-project.cloudfunctions.net/app/team/created"
        val uid = auth.currentUser!!.uid

        makePostRequest(url, uid,
            onSuccess = { response ->
                // Handle the successful response here
                val gson = Gson()
                val resultMap: Map<String, Any> = gson.fromJson(response, object : TypeToken<Map<String, Any>>() {}.type)
                val value1: ArrayList<Map<String,String>> = resultMap["waiting_users"] as ArrayList<Map<String, String>>
                val value2: ArrayList<Map<String,String>> = resultMap["accepted_users"] as ArrayList<Map<String, String>>
                val list1 = value1.map { item ->
                    gson.fromJson(gson.toJson(item), UserData::class.java)
                }
                val list2 = value2.map { item ->
                    gson.fromJson(gson.toJson(item), UserData::class.java)
                }
                println(list1)
                data1 = ArrayList<UserData>(list1)
                data2 = ArrayList<UserData>(list2)
                initRecyclerView()

            },
            onError = { errorMessage ->
                // Handle the error case here
                println("Error: $errorMessage")
            }
        )
    }

}