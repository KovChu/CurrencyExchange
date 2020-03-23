package com.kuanyi.currencyexchange.component

import com.kuanyi.currencyexchange.ui.list.CurrencyListViewModel
import com.kuanyi.currencyexchange.ui.quote.CurrencyQuoteViewModel
import com.kuanyi.data.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelComponent {

    fun inject(currencyListViewModel: CurrencyListViewModel)

    fun inject(currencyQuoteViewModel: CurrencyQuoteViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelComponent

        fun networkModule(networkModule: NetworkModule): Builder
    }
}