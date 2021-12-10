package com.goodswarehouse.scanner.presentation.screens.acceptance

import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.default
import com.goodswarehouse.scanner.defaultActions
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.domain.track.TrackEventInteractor
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import com.goodswarehouse.scanner.toDisposables
import javax.inject.Inject

class AcceptanceViewModel @Inject constructor() : BaseViewModel<AcceptanceView>() {

    @Inject
    lateinit var interactor: TrackEventInteractor

    var itemNo = ObservableField("")
    var boxNo = ObservableField("")
    var productCnt = ObservableField("")

    fun clear() {
        itemNo.set("")
        boxNo.set("")
        productCnt.set("")
    }

    fun clearItemNo() = itemNo.set("")
    fun clearBoxNo() = boxNo.set("")
    fun clearProductCnt() = productCnt.set("")

    fun verification() {
        val itemNoEan = itemNo.get()
        val boxNoEan = boxNo.get()
        val productCntEan = productCnt.get()

        if (
            !itemNoEan.isNullOrBlank() &&
            !boxNoEan.isNullOrBlank() &&
            !productCntEan.isNullOrBlank()
        ) {
            val item = TrackModel()
            item.itemNo = itemNoEan
            item.boxNo = boxNoEan
            item.productCnt = productCntEan
            item.shopNo = ""
            submit(item)
        } else {
            view?.onVibrate()
            view?.showMessage("Зіскануйте всі поля")
        }
    }

    fun submit(trackModel: TrackModel) = view?.submit(trackModel)

    fun sendRequest(trackModel: TrackModel) {
        interactor.restRepo.sendTrack(TrackRequest(listOf(trackModel)))
            .doOnSubscribe { view?.showProgress() }
            .default { handleCommonErrors(it) }
            .defaultActions(view)
            .subscribe({
                it?.apply {
                    clear()
                }
                view?.hideProgress()
            }, {
                view?.hideProgress()
            })
            .toDisposables(disposables)
    }

    fun saveAsOffLine(trackModel: TrackModel) {
        interactor.save(trackModel)
        clear()
    }

}