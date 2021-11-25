package com.goodswarehouse.scanner.presentation.screens.action

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.navigate
import com.goodswarehouse.scanner.presentation.showSoftKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.system.exitProcess

class ActionFragment : BaseFragment<ActionViewModel>(), ActionView {

    override fun getViewModelClass(): Class<ActionViewModel> = ActionViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_action

    override fun viewInit() {
        setTitle("Action")
        addBackArrow(false)
    }

    override fun onResume() {
        super.onResume()
        edt_name?.postDelayed({
            edt_name?.requestFocus()
            edt_name?.showSoftKeyboard()
        }, 400)
    }

    override fun beforeViewInit() {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item?.itemId) {
        R.id.action_exit -> {
            exitProcess(0)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun acceptance() {
        navigate(R.id.open_acceptance, R.id.action)
    }

    override fun departure() {
        navigate(R.id.open_departure, R.id.action)
    }

}