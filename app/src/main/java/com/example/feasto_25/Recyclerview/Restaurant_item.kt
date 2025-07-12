package com.example.feasto_25.Recyclerview

class Restaurant_item (

    val name: String,
    val imgurl: String,
    var rating: String,
    val docId: String = "",
    val dishes: List<String> = emptyList()
    //val dishes: List<String> = listOf()
)