package com.example.kuroupproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroupproject.datas.TeamCheckData
import com.example.kuroupproject.databinding.RowTeamBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class TeamAdapter(val items: ArrayList<TeamCheckData>) :


    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
    lateinit var db: FirebaseFirestore
    lateinit var userId: String
    lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser


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

        suspend fun userExistsInMembersInfo(postId: String, userId: String): Boolean =
            suspendCancellableCoroutine<Boolean> { continuation ->
                db.collection("posts").document(postId).get()
                    .addOnSuccessListener { document ->
                        var userExists = false
                        if (document != null) {
                            val membersInfo = document.get("members_info") as List<Map<String, Any>>
                            for (memberInfo in membersInfo) {
                                if (memberInfo["uid"] == userId) {
                                    userExists = true
                                    break
                                }
                            }
                        }
                        continuation.resume(userExists) {
                            if (continuation.isCancelled) {
                                it.printStackTrace()
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }//이미 지원했는지 확인하는 함수

        fun bind(data: TeamCheckData, position: Int) {
            viewBinding.mention.text = data.title
            viewBinding.place.text = "내용 : " + data.place
            viewBinding.nowStatus.text =
                "모집현황 : " + data.nowNumber.toString() + " / " + data.totalNumber.toString()
            viewBinding.supply.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    auth = FirebaseAuth.getInstance()
                    db = FirebaseFirestore.getInstance()
                    currentUser = auth.currentUser!!
                    userId = currentUser?.uid!! // 사용자의 고유 식별자를 입력

                    val userAlreadyExists =
                        userExistsInMembersInfo(data.id, userId)
                    if (userAlreadyExists) {
                        Toast.makeText(itemView.context, "이미 지원한 게시물입니다.", Toast.LENGTH_SHORT)
                            .show()
                        return@launch
                    }

                    val scrapData = hashMapOf(
                        "state" to "waiting",
                        "uid" to userId
                    )
                    val postsCollection = db.collection("posts")
                    //즐겨찾기
                    if (data.nowNumber < data.totalNumber) {
                        postsCollection.document(data.id)
                            .update("members_info", FieldValue.arrayUnion(scrapData))
                            .addOnSuccessListener {
                                Log.d("Scrap", "Scrap added successfully")
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Scrap", "Error adding scrap", exception)
                            }
                        viewBinding.nowStatus.text =
                            "모집현황 : " + data.nowNumber.toString() + " / " + data.totalNumber.toString()
                        Toast.makeText(itemView.context, "지원이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(itemView.context, "더 이상 지원할 수 없습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    notifyItemChanged(adapterPosition)
                }
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