package com.goodswarehouse.scanner.presentation.screens.login

import com.goodswarehouse.scanner.presentation.base.BaseView

interface LoginView: BaseView {

    fun submit(userName: String?)

}