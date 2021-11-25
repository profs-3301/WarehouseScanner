package com.goodswarehouse.scanner.presentation.screens.login

import android.content.Context
import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.BuildConfig
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(val context: Context) : BaseViewModel<LoginView>() {

    var nameBind: ObservableField<String> = ObservableField(
        if (BuildConfig.DEBUG) "userName"
        else ""
    )

    fun clear() = nameBind.set("")

    fun login() = view?.submit(nameBind.get())

}