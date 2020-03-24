package com.kuanyi.currencyexchange.ui.quote

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.kuanyi.currencyexchange.R
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.currencyexchange.databinding.ActivityCurrencyQuoteBinding
import com.kuanyi.data.CurrencyDatabase
import kotlinx.android.synthetic.main.activity_currency_quote.*

class CurrencyQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyQuoteBinding
    private lateinit var viewModel: CurrencyQuoteViewModel

    private var currency: String = "AED"

    private var adapter = CurrencyQuoteAdapter()

    companion object {
        const val BUNDLE_CURRENCY = "BUNDLE_CURRENCY"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_quote)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_quote)

        setupViewModel()

        setupRecyclerView()

        savedInstanceState?.let {
            editAmount.setText(it.getString(BUNDLE_AMOUNT))
            currency = it.getString(BUNDLE_CURRENCY).toString()
        }

        setupAmountText()

        setupRefreshView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_AMOUNT, editAmount.text.toString())
        outState.putString(BUNDLE_CURRENCY, currency)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this))
            .get(CurrencyQuoteViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                BaseLoadingViewModel.LoadingStatus.STARTED ->
                    swipeRefreshLayout.isRefreshing = true
                BaseLoadingViewModel.LoadingStatus.FINISHED ->
                    swipeRefreshLayout.isRefreshing = false
                BaseLoadingViewModel.LoadingStatus.ERROR ->
                    displayError()
                else -> {
                }
            }
        })

        viewModel.quoteList.observe(this, Observer {
            adapter.setCurrencyList(it)
            displayAmount(editAmount.text.toString().toDouble())
        })

        viewModel.currencyList.observe(this, Observer {
            ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_item,
                it
            ).also { spinnerAdapter ->
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCurrency.adapter = spinnerAdapter
                spinnerCurrency.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {}

                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            currency = spinnerAdapter.getItem(p2).toString()
                            adapter.changeCurrency(currency)
                            displayAmount(editAmount.text.toString().toDouble())
                        }


                    }

            }
        })
    }

    private fun setupRecyclerView() {
        val column = calculateColumnCount()
        recyclerView.layoutManager = GridLayoutManager(this, column)
        recyclerView.addItemDecoration(
            GridSpacingItemDecorator(
                column,
                resources.getDimensionPixelSize(R.dimen.spacing_8),
                false
            )
        )

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    private fun calculateColumnCount(): Int {
        return (resources.displayMetrics.widthPixels / resources.getDimensionPixelSize(R.dimen.currency_list_item_width))
    }

    private fun setupAmountText() {
        editAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                displayAmount(editAmount.text.toString().toDouble())
                true
            } else {
                false
            }
        }
    }


    private fun displayAmount(amount: Double) {
        txtQuoteHint.text = String.format(getString(R.string.txt_quote_amount), amount, currency)
        adapter.quoteInput = amount
    }

    private fun setupRefreshView() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadCurrencyList()
        }
    }


    private fun displayError() {
        val snackbar = Snackbar.make(
            root_layout,
            getText(R.string.txt_loading_error),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(getText(R.string.btn_retry)) {
            viewModel.loadCurrencyList()
        }
        snackbar.show()
    }

    class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CurrencyQuoteViewModel::class.java)) {
                val db = Room.databaseBuilder(
                    activity.applicationContext,
                    CurrencyDatabase::class.java,
                    "currency"
                ).build()
                @Suppress("UNCHECKED_CAST")
                return CurrencyQuoteViewModel(db.currencyDao()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}