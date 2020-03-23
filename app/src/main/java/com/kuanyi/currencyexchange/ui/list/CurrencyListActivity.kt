package com.kuanyi.currencyexchange.ui.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuanyi.currencyexchange.R
import com.kuanyi.currencyexchange.base.BaseLoadingViewModel
import com.kuanyi.currencyexchange.databinding.ActivityCurrencyListBinding
import com.kuanyi.currencyexchange.ui.adapter.CurrencyListAdapter
import com.kuanyi.currencyexchange.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_currency_list.*


class CurrencyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyListBinding
    private lateinit var viewModel: CurrencyListViewModel

    private var adapter = CurrencyListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_list)
        viewModel = ViewModelProviders.of(this).get(CurrencyListViewModel::class.java)
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

        viewModel.currecyList.observe(this, Observer {
            adapter.setCurrencyList(it)
        })
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }


    private fun displayError() {
        ViewUtils.showErrorSnackbar(root_layout, View.OnClickListener {
            viewModel.loadCurrencyList()
        })
    }
}
