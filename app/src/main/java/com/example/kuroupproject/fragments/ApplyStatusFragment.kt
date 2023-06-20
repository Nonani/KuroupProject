package com.example.kuroupproject.fragments

import android.content.Intent
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
import com.example.kuroupproject.activitys.DetailActivity
import com.example.kuroupproject.adapters.ApplyTeamDataAdapter
import com.example.kuroupproject.adapters.AcceptedTeamDataAdapter
import com.example.kuroupproject.datas.TeamData
import com.example.kuroupproject.databinding.FragmentStatusApplyBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class ApplyStatusFragment : Fragment() {

    private lateinit var binding : FragmentStatusApplyBinding
    lateinit var data1: ArrayList<TeamData>
    lateinit var data2: ArrayList<TeamData>
    lateinit var adapter1: ApplyTeamDataAdapter
    lateinit var adapter2: AcceptedTeamDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusApplyBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun updateEmptyTV() {
        if(data1.isEmpty())
            binding.tvNone1.visibility=View.VISIBLE
        else
            binding.tvNone1.visibility=View.GONE
        if(data2.isEmpty())
            binding.tvNone2.visibility=View.VISIBLE
        else
            binding.tvNone2.visibility=View.GONE
    }

    private fun initRecyclerView() {
        binding.contestList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter1 = ApplyTeamDataAdapter(data1)

        updateEmptyTV()

        adapter1.itemClickListener1 = object: ApplyTeamDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: TeamData) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra("contest_title", data.contest_title)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        binding.contestList.adapter = adapter1

        binding.contestList2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter2 = AcceptedTeamDataAdapter(data2)

        adapter2.itemClickListener1 = object: AcceptedTeamDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: TeamData) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra("contest_title", data.contest_title)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
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
        val url = "https://us-central1-kuroup-project.cloudfunctions.net/app/team/supporting"
        val uid = auth.currentUser!!.uid

        makePostRequest(url, uid,
            onSuccess = { response ->
                // Handle the successful response here
                val gson = Gson()
                val resultMap: Map<String, Any> = gson.fromJson(response, object : TypeToken<Map<String, Any>>() {}.type)
                val value1: ArrayList<Map<String,String>> = resultMap["waiting_list"] as ArrayList<Map<String, String>>
                val value2: ArrayList<Map<String,String>> = resultMap["accepted_list"] as ArrayList<Map<String, String>>
                val list1 = value1.map { item ->
                    gson.fromJson(gson.toJson(item), TeamData::class.java)
                }
                val list2 = value2.map { item ->
                    gson.fromJson(gson.toJson(item), TeamData::class.java)
                }
                println(list1)
                data1 = ArrayList<TeamData>(list1)
                data2 = ArrayList<TeamData>(list2)
                initRecyclerView()

            },
            onError = { errorMessage ->
                // Handle the error case here
                println("Error: $errorMessage")
            }
        )
    }
}