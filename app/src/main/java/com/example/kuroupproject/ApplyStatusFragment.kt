package com.example.kuroupproject

import android.os.Bundle
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
import com.example.kuroupproject.databinding.FragmentStatusApplyBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class ApplyStatusFragment : Fragment() {

    private lateinit var binding : FragmentStatusApplyBinding
    var data1: ArrayList<UserData> = ArrayList()
    lateinit var adapter: ApplyUserDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusApplyBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun initRecyclerView(list: List<TeamData>) {
        binding.contestList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ApplyUserDataAdapter(data1)

        adapter.itemClickListener1 = object:ApplyUserDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: UserData) {

            }
        }
        adapter.itemClickListener2 = object:ApplyUserDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: UserData) {

            }
        }
        binding.contestList.adapter = adapter

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
        val url = "https://us-central1-kuroup-project.cloudfunctions.net/app/team/supporting"
        val uid = auth.currentUser!!.uid

        makePostRequest(url, uid,
            onSuccess = { response ->
                // Handle the successful response here
                val gson = Gson()
                val resultMap: Map<String, Any> = gson.fromJson(response, object : TypeToken<Map<String, Any>>() {}.type)
                val value1: ArrayList<Map<String,String>> = resultMap["accepted_list"] as ArrayList<Map<String, String>>

                val list = value1.map { item ->
                    TeamData(
                        detail_url = item["detail_url"] as String,
                        team_max_number = (item["team_max_number"] as Double).toInt(),
                        content = item["content"] as String,
                        contest_title = item["contest_title"] as String,
                        leader_uid = item["leader_uid"] as String,
                        title = item["title"] as String,
                        members_info = (item["members_info"] as List<Map<String, String>>).toMutableList()
                    )
                }
                initRecyclerView(list)

                println("Response: $list")
            },
            onError = { errorMessage ->
                // Handle the error case here
                println("Error: $errorMessage")
            }
        )
    }
}