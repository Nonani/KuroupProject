package com.example.kuroupproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.datas.ContestData
import com.example.kuroupproject.activitys.DetailActivity
import com.example.kuroupproject.adapters.ContestAdapter
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface ApiService {
    @POST("list")
    suspend fun getContests(@Body requestBody: RequestBody): ArrayList<ContestData>
}

class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding

    var contests: ArrayList<ContestData> = ArrayList()

    lateinit var contests_adapter: ContestAdapter
    var isClickedBoom: Boolean = false
    var isClickedLate: Boolean = false
    lateinit var userId: String
    lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = viewBinding.progressBar
        if (!::contests_adapter.isInitialized) {
            init_data()
        } else {
            setupRecyclerView()
        }
    }



    private fun setupRecyclerView() {
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
                val intent = Intent(activity, DetailActivity::class.java).apply {
                    putExtra(
                        "contestData",
                        data
                    )
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

        }
        viewBinding.boomOrder.setOnClickListener {
            isClickedBoom = !isClickedBoom
            if (isClickedBoom)
                contests.sortBy {
                    it.d_day.split("-").lastOrNull()?.toIntOrNull() ?: it.d_day.split("+").lastOrNull()?.toIntOrNull()?.let { it + 100 }
                }
            else
                contests.sortByDescending {
                    it.d_day.split("-").lastOrNull()?.toIntOrNull() ?: it.d_day.split("+").lastOrNull()?.toIntOrNull()?.let { it + 100 }
                }
            viewBinding.contestList.adapter?.notifyDataSetChanged()
        }

        viewBinding.contestList.adapter = contests_adapter
    }


    private fun init_data() {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
        val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-kuroup-project.cloudfunctions.net/app/contest/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        lifecycleScope.launch {
            val requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                "{\"uid\": \"$userId\"}"
            )

            try {
                contests = apiService.getContests(requestBody)
                progressBar.visibility = View.GONE
                setupRecyclerView()
            } catch (e: Exception) {
                e.printStackTrace()
                progressBar.visibility = View.GONE
                // 에러 처리 필요한 경우 추가
            }
        }
    }
}