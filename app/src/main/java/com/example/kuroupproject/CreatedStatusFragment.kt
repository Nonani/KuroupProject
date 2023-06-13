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
import com.example.kuroupproject.databinding.FragmentStatusCreatedBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class CreatedStatusFragment : Fragment() {
    private lateinit var binding : FragmentStatusCreatedBinding
    lateinit var data1: ArrayList<UserData>
    lateinit var data2: ArrayList<UserData>
    lateinit var adapter: ApplyUserDataAdapter
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

                // 결과를 ArrayList<TeamData> 타입으로 변환
                data1 = ArrayList<UserData>(list1)
                initRecyclerView()

                println("Response: $data1")
            },
            onError = { errorMessage ->
                // Handle the error case here
                println("Error: $errorMessage")
            }
        )
    }

}