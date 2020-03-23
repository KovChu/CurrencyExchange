package com.kuanyi.currencyexchange.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kuanyi.data.model.CurrencyModel

@BindingAdapter("setCurrencyName")
fun setCurrencyName(textView: TextView, currency: CurrencyModel) {
    textView.text = "${currency.name} (${currency.abbr})"
}