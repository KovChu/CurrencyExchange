package com.kuanyi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo var id: Int,
    @ColumnInfo
    val abbr: String,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val rate: Double
)