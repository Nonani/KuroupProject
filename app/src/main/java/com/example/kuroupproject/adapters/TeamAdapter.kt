package com.example.kuroupproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.datas.TeamCheckData
import com.example.kuroupproject.databinding.RowTeamBinding

class TeamAdapter(val items: ArrayList<TeamCheckData>) :

    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(
            holder: ViewHolder,
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
            viewBinding.place.text = "내용 : " + data.place
            viewBinding.nowStatus.text =
                "모집현황 : " + data.nowNumber.toString() + " / " + data.totalNumber.toString()
            viewBinding.supply.setOnClickListener {
                //즐겨찾기
                if (data.nowNumber < data.totalNumber) {
                    viewBinding.nowStatus.text =
                        "모집현황 : " + data.nowNumber++.toString() + " / " + data.totalNumber.toString()
                    Toast.makeText(itemView.context, "지원이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(itemView.context, "더 이상 지원할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                notifyItemChanged(adapterPosition)
            }
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