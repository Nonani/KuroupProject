package com.example.kuroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.databinding.RowBookmarkBinding
import com.example.kuroupproject.databinding.RowContestBinding
import com.example.kuroupproject.databinding.RowTeamBinding

class TeamAdapter(val items: ArrayList<TeamCheckData>) :
    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(
            holder: TeamAdapter.ViewHolder,
            view: View,
            data: TeamCheckData,
            position: Int
        )
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val viewBinding: RowTeamBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: TeamCheckData, position: Int) {
            viewBinding.mention.text = data.title
            viewBinding.place.text = "주 활동지역 : " + data.place

            viewBinding.nowStatus.text = "모집현황 : " + data.nowNumber.toString()+" / "+data.totalNumber.toString()

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}