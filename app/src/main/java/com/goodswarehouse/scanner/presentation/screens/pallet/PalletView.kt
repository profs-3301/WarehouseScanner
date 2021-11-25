package com.goodswarehouse.scanner.presentation.screens.pallet

import com.goodswarehouse.scanner.presentation.base.BaseView

interface PalletView : BaseView {

    fun requestFocusPallet()
    fun requestFocusLot()
    fun requestFocusItem()

    fun onSubmitClick()
    fun submit(palletNo: String, lotNo: String, itemNo: String, hasOfflinePallet: Boolean) {}

}