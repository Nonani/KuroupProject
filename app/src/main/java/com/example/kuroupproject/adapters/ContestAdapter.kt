package com.example.kuroupproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.kuroupproject.datas.ContestData
import com.example.kuroupproject.R
import com.example.kuroupproject.databinding.RowContestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class ContestAdapter(val items: ArrayList<ContestData>) :
    RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: ContestData, position: Int)
    }

    lateinit var userId: String
    var itemClickListener: OnItemClickListener? = null
    lateinit var currentUser: FirebaseUser
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore



    inner class ViewHolder(val viewBinding: RowContestBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: ContestData, position: Int) {
            viewBinding.contestTitle.text = data.title
            viewBinding.contestSupport.text = data.sub_title
            viewBinding.dday.text = data.d_day



            if (data.clipped)
                viewBinding.imageStar.setImageResource(R.drawable.bookmark_selected)
            else
                viewBinding.imageStar.setImageResource(R.drawable.bookmark_unselected)


            viewBinding.imageStar.setOnClickListener {
                //즐겨찾기
                data.clipped = !data.clipped
                if (data.clipped) {
                    viewBinding.imageStar.setImageResource(R.drawable.bookmark_selected)
                    saveScrap(data)
                } else {
                    viewBinding.imageStar.setImageResource(R.drawable.bookmark_unselected)
                    deleteScrap(data)
                }
                notifyItemChanged(adapterPosition)
            }

            viewBinding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[position], position)
            }
        }

        private fun saveScrap(contest: ContestData) {

            auth = FirebaseAuth.getInstance()
            currentUser = auth.currentUser!!
            userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
            firestore = FirebaseFirestore.getInstance()

            val scrapData = hashMapOf(
                "d_day" to contest.d_day,
                "support" to contest.sub_title,
                "title" to contest.title
            )
            firestore.collection("users").document(userId).update("scrap", FieldValue.arrayUnion(scrapData))
                .addOnSuccessListener {
                    Log.d("Scrap", "Scrap added successfully")
                }
                .addOnFailureListener { exception ->
                    Log.w("Scrap", "Error adding scrap", exception)
                }
        }

        private fun deleteScrap(contest: ContestData) {

            auth = FirebaseAuth.getInstance()
            currentUser = auth.currentUser!!
            userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
            firestore = FirebaseFirestore.getInstance()

            val scrapData = hashMapOf(
                "d_day" to contest.d_day,
                "support" to contest.sub_title,
                "title" to contest.title
            )
            firestore.collection("users").document(userId).update("scrap", FieldValue.arrayRemove(scrapData))
                .addOnSuccessListener {
                    Log.d("Scrap", "Scrap deleted successfully")
                }
                .addOnFailureListener { exception ->
                    Log.w("Scrap", "Error deleting scrap", exception)
                }
        }

        private fun init_firebase(){
            auth = FirebaseAuth.getInstance()
            currentUser = auth.currentUser!!
            userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력
            firestore = FirebaseFirestore.getInstance()
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