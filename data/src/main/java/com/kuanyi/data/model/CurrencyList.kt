package com.kuanyi.data.model

import com.google.gson.JsonObject

data class CurrencyList(
    override val success: Boolean,
    override val terms: String,
    override val privacy: String,
    val currencies: JsonObject
) : BaseResponse {

    fun toCurrencyModelList() : List<CurrencyModel> {
        val currencyList = mutableListOf<CurrencyModel>()
        for (abbr in currencies.keySet()) {
            currencyList.add(CurrencyModel(abbr, currencies.get(abbr).asString))
        }
            return currencyList
    }
}