package com.goodswarehouse.scanner.presentation.screens.departure

import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.presentation.base.BaseView

interface DepartureView: BaseView {

    fun submit(trackModel: TrackModel)

}