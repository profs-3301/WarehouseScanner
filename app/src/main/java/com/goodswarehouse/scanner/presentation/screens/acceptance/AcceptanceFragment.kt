package com.goodswarehouse.scanner.presentation.screens.acceptance

import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.withActivity

class AcceptanceFragment : BaseFragment<AcceptanceViewModel>(), AcceptanceView {

    override fun getViewModelClass(): Class<AcceptanceViewModel> = AcceptanceViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_acceptance

    override fun viewInit() {
        setTitle("Acceptance")
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