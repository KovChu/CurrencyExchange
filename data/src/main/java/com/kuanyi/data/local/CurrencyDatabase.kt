package com.kuanyi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuanyi.data.model.CurrencyModel

@Database(
    entities = [CurrencyModel::class],
    version = 1,
    exportSchema = false
)

abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    companion object {

        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getDatabase(context: Context): CurrencyDatabase? {
            if (INSTANCE == null) {
                synchronized(CurrencyDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CurrencyDatabase::class.java, "currency_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}