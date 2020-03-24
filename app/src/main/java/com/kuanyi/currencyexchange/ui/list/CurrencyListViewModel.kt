package com.kuanyi.currencyexchange.ui.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.data.BuildConfig
import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyModel
import com.kuanyi.data.model.CurrencyQuotes
import com.kuanyi.data.remote.CurrencyAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyListViewModel : BaseLoadingViewModel() {

    private var source: String = "USD"

    @Inject
    lateinit var currencyAPI: CurrencyAPI

    private lateinit var subscription: Disposable

    val currencyList: MutableLiveData<List<CurrencyModel>> = MutableLiveData()

    init {
        loadCurrencyList()
    }

    fun setSource(source: String) {
        this.source = source
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
        currencyList.value = data
    }
}