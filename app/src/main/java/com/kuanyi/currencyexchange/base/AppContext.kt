package com.kuanyi.currencyexchange.base

import android.app.Application


class AppContext : Application() {

    companion object {
        lateinit var INSTANCE: AppContext
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}