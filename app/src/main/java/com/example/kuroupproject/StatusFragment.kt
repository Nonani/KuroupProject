package com.example.kuroupproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.kuroupproject.databinding.FragmentMyPageBinding
import com.example.kuroupproject.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {
    private lateinit var viewBinding : FragmentStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentStatusBinding.inflate(layoutInflater)
        setFragment()
        return viewBinding.root
    }

    private fun setFragment() {

        /**
         * 가장 처음 지정될 아이콘 및 프레그먼트 설정
         */
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_status,CreatedStatusFragment())
            .commitAllowingStateLoss()

        /**
         * 각 아이콘 클릭으로 인한 프레그먼트 변경 이벤트
         */

        viewBinding.radioGroup.setOnCheckedChangeListener { group, id ->
            Log.d("test",id.toString())
            when(id){
                2131231293->{
                    Log.d("test","id 0")
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_status,ApplyStatusFragment())
                        .commitAllowingStateLoss()
                }
                2131231294->{
                    Log.d("test","id 1")
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_status,CreatedStatusFragment())
                        .commitAllowingStateLoss()
                }
                else-> {

                }
            }
        }
    }
}