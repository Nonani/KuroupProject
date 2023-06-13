package com.example.kuroupproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.databinding.RowUserBinding

class AcceptedUserDataAdapter(val items: ArrayList<UserData>) :
RecyclerView.Adapter<AcceptedUserDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: UserData)
    }

    var itemClickListener1: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.acceptBtn.setOnClickListener { itemClickListener1?.OnItemClick(items[adapterPosition]) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.applyTitle.text = items[position].name+" 님을 팀원으로 수락하시겠어요?"
        holder.binding.tvGender.text = items[position].gender
        holder.binding.tvLanguage.text = items[position].language
        holder.binding.tvLocation.text = items[position].location
        holder.binding.tvMainPart.text = items[position].mainPart
        holder.binding.tvOnelineExplain.text = items[position].onelineExplain
//        println(position)
    }
    fun getItemIndex(data: UserData): Int {
        return items.indexOf(data)
    }
}
