package com.goodswarehouse.scanner.presentation.screens.pallet

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.SPECIFIC_D_SKU_LIST
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.disableAutoShowKeyboard
import com.goodswarehouse.scanner.presentation.navigate
import com.goodswarehouse.scanner.presentation.requestFocusDelayed
import com.goodswarehouse.scanner.presentation.withActivity
import kotlinx.android.synthetic.main.fragment_pallet.*

class PalletFragment : BaseFragment<PalletViewModel>(), PalletView {

    override fun getViewModelClass(): Class<PalletViewModel> = PalletViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_pallet

    override fun viewInit() {
        setTitle("Enter Pallet NO.")
        addBackArrow(false)

        edt_pallet.disableAutoShowKeyboard()
        edt_lot.disableAutoShowKeyboard()

        requestFocusPallet()
    }

    override fun submit(palletNo: String, lotNo: String, itemNo: String, isOffline : Boolean) {
        withActivity<BaseActivity> {
            trackModel.palletNo = palletNo
            trackModel.lotNo = lotNo
            trackModel.itemNo = itemNo
            this.hasOfflinePallet = hasOfflinePallet
        }
        if (isSpecificD(itemNo)){
            this.navigate(R.id.open_track_d_fragment, R.id.pallet_fragment)
        } else {
            this.navigate(R.id.open_track_fragment, R.id.pallet_fragment)
        }
    }

    private fun isSpecificD(itemNo: String): Boolean {
        SPECIFIC_D_SKU_LIST.map {
            if (it.contains(itemNo, true))
                return true
        }
        return false
    }

    override fun beforeViewInit() = setHasOptionsMenu(true)


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_logoff, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logoff -> {
            this.navigate(R.id.pallet_logoff, R.id.pallet_fragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun requestFocusPallet() = edt_pallet.requestFocusDelayed()
    override fun requestFocusLot()    = edt_lot   .requestFocusDelayed()
    override fun requestFocusItem()   = edt_item  .requestFocusDelayed()

    override fun onSubmitClick() {
        withActivity<BaseActivity> {
            if (isPhoneOnline) {
                viewModel?.sendRequest()
            } else {
                viewModel?.saveAsOffLine()
            }
        }
    }

}