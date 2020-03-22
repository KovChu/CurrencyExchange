package com.kuanyi.data.remote

import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyQuotes
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("list")
    suspend fun getCurrencyList(): CurrencyList

    @GET("live")
    fun getCurrencyQuote(
        @Query("source") source: String
    ): Deferred<Response<CurrencyQuotes>>
}