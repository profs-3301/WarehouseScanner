package com.goodswarehouse.scanner.presentation.screens.departure

import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.base.BaseFragment

class DepartureFragment : BaseFragment<DepartureViewModel>(), DepartureView {

    override fun getViewModelClass(): Class<DepartureViewModel> = DepartureViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_departure

    override fun viewInit() {
        setTitle("Departure")
        addBackArrow(true)
    }

    override fun submit() {
       // navigate(R.id.open_pallet_fragment, R.id.login_fragment)
    }

}