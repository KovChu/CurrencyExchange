package com.kuanyi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuanyi.data.model.CurrencyModel

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currencies : List<CurrencyModel>)


    @Query("SELECT * FROM currency ORDER BY name ASC")
    fun getCurrencyList(): List<CurrencyModel>
}