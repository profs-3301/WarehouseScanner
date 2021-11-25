package com.goodswarehouse.scanner.presentation.screens.departure

import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import javax.inject.Inject

class DepartureViewModel @Inject constructor() : BaseViewModel<DepartureView>() {

    var itemNo = ObservableField("")
    var boxNo = ObservableField("")
    var productCnt = ObservableField("")
    var shopNo = ObservableField("")

    fun clear() {
        itemNo.set("")
        boxNo.set("")
        productCnt.set("")
        shopNo.set("")
    }

    fun clearItemNo() {
        itemNo.set("")
    }

    fun clearBoxNo() {
        boxNo.set("")
    }

    fun clearProductCnt() {
        productCnt.set("")
    }

    fun clearShopNo() {
        shopNo.set("")
    }

    fun verification() {

    }

    fun submit() {
        val itemNoEan = itemNo.get()
        val boxNoEan = boxNo.get()
        val productCntEan = productCnt.get()
        val shopNoEan = shopNo.get()

        if (
            !itemNoEan.isNullOrBlank() &&
            !boxNoEan.isNullOrBlank() &&
            !productCntEan.isNullOrBlank() &&
            !shopNoEan.isNullOrBlank()
        ) {
            view?.submit()
        } else {
            view?.onVibrate()
            view?.showMessage("Зіскануйте всі поля")
        }

    }

}