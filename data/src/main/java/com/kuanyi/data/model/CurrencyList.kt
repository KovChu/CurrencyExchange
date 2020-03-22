package com.kuanyi.data.model

data class CurrencyList(
    override val success: Boolean,
    override val terms: String,
    override val privacy: String,
    val currencies: String
) : BaseResponse