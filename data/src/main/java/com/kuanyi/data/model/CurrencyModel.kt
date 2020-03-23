package com.kuanyi.data.model

data class CurrencyModel(
    val abbr: String,
    val name: String,
    val rate: Double = 0.0
)