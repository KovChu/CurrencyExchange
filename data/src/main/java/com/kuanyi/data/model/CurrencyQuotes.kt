package com.kuanyi.data.model

import com.google.gson.JsonObject

data class CurrencyQuotes(
    override val success: Boolean,
    override val terms: String,
    override val privacy: String,
    val timestamp: Long,
    val source: String,
    val quotes: JsonObject
) : BaseResponse {

    fun convertCurrencyQuotes(source: String): List<CurrencyModel> {
        val currencyList = mutableListOf<CurrencyModel>()
        //use USD as default rate and convert it
        val currencyRate = quotes.get("USD${source}").asDouble
        for (abbr in quotes.keySet()) {
            val currency = abbr.removePrefix("USD")
            if (currency != source) {
                val rate = quotes.get(abbr).asDouble
                currencyList.add(CurrencyModel(currency, "", rate / currencyRate))
            }
        }
        return currencyList
    }
}
