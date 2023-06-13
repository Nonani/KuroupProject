package com.example.kuroupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.databinding.RowBookmarkBinding
import com.example.kuroupproject.databinding.RowContestBinding

class ContestAdapter (val items: ArrayList<ContestData>) :
    RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(holder: ContestAdapter.ViewHolder, view: View, data: ContestData, position: Int)
    }

    var itemClickListener: OnItemClickListener?= null

    inner class ViewHolder(val viewBinding: RowContestBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: ContestData, position: Int) {
            viewBinding.contestTitle.text = data.title
            viewBinding.contestSupport.text=data.sub_title
            viewBinding.dday.text=data.d_day


            if (data.clipped)
                viewBinding.imageStar.setImageResource(R.drawable.bookmark_selected)
            else
                viewBinding.imageStar.setImageResource(R.drawable.bookmark_unselected)


            viewBinding.imageStar.setOnClickListener {
                //즐겨찾기
                data.clipped = !data.clipped
                if (data.clipped)
                    viewBinding.imageStar.setImageResource(R.drawable.bookmark_selected)
                else
                    viewBinding.imageStar.setImageResource(R.drawable.bookmark_unselected)
                notifyItemChanged(adapterPosition)
            }

            viewBinding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[position], position)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowContestBinding.inflate(
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