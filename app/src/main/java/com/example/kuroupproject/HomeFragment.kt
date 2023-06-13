package com.example.kuroupproject

import android.content.Intent
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroupproject.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        init_data()
        return viewBinding.root
    }




    private fun setupRecyclerView(contests: ArrayList<ContestData>) {
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
//            contests.sortBy { it.date }
            viewBinding.contestList.adapter?.notifyDataSetChanged()
        }
        viewBinding.contestList.adapter = contests_adapter
    }


    private fun init_data() {


        val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-kuroup-project.cloudfunctions.net/app/contest/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val requestBody = RequestBody.create(
            MediaType.parse("applycation/json"),
            "{\"uid\": \"fKDS6vZokPhWhjwnNCrdRuOF2vJ3\"}"
        )

        lifecycleScope.launch {
            val requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                "{\"uid\": \"fKDS6vZokPhWhjwnNCrdRuOF2vJ3\"}"
            )

            try {
                contests = apiService.getContests(requestBody)
                setupRecyclerView(contests)
                contests_adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 처리 필요한 경우 추가
            }
        }


//        contests.add(ContestData("2023 버블탭 아이디어 공모전~~~~~~~", "고용노동부,한국산업인력공단", "www.ggg", 6, false, 10))

    }
}