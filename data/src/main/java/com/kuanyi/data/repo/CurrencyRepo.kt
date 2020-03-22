package com.kuanyi.data.repo

import com.kuanyi.data.model.CurrencyModel
import com.kuanyi.data.model.CurrencyQuotes
import com.kuanyi.data.remote.CurrencyAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


interface CurrencyRepo {
    fun fetchCurrencyList(): Flow<CurrencyModel>>
    fun fetchCurrencyQuotes(): Flow<Response<CurrencyQuotes>>
}

class CurrencyRepoImpl(private val api: CurrencyAPI) : CurrencyRepo {

    override fun fetchCurrencyList(): Flow<CurrencyModel> {
        flow {
           api.getCurrencyList()
        }

    }

    override fun fetchCurrencyQuotes(): Flow<Response<CurrencyQuotes>> {

    }

}