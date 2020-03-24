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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.kuanyi.currencyexchange.R
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.currencyexchange.databinding.ActivityCurrencyQuoteBinding
import com.kuanyi.currencyexchange.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_currency_list.*
import kotlinx.android.synthetic.main.activity_currency_list.progressBar
import kotlinx.android.synthetic.main.activity_currency_list.recyclerView
import kotlinx.android.synthetic.main.activity_currency_quote.*

class CurrencyQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyQuoteBinding
    private lateinit var viewModel: CurrencyQuoteViewModel

    private var currency: String = "USD"

    private val COLUMN_COUNT = 2

    private var adapter =
        CurrencyQuoteAdapter(currency)

    companion object {
        const val BUNDLE_CURRENCY = "BUNDLE_CURRENCY"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_quote)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory { CurrencyQuoteViewModel(currency) })
                .get(CurrencyQuoteViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                BaseLoadingViewModel.LoadingStatus.STARTED ->
                    progressBar.visibility = View.VISIBLE
                BaseLoadingViewModel.LoadingStatus.FINISHED ->
                    progressBar.visibility = View.GONE
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
                spinnerCurrency.setSelection(spinnerAdapter.getPosition(currency))
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

        recyclerView.layoutManager = GridLayoutManager(this, COLUMN_COUNT)
        recyclerView.addItemDecoration(
            GridSpacingItemDecorator(
                COLUMN_COUNT,
                resources.getDimensionPixelSize(R.dimen.spacing_8),
                false
            )
        )

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        savedInstanceState?.let {
            editAmount.setText(it.getString(BUNDLE_AMOUNT))
        }
        editAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                displayAmount(editAmount.text.toString().toDouble())
                true
            } else {
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_AMOUNT, editAmount.text.toString())
        outState.putString(BUNDLE_CURRENCY, currency)
    }

    private fun displayAmount(amount: Double) {
        txtQuoteHint.text = String.format(getString(R.string.txt_quote_amount), amount, currency)
        adapter.quoteInput = amount
    }


    private fun displayError() {
        ViewUtils.showErrorSnackbar(root_layout, View.OnClickListener {
            viewModel.loadCurrencyList()
        })
    }

    private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }
}