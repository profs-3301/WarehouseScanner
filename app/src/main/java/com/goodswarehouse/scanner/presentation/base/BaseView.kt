package com.goodswarehouse.scanner.presentation.base

import com.goodswarehouse.scanner.domain.model.Scan

interface BaseView{

    fun showError(message: String, title: String = "Error"): Unit?
    fun showError(scans: List<Scan>, sku: String): Unit?

    fun showMessage(message: String, title: String? = null): Unit?
    fun onVibrate(): Unit?

    fun showSnackBar(message: String?): Unit?

    fun showProgress()

    fun hideProgress()

    fun viewInit(){}

    fun beforeViewInit(){}

    fun disposePendingActions(){}

    fun navigateBack(){}

    fun updateNetworkState(online: Boolean){}

}