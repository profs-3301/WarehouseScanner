package com.goodswarehouse.scanner.presentation.screens.departure

import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.withActivity

class DepartureFragment : BaseFragment<DepartureViewModel>(), DepartureView {

    override fun getViewModelClass(): Class<DepartureViewModel> = DepartureViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_departure

    override fun viewInit() {
        setTitle("Departure")
        addBackArrow(true)
    }

    override fun submit(trackModel: TrackModel) {
        withActivity<BaseActivity> {
            if (isPhoneOnline) {
                viewModel?.sendRequest(trackModel)
            } else {
                viewModel?.saveAsOffLine(trackModel)
            }
        }
    }

}