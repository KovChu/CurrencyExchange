package com.kuanyi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyModel(

    @PrimaryKey
    @ColumnInfo
    val abbr: String,
    @ColumnInfo
    val rate: Double = 0.0


)