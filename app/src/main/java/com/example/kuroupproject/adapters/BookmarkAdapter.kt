package com.example.kuroupproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.datas.BookmarkData
import com.example.kuroupproject.databinding.RowBookmarkBinding

class BookmarkAdapter (val items: ArrayList<BookmarkData>) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: BookmarkData)
    }
    var itemClickListener : OnItemClickListener?=null

    inner class ViewHolder(val viewBinding: RowBookmarkBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: BookmarkData, position: Int) {
            viewBinding.bookmarkTitle.text = data.title
            viewBinding.bookmarkSupport.text=data.support
            viewBinding.btDday.text="D-"+data.dday.toString()

            viewBinding.imageStar.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition])
                //해당 row 삭제
                items.removeAt(position)
                notifyItemChanged(adapterPosition)
            }

            viewBinding.root.setOnClickListener {
                //해당 공모전 정보로 이동
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =RowBookmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }
}