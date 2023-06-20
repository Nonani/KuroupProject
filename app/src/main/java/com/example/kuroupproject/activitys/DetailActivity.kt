package com.example.kuroupproject.activitys

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.kuroupproject.datas.ContestData
import com.example.kuroupproject.datas.ContestDetailData
import com.example.kuroupproject.databinding.ActivityDetailBinding
import com.example.kuroupproject.fragments.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    var data: ContestData? = null
    var contest_title: String? = null
    private lateinit var checkTeamActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var userId: String
    lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    var contests: ArrayList<ContestData> = ArrayList()
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        contest_title = intent.getStringExtra("contest_title")

        if (contest_title != null) {
            bringData(contest_title!!) { loadedData ->
                init(loadedData)
            }
        } else {
            @Suppress("DEPRECATION")
            data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getSerializableExtra("contestData", ContestData::class.java)!!
            else
                intent.getSerializableExtra("contestData") as ContestData?
        }

        if (data != null) {
            init(data!!)
        } else {
//            Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
        setContentView(viewBinding.root)


        checkTeamActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // 데이터를 다시 로드합니다.
                    init(data!!)
                }
            }
    }

    private fun bringData(title: String, onDataLoaded: (ContestData) -> Unit) {
        viewBinding.progressBar.visibility = View.VISIBLE // 데이터 로드 전 ProgressBar 표시
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
                for (contest in contests) {
                    if (contest.title == title) {
                        data = contest
                        onDataLoaded(data!!)
                        viewBinding.progressBar.visibility = View.GONE
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 처리 필요한 경우 추가
            }
        }
    }

    private fun init(contestData: ContestData) {
        viewBinding.createTeamButton.setOnClickListener {
            val intent = Intent(this, CheckTeamActivity::class.java)
            intent.putExtra("contestTitle", data!!.title)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            checkTeamActivityResultLauncher.launch(intent)
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
        viewBinding.progressBar.visibility = View.VISIBLE // ProgressBar를 표시합니다.

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
                    viewBinding.progressBar.visibility = View.GONE // 데이터 가져온 후 ProgressBar 숨깁니다.
                    showDetailData(detailData)
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to load data", Toast.LENGTH_SHORT)
                        .show()
                    viewBinding.progressBar.visibility = View.GONE // 데이터 가져온 후 ProgressBar 숨깁니다.

                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                viewBinding.progressBar.visibility = View.GONE // 데이터 가져온 후 ProgressBar 숨깁니다.

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