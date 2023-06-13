package com.example.kuroupproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.datas.BookmarkData
import com.example.kuroupproject.databinding.RowBookmarkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class BookmarkAdapter (val items: ArrayList<BookmarkData>) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: BookmarkData)
    }
    var itemClickListener : OnItemClickListener?=null
    lateinit var userId: String
    lateinit var currentUser: FirebaseUser
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    inner class ViewHolder(val viewBinding: RowBookmarkBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: BookmarkData, position: Int) {
            viewBinding.bookmarkTitle.text = data.title
            viewBinding.bookmarkSupport.text=data.support
            viewBinding.btDday.text=data.d_day

            viewBinding.imageStar.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition])

                auth = FirebaseAuth.getInstance()
                currentUser = auth.currentUser!!
                userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
                firestore = FirebaseFirestore.getInstance()

                val scrapData = hashMapOf(
                    "d_day" to data.d_day,
                    "support" to data.support,
                    "title" to data.title
                )
                firestore.collection("users").document(userId).update("scrap", FieldValue.arrayRemove(scrapData))
                    .addOnSuccessListener {
                        Log.d("Scrap", "Scrap deleted successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Scrap", "Error deleting scrap", exception)
                    }
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