package com.kuanyi.data.remote

import com.kuanyi.data.BuildConfig
import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyQuotes
import io.reactivex.Observable
import retrofit2.http.GET

interface CurrencyAPI {

    @GET("/list?access_key=${BuildConfig.CurrencyLayerAPIKey}")
    fun getCurrencyList(): Observable<CurrencyList>

    @GET("/live?access_key=${BuildConfig.CurrencyLayerAPIKey}")
    fun getCurrencyQuote(): Observable<CurrencyQuotes>
}