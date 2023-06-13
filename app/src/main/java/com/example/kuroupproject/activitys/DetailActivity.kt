package com.example.kuroupproject.activitys

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.kuroupproject.datas.ContestData
import com.example.kuroupproject.datas.ContestDetailData
import com.example.kuroupproject.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.MediaType
import okhttp3.RequestBody


interface ApiService1 {
    @POST("detail")
    suspend fun getDetailData(@Body requestBody: RequestBody): Response<ContestDetailData>
}

class DetailActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        @Suppress("DEPRECATION")
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra("contestData", ContestData::class.java)
        else
            intent.getSerializableExtra("contestData") as ContestData
        setContentView(viewBinding.root)
        init(data!!)
    }

    private fun init(contestData: ContestData) {
        viewBinding.createTeamButton.setOnClickListener {
            val intent = Intent(this, CheckTeamActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        viewBinding.backDetail.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }

        val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-kuroup-project.cloudfunctions.net/app/contest/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService = retrofit.create(ApiService1::class.java)

        val detailUrl = contestData.detail_url
        loadDetailData(detailUrl)


    }

    private fun loadDetailData(detailUrl: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val requestBodyJson = "{\"url\": \"$detailUrl\"}"
            val requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBodyJson
            )

            val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://us-central1-kuroup-project.cloudfunctions.net/app/contest/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            val apiService = retrofit.create(ApiService1::class.java)

            try {
                val response: Response<ContestDetailData> = apiService.getDetailData(requestBody)

                if (response.isSuccessful) {
                    val detailData = response.body()
                    showDetailData(detailData)
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to load data", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun showDetailData(detailData: ContestDetailData?) {
        if (detailData != null) {
            // 데이터를 TextView에 추가
            Glide.with(this).load(detailData.imgUrl).into(viewBinding.contestImg)
            viewBinding.field1.text = detailData.field
            viewBinding.target1.text = detailData.targetAudience
            viewBinding.organizer1.text = detailData.organizer
            viewBinding.sponsor1.text = detailData.sponsor
            viewBinding.period1.text = detailData.applicationPeriod
            viewBinding.totalPrize1.text = detailData.totalPrize
            viewBinding.firstPrize1.text = detailData.firstPrize
            viewBinding.websiteUrl1.text = detailData.websiteURL
        }
    }
}