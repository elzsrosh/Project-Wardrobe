package com.example.wardrobecomposer.data.model

data class Item(
    val name: String,
    val color: String,
    val style: String,
    val material: String,
    val imagePath: String? = null
)
