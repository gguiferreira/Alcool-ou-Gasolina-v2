package com.example.gasolinaoualcool.data

data class FuelStation(
    val id: String,
    val name: String,
    val alcoholPrice: Float,
    val gasPrice: Float,
    val date: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
