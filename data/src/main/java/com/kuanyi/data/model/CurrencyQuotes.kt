package com.kuanyi.data.model

data class CurrencyQuotes(
    override val success: Boolean,
    override val terms: String,
    override val privacy: String,
    val timestamp : Long,
    val source : String,
    val quotes: String
) : BaseResponse