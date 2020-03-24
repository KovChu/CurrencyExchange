package com.kuanyi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuanyi.data.model.CurrencyModel
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<CurrencyModel>): List<Long>

    @Query("SELECT * from currency")
    fun getCurrencyQuotes(): Single<List<CurrencyModel>>

    @Query("SELECT abbr from currency")
    fun getCurrencyList(): Single<List<String>>
}