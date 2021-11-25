package com.goodswarehouse.scanner.presentation.screens.track.d

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.*
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.screens.track.TrackView
import kotlinx.android.synthetic.main.fragment_track.*
import kotlinx.android.synthetic.main.fragment_track.edt_phial
import kotlinx.android.synthetic.main.fragment_track.edt_phial2
import kotlinx.android.synthetic.main.fragment_track.edt_track
import kotlinx.android.synthetic.main.fragment_track.radio_group
import kotlinx.android.synthetic.main.fragment_track.rb1
import kotlinx.android.synthetic.main.fragment_track.rb2
import kotlinx.android.synthetic.main.fragment_track.rb3
import kotlinx.android.synthetic.main.fragment_track.rb4
import kotlinx.android.synthetic.main.view_specific_d.*

class TrackDFragment : BaseFragment<TrackDViewModel>(), TrackView {

    override fun getViewModelClass(): Class<TrackDViewModel> = TrackDViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_track_d

    override fun onResume() {
        super.onResume()

        view?.postDelayed({
            view?.hideSoftKeyboard()
        }, 200)

    }

    override fun clearQty() {
        radio_group?.clearCheck()
        nonFocusView?.requestFocusDelayed()
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

        radio_group?.setOnCheckedChangeListener { radioGroup, optionId ->
            run {

                requestFocusPhial()

                when (optionId) {
                    R.id.rb1 -> { viewModel?.updateKitsCount(1) }
                    R.id.rb2 -> { viewModel?.updateKitsCount(2) }
                    R.id.rb3 -> { viewModel?.updateKitsCount(3) }
                    R.id.rb4 -> { viewModel?.updateKitsCount(4) }
                    else -> {  }
                }
            }
        }

        edt_track?.disableAutoShowKeyboard()

        withActivity<BaseActivity> { viewModel?.trackModel = trackModel }
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

    override fun navigateBack() = this.navigate(R.id.back_to_pallet_fragment, R.id.track_d_fragment)

    override fun beforeViewInit() = setHasOptionsMenu(true)

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

    override fun requestFocusPhial()  = edt_phial .requestFocusDelayed()
    override fun requestFocusPhial2() = edt_phial2.requestFocusDelayed()
    override fun requestFocusPhial3() = edt_phial3.requestFocusDelayed()
    override fun requestFocusPhial4() = edt_phial4.requestFocusDelayed()
    override fun requestFocusTrack()  = edt_track .requestFocusDelayed()

    override fun requestNextFocusD(current: Int, count: Int?) {
        if (count != null && count > 0) {
            if (count == current) {
                requestFocusTrack()
            } else {
                when (count) {
                    2 -> when (current) {
                        1 -> requestFocusPhial2()
                    }
                    3 -> when (current) {
                        1 -> requestFocusPhial2()
                        2 -> requestFocusPhial3()
                    }
                    4 -> when (current) {
                        1 -> requestFocusPhial2()
                        2 -> requestFocusPhial3()
                        3 -> requestFocusPhial4()
                    }
                }
            }
        }
    }

}