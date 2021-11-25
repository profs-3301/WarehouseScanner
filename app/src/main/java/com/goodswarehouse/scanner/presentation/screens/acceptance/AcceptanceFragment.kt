package com.goodswarehouse.scanner.presentation.screens.acceptance

import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.navigate

class AcceptanceFragment : BaseFragment<AcceptanceViewModel>(), AcceptanceView {

    override fun getViewModelClass(): Class<AcceptanceViewModel> = AcceptanceViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_acceptance

    override fun viewInit() {
        setTitle("Acceptance")
        addBackArrow(true)
    }

    override fun submit() {
        navigate(R.id.open_pallet_fragment, R.id.login_fragment)
    }

}