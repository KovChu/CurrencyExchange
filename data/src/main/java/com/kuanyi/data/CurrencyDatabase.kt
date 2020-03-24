package com.kuanyi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuanyi.data.dao.CurrencyDao
import com.kuanyi.data.model.CurrencyModel


@Database(
    entities = [CurrencyModel::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
