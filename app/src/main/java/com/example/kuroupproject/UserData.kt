package com.example.kuroupproject

data class UserData(val gender:String,
                    val language:String,
                    val location:String,
                    val mainPart:String,
                    val name:String,
                    val onelineExplain:String,
                    val phonenum:String,
                    val scrap:MutableList<Map<String,String>>, ) {
}