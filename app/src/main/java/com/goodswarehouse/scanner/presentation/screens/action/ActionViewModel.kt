package com.goodswarehouse.scanner.presentation.screens.action

import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import javax.inject.Inject

class ActionViewModel @Inject constructor() : BaseViewModel<ActionView>() {

    var nameBind: ObservableField<String> = ObservableField(
        if (BuildConfig.DEBUG) "userName"
        else ""
    )

    fun clear() = nameBind.set("")

    fun acceptance() = view?.acceptance()
    fun departure()  = view?.departure()

}