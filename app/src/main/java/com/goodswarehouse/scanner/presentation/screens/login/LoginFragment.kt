package com.goodswarehouse.scanner.presentation.screens.login

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.base.BaseFragment
import com.goodswarehouse.scanner.presentation.navigate
import com.goodswarehouse.scanner.presentation.permissionCheck
import com.goodswarehouse.scanner.presentation.showSoftKeyboard
import com.goodswarehouse.scanner.presentation.withActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.system.exitProcess

class LoginFragment : BaseFragment<LoginViewModel>(), LoginView {

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun getLayoutResource(): Int = R.layout.fragment_login

    override fun viewInit() {
        setTitle("Login")
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

    override fun submit(userName: String?) {
        withActivity<BaseActivity> {
            trackModel.user = userName
            permissionCheck(android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if (this) {
                    withActivity<BaseActivity> {
                        getDeviceId()
                    }
                    navigate(R.id.open_action, R.id.login_fragment)
                }
            }
        }
    }

}