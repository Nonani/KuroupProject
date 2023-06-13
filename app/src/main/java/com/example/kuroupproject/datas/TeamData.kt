package com.example.kuroupproject.datas

data class TeamData(val detail_url:String,
                    val team_max_number:Int,
                    val content:String,
                    val contest_title:String,
                    val leader_uid:String,
                    val title:String,
                    val members_info:MutableList<Map<String, String>>
) {
}