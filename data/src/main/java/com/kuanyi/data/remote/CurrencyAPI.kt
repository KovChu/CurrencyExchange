package com.kuanyi.data.remote

import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyQuotes
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/list")
    fun getCurrencyList(
        @Query("access_key") accessKey: String
    ): Observable<CurrencyList>

    @GET("/live")
    fun getCurrencyQuote(
        @Query("access_key") accessKey: String,
        @Query("source") source: String
    ): Observable<CurrencyQuotes>
}