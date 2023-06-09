package com.example.kuroupproject.datas

data class UserData(val uid:String,
                    val gender:String,
                    val language:String,
                    val location:String,
                    val mainPart:String,
                    val name:String,
                    val onelineExplain:String,
                    val phonenum:String,
                    val scrap:MutableList<Map<String,String>> ) {
}