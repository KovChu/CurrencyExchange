package com.kuanyi.currencyexchange.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kuanyi.currencyexchange.R

class ViewUtils {
    companion object {
        fun showErrorSnackbar(view: View, onClickListener: View.OnClickListener) {
            val snackbar = Snackbar.make(
                view,
                view.resources.getText(R.string.txt_loading_error),
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.setAction(view.resources.getText(R.string.btn_retry), onClickListener)
            snackbar.show()
        }
    }
}