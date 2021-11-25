package com.goodswarehouse.scanner.presentation.base

import IS_DEV_MODE
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.BuildConfig
import com.goodswarehouse.scanner.WITH_LOGS
import com.goodswarehouse.scanner.presentation.custom.onUi
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel<T : BaseView> : ViewModel() {

    val isDebugMode = BuildConfig.DEBUG || IS_DEV_MODE

    var view: T? = null

    protected var disposables = CompositeDisposable()

    open fun dispose() {
        disposables.clear()
        view?.disposePendingActions()
    }

    fun handleCommonErrors(e: Throwable?) {
        e?.printStackTrace()
        onUi {
            e?.apply {
                localizedMessage?.apply {
                    view?.showError(this)
                }
            }
        }
    }
}