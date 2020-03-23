package com.kuanyi.data.remote

import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyQuotes
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("list")
    suspend fun getCurrencyList(): Observable<CurrencyList>

    @GET("live")
    fun getCurrencyQuote(
        @Query("source") source: String
    ): Observable<CurrencyQuotes>
}