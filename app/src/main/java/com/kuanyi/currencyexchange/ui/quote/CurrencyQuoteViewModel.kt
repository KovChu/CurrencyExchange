package com.kuanyi.currencyexchange.ui.quote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.data.model.CurrencyModel
import com.kuanyi.data.model.CurrencyQuotes
import com.kuanyi.data.remote.CurrencyAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyQuoteViewModel(private val source: String) : BaseLoadingViewModel() {

    @Inject
    lateinit var currencyAPI: CurrencyAPI

    private lateinit var subscription: Disposable

    val quoteList: MutableLiveData<List<CurrencyModel>> = MutableLiveData()

    val currencyList: MutableLiveData<List<String>> = MutableLiveData()

    init {
        loadCurrencyList()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadCurrencyList() {
        subscription = currencyAPI.getCurrencyQuote()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLoadingStarted() }
            .doOnTerminate { onLoadingFinished() }
            .map { currencyQuote: CurrencyQuotes ->
                return@map currencyQuote.convertCurrencyQuotes(source)
            }.subscribe(
                { data -> onLoadingSuccess(data) },
                {
                    onLoadingError()
                })
    }


    private fun onLoadingSuccess(data: List<CurrencyModel>) {
        super.onLoadingSuccess()
        Log.i("data", data.toString())
        quoteList.value = data
        Observable.just(data).map {
            val abbrList = mutableListOf<String>()
            for (item in data) {
                abbrList.add(item.abbr)
            }
            return@map abbrList
        }.subscribe(
            { currencyList.value = it },
            { onLoadingError() }
        )
    }

}