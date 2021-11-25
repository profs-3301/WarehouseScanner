package com.goodswarehouse.scanner.presentation.screens.track

import com.goodswarehouse.scanner.presentation.base.BaseView

interface TrackView : BaseView {

    fun requestNextFocusD(current: Int, count: Int?) {}

    fun requestFocusPhial()  {}
    fun requestFocusPhial2() {}
    fun requestFocusPhial3() {}
    fun requestFocusPhial4() {}

    fun requestFocusTrack()  {}

    fun freezeQtyMode(qty: Int)

    fun onSubmitClick()
    fun submit() {}
    fun clearQty()
}