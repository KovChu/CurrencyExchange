package com.kuanyi.currencyexchange.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuanyi.currencyexchange.component.DaggerViewModelComponent
import com.kuanyi.currencyexchange.component.ViewModelComponent
import com.kuanyi.data.module.NetworkModule

abstract class BaseLoadingViewModel : ViewModel() {

    enum class LoadingStatus {
        STARTED,
        SUCCESS,
        FINISHED,
        ERROR,
    }

    val loadingStatus: MutableLiveData<LoadingStatus> = MutableLiveData()

    open fun onLoadingStarted() {
        loadingStatus.value = LoadingStatus.STARTED
    }

    open fun onLoadingFinished() {
        loadingStatus.value = LoadingStatus.FINISHED
    }

    open fun onLoadingSuccess() {
        loadingStatus.value = LoadingStatus.SUCCESS
    }

    open fun onLoadingError() {
        loadingStatus.value = LoadingStatus.ERROR
    }

    private val component: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
//            is TimelineViewModel -> component.inject(this)
//            is ProductListViewModel -> component.inject(this)
        }
    }
}