package com.kuanyi.currencyexchange.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuanyi.currencyexchange.databinding.ItemCurrencyBinding
import com.kuanyi.data.model.CurrencyModel

class CurrencyListAdapter : RecyclerView.Adapter<CurrencyListAdapter.CurrencyListViewHolder>() {

    private var currencies: List<CurrencyModel> = emptyList()

    var onItemClick: ((CurrencyModel) -> Unit)? = null

    fun setCurrencyList(currencies: List<CurrencyModel>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return currencies.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(currencies[position])
    }


    inner class CurrencyListViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemCurrencyBinding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {



        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(currencies[adapterPosition])
            }
        }

        fun bind(currency: CurrencyModel) {
            binding.currency = currency
            binding.executePendingBindings()
        }

    }
}