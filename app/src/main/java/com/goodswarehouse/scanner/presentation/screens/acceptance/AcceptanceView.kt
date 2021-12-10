package com.goodswarehouse.scanner.presentation.screens.acceptance

import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.presentation.base.BaseView

interface AcceptanceView: BaseView {

    fun submit(trackModel: TrackModel)

}