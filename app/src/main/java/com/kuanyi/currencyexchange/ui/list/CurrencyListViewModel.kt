package com.kuanyi.currencyexchange.ui.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.data.BuildConfig
import com.kuanyi.data.model.CurrencyList
import com.kuanyi.data.model.CurrencyModel
import com.kuanyi.data.remote.CurrencyAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyListViewModel : BaseLoadingViewModel() {
    @Inject
    lateinit var currencyAPI: CurrencyAPI

    private lateinit var subscription: Disposable

    val currecyList: MutableLiveData<List<CurrencyModel>> = MutableLiveData()

    init {
        loadCurrencyList()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadCurrencyList() {
        subscription = currencyAPI.getCurrencyList(BuildConfig.CurrencyLayerAPIKey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLoadingStarted() }
            .doOnTerminate { onLoadingFinished() }
            .map { currencyList: CurrencyList ->
                return@map currencyList.toCurrencyModelList()

            }.subscribe(
                { data -> onLoadingSuccess(data) },
                {
                    onLoadingError()
                })

    }

    private fun onLoadingSuccess(data: List<CurrencyModel>) {
        super.onLoadingSuccess()
        Log.i("data", data.toString())
        currecyList.value = data
    }
}