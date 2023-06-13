package com.example.kuroupproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.databinding.RowApplyTeamBinding
import com.example.kuroupproject.datas.TeamData

class ApplyTeamDataAdapter(val items: ArrayList<TeamData>) :
RecyclerView.Adapter<ApplyTeamDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: TeamData)
    }

    var itemClickListener1: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowApplyTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.detailBtn.setOnClickListener { itemClickListener1?.OnItemClick(items[adapterPosition]) }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = RowApplyTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contestTitle.text = items[position].contest_title
        holder.binding.teamTitle.text = items[position].title

//        println(position)
    }
    fun getItemIndex(data: TeamData): Int {
        return items.indexOf(data)
    }
}
