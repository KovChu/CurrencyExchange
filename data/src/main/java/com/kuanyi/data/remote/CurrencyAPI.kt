package com.kuanyi.data.remote

import com.kuanyi.data.BuildConfig
import com.kuanyi.data.model.CurrencyQuotes
import io.reactivex.Single
import retrofit2.http.GET

interface CurrencyAPI {

    @GET("/live?access_key=${BuildConfig.CurrencyLayerAPIKey}")
    fun getCurrencyQuote(): Single<CurrencyQuotes>
}