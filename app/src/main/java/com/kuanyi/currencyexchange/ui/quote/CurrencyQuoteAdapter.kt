package com.kuanyi.currencyexchange.ui.quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuanyi.currencyexchange.databinding.ItemCurrencyBinding
import com.kuanyi.data.model.CurrencyModel

class CurrencyQuoteAdapter() :
    RecyclerView.Adapter<CurrencyQuoteAdapter.CurrencyQuoteViewHolder>() {

    private var currencies: List<CurrencyModel> = emptyList()

    var quoteInput: Double = -1.0
        set(value) {
            //only support set that is positive
            if (value > 0.0) {
                field = value
                notifyDataSetChanged()
            }
        }

    var baseRate = 1.0

    private lateinit var currency: String

    fun changeCurrency(newCurrency: String) {
        currency = newCurrency
        for (model in currencies) {
            if (model.abbr == currency) {
                baseRate = model.rate
                break
            }
        }
    }


    fun setCurrencyList(currencies: List<CurrencyModel>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyQuoteViewHolder {
        return CurrencyQuoteViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CurrencyQuoteViewHolder, position: Int) {
        holder.bind(currencies[position])
    }


    inner class CurrencyQuoteViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemCurrencyBinding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyModel) {
            binding.currency = item
            binding.txtQuote.text = "%.2f".format((item.rate / baseRate) * quoteInput)
            binding.txtName.text = item.abbr
            binding.executePendingBindings()
        }

    }
}