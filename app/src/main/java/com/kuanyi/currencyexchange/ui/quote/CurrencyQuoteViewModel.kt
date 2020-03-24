package com.kuanyi.currencyexchange.ui.quote

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.MutableLiveData
import com.kuanyi.currencyexchange.base.AppContext
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.data.dao.CurrencyDao
import com.kuanyi.data.model.CurrencyModel
import com.kuanyi.data.model.CurrencyQuotes
import com.kuanyi.data.remote.CurrencyAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyQuoteViewModel(private val currencyDao: CurrencyDao) : BaseLoadingViewModel() {

    @Inject
    lateinit var currencyAPI: CurrencyAPI

    private var compositeDisposable = CompositeDisposable()

    val quoteList: MutableLiveData<List<CurrencyModel>> = MutableLiveData()

    val currencyList: MutableLiveData<List<String>> = MutableLiveData()

    companion object {
        const val STORAGE = "STORAGE"
        const val UPDATE_TIME = "UPDATE_TIME"

        //set the expire time to 30 minutes
        const val EXPIRE_TIME = 30 * 60 * 1000
    }

    init {
        loadCurrencyList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun loadCurrencyList() {
        if (isExpired()) {
            getDataFromAPI()
        } else {
            getDataFromStorage()
        }

    }

    private fun getDataFromAPI() {
        currencyAPI.getCurrencyQuote()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLoadingStarted() }
            .map { currencyQuote: CurrencyQuotes ->
                val quotes = currencyQuote.convertCurrencyQuotes()
                currencyDao.insertNews(quotes)
                return@map quotes
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val pref = AppContext.INSTANCE.getSharedPreferences(STORAGE, MODE_PRIVATE)
                    pref.edit().putLong(UPDATE_TIME, System.currentTimeMillis()).apply()
                    getDataFromStorage()
                },
                {
                    onLoadingError()
                }).apply { compositeDisposable.add(this) }
    }

    private fun getDataFromStorage() {
        currencyDao.getCurrencyQuotes()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLoadingStarted() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onLoadingSuccess(data) },
                {
                    onLoadingError()
                }).apply { compositeDisposable.add(this) }
    }


    private fun isExpired(): Boolean {
        val pref = AppContext.INSTANCE.getSharedPreferences(STORAGE, MODE_PRIVATE)
        val updateTime = pref.getLong(UPDATE_TIME, 0)
        return System.currentTimeMillis() - updateTime > EXPIRE_TIME
    }


    private fun onLoadingSuccess(data: List<CurrencyModel>) {
        super.onLoadingSuccess()
        quoteList.value = data
        currencyDao.getCurrencyList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { list ->
                    onLoadingFinished()
                    currencyList.value = list
                },
                {
                    onLoadingError()
                }).apply { compositeDisposable.add(this) }
    }

}