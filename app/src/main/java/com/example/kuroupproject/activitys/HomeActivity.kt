package com.example.kuroupproject.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kuroupproject.fragments.HomeFragment
import com.example.kuroupproject.fragments.MyPageFragment
import com.example.kuroupproject.R
import com.example.kuroupproject.fragments.StatusFragment
import com.example.kuroupproject.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {

        /**
         * 가장 처음 지정될 아이콘 및 프레그먼트 설정
         */

        viewBinding.btmNav.selectedItemId= R.id.bnb_home
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame, HomeFragment())
            .commitAllowingStateLoss()

        /**
         * 각 아이콘 클릭으로 인한 프레그먼트 변경 이벤트
         */

        viewBinding.btmNav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.bnb_kuroup_status ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, StatusFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.bnb_home ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, HomeFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.bnb_mypage ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, MyPageFragment())
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
        }
    }

}