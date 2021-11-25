package com.goodswarehouse.scanner.presentation.screens.track

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.*
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_track.*

class TrackFragment : BaseFragment<TrackViewModel>(), TrackView {

    override fun getViewModelClass(): Class<TrackViewModel> = TrackViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_track

    override fun onResume() {
        super.onResume()

        view?.postDelayed({
            view?.hideSoftKeyboard()
        }, 200)

    }

    override fun clearQty() {
        radio_group.clearCheck()
        nonFocusView.requestFocusDelayed()
    }

    override fun freezeQtyMode(qty: Int) {
        var freezeQty = qty
        when (qty) {
            1 -> { rb1?.isChecked = true }
            2 -> { rb2?.isChecked = true }
            3 -> { rb3?.isChecked = true }
            4 -> { rb4?.isChecked = true }
            else -> {
                rb1?.isChecked = true
                freezeQty = 1
            }
        }

        viewModel?.updateKitsCount(freezeQty)
    }

    override fun viewInit() {
        setTitle("Enter RM track.".uppercase())
        addBackArrow(true)

        radio_group.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb1 -> { viewModel?.updateKitsCount(1) }
                    R.id.rb2 -> { viewModel?.updateKitsCount(2) }
                    R.id.rb3 -> { viewModel?.updateKitsCount(3) }
                    R.id.rb4 -> { viewModel?.updateKitsCount(4) }
                    else -> {  }
                }
            }
        }

        edt_track.disableAutoShowKeyboard()

        withActivity<BaseActivity> { viewModel?.trackModel = trackModel }
        viewModel?.checkSKU()
        viewModel?.checkIfPrefixIsNeeded()
        viewModel?.updateDebugInfo()
    }

    override fun onSubmitClick() {
        withActivity<BaseActivity> {
            if (isPhoneOnline) {
                viewModel?.sendRequest()
            } else {
                viewModel?.saveAsOffLine()
            }
        }
    }

    override fun navigateBack() {
        this.navigate(R.id.back_to_pallet_fragment, R.id.track_fragment)
    }

    override fun beforeViewInit() {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_logoff, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logoff -> {
            this.navigate(R.id.track_logoff, R.id.track_fragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun requestFocusPhial()  = edt_phial.requestFocusDelayed()
    override fun requestFocusPhial2() = edt_phial2.requestFocusDelayed()

    override fun requestFocusTrack()  = edt_track.requestFocusDelayed()

}