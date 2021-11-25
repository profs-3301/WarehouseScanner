package com.goodswarehouse.scanner.presentation.screens.track.d

import android.content.Context
import androidx.databinding.ObservableField
import android.widget.CompoundButton
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.model.*
import com.goodswarehouse.scanner.domain.track.TrackEventInteractor
import com.goodswarehouse.scanner.presentation.*
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import com.goodswarehouse.scanner.presentation.custom.ScannerTextWatcher
import com.goodswarehouse.scanner.presentation.screens.track.TrackView
import java.util.*
import javax.inject.Inject


class TrackDViewModel @Inject constructor(val context: Context) : BaseViewModel<TrackView>() {

    @Inject
    lateinit var localRepo: LocalRepo
    @Inject
    lateinit var restRepo: RestRepo

    @Inject
    lateinit var interactor: TrackEventInteractor

    var kitsList : MutableList<PhialModel> = mutableListOf()

    var kitsCountBind: ObservableField<Int> = ObservableField(0)

    var phialBind  : ObservableField<String> = ObservableField("")
    var phial2Bind : ObservableField<String> = ObservableField("")
    var phial3Bind : ObservableField<String> = ObservableField("")
    var phial4Bind : ObservableField<String> = ObservableField("")
    var trackBind  : ObservableField<String> = ObservableField("")

    //isQtyNormalMode or freezeQtyMode
    var isQtyNormalModeBind: ObservableField<Boolean> = ObservableField(true)

    var prefixes: List<String> = listOf()

    //for debug
    var debugTextBind : ObservableField<String>  = ObservableField("")

    lateinit var trackModel: TrackModel

    fun onQtyModeChanged(v: CompoundButton, isChecked: Boolean) {
       if (isChecked) {
            view?.freezeQtyMode(kitsCountBind.get() ?: 0)
        }
        isQtyNormalModeBind.set(!isChecked)
    }

    fun clear() {
        kitsCountBind.get()?.apply {
            phialBind .set("")
            phial2Bind.set("")
            phial3Bind.set("")
            phial4Bind.set("")
            trackBind .set("")

            kitsList = mutableListOf()

            //updated only for normal mode, not for freeze mode
            if (isQtyNormalModeBind.get() == true) {
                updateKitsCount(0)
            } else {
                view?.requestFocusPhial()
            }
        }
    }

    fun clearPhial() {
        phialBind.set("")
        view?.requestFocusPhial()
    }

    fun clearPhial2() {
        phial2Bind.set("")
        view?.requestFocusPhial2()
    }

    fun clearPhial3() {
        phial3Bind.set("")
        view?.requestFocusPhial3()
    }

    fun clearPhial4() {
        phial4Bind.set("")
        view?.requestFocusPhial4()
    }

    fun clearTrack() {
        trackBind.set("")
        view?.requestFocusTrack()
    }

    private fun verificationError(message: String) {
        view?.showError(message, "Error")
    }

    //filling in the scan model and validation
    fun verification() {
        val phial1Ean  = phialBind .get() ?: ""
        val phial2Ean  = phial2Bind.get() ?: ""
        val phial3Ean  = phial3Bind.get() ?: ""
        val phial4Ean  = phial4Bind.get() ?: ""
        val trackingId = trackBind .get() ?: ""
        val kitsQty    = kitsCountBind.get() ?: 0

        trackModel.testKitQty = kitsQty
        trackModel.trackingId = trackingId

        if (kitsQty > 0 && phial1Ean.checkPhialItemNo() && trackingId.checkReturnLabel()) {
            updatePhialsList(phial1Ean, phial2Ean, phial3Ean, phial4Ean, kitsQty) {
                if (this && isUniqueList(kitsList)) {
                    onSubmit()
                } else {
                    verificationError("Wrong value for Phial field")
                }
            }
        } else {
            verificationError("Some fields contain incorrect scans")
        }
    }

    private fun updatePhialsList(
        phial1Ean: String,
        phial2Ean: String,
        phial3Ean: String,
        phial4Ean: String,
        kitsQty: Int,
        closure: Boolean.() -> Unit
    ) {
        if (kitsQty > 0) {

            kitsList = mutableListOf()

            when (kitsQty) {
                1 -> {
                    if (phial1Ean.checkPhialItemNo()) {
                        addPhial(phial1Ean, 0)
                    }
                }
                2 -> {
                    if (phial1Ean.checkPhialItemNo() &&
                        phial2Ean.checkPhialItemNo()
                    ) {
                        addPhial(phial1Ean, 0)
                        addPhial(phial2Ean, 1)
                    }
                }
                3 -> {
                    if (phial1Ean.checkPhialItemNo() &&
                        phial2Ean.checkPhialItemNo() &&
                        phial3Ean.checkPhialItemNo()
                    ) {
                        addPhial(phial1Ean, 0)
                        addPhial(phial2Ean, 1)
                        addPhial(phial3Ean, 2)
                    }
                }
                4 -> {
                    if (phial1Ean.checkPhialItemNo() &&
                        phial2Ean.checkPhialItemNo() &&
                        phial3Ean.checkPhialItemNo() &&
                        phial4Ean.checkPhialItemNo()
                    ) {
                        addPhial(phial1Ean, 0)
                        addPhial(phial2Ean, 1)
                        addPhial(phial3Ean, 2)
                        addPhial(phial4Ean, 3)
                    }
                }
            }

            //the valid size should be the same as kitsQty
            closure(kitsList.size == kitsQty)
        }
    }


    private fun onSubmit() {
        context.showDebugMessage("onSubmit")
        trackModel.setPhialsList(kitsList)
        view?.onSubmitClick()
    }

    fun saveAsOffLine() {
        interactor.save(trackModel)
        clear()
    }

    fun sendRequest() {

        val track =  TrackRequest(listOf(trackModel))

        context.showDebugMessage("send: ${track.toJson()}")

        if (WITH_LOGS) {
            context.addLog(TrackRequest(listOf(trackModel)))
        }

        restRepo.sendTrack(track)
            .doOnSubscribe { view?.showProgress() }
            .default { handleCommonErrors(it) }
            .defaultActions(view)
            .subscribe({
                it?.apply {
                    onSuccessfulResponse(this)
                }
                view?.hideProgress()
            }, {
                context.showDebugMessage("ERROR: ${it.message}")
                view?.hideProgress()
            })
            .toDisposables(disposables)
    }

    private fun onSuccessfulResponse(response: TrackResponse) {
        context.showDebugMessage("onSuccessfulResponse: $this")

        var scans: List<Scan> = response.scans.filter {it.result != "0"}

        if (scans.isEmpty()) {
            clear()
        } else {
            if (WITH_LOGS) {
                context.addResponseLog("$response")
            }
            context.showDebugMessage("error: $scans")
            view?.showError(scans, trackModel.itemNo!!)
        }
    }

    fun updateKitsCount(count: Int){
        if (count == 0) {
            view?.clearQty()
        }
        kitsCountBind.set(count)

        kitsCountBind.get()?.apply {
            while (kitsList.size > this) {
                kitsList.removeAt(kitsList.size - 1)
            }
        }
    }

    private fun addPhial(phial: String, position: Int) {
        val phial = PhialModel(phial, UUID.randomUUID().toString().substring(0,11))
        if (kitsList.size > position) {
            kitsList[position] = phial
        } else {
            kitsList.add(phial)
        }
    }

    private fun isUniqueList(list: List<PhialModel>): Boolean {
        return list.size == kitsList.map { it.phialNo }.distinct().size
    }

    val phialTextChangeListener = object : ScannerTextWatcher({ ean ->
        ean.checkPhialItemNo().let { isValid ->
            if (isValid) {
                if (ean.hasCorrectPrefix(prefixes)) {
                    phialBind.set(ean)
                    view?.requestNextFocusD(1, kitsCountBind.get())
                } else {
                    verificationError(context.getString(R.string.error_prefix, prefixes))
                }
            } else {
                phialBind.set("")
                view?.onVibrate()
            }
        }
    }) {}

    val phial2TextChangeListener = object : ScannerTextWatcher({ ean ->
        ean.checkPhialItemNo().let { isValid ->
            if (isValid) {
                if (ean.hasCorrectPrefix(prefixes)) {
                    phial2Bind.set(ean)
                    view?.requestNextFocusD(2, kitsCountBind.get())
                } else {
                    verificationError(context.getString(com.goodswarehouse.scanner.R.string.error_prefix, prefixes))
                }
            } else {
                phial2Bind.set("")
                view?.onVibrate()
            }
        }
    }) {}

    val phial3TextChangeListener = object : ScannerTextWatcher({ ean ->
        ean.checkPhialItemNo().let { isValid ->
            if (isValid) {
                if (ean.hasCorrectPrefix(prefixes)) {
                    phial3Bind.set(ean)
                    view?.requestNextFocusD(3, kitsCountBind.get())
                } else {
                    verificationError(context.getString(com.goodswarehouse.scanner.R.string.error_prefix, prefixes))
                }
            } else {
                phial3Bind.set("")
                view?.onVibrate()
            }
        }
    }) {}

    val phial4TextChangeListener = object : ScannerTextWatcher({ ean ->
        ean.checkPhialItemNo().let { isValid ->
            if (isValid) {
                if (ean.hasCorrectPrefix(prefixes)) {
                    phial4Bind.set(ean)
                    view?.requestNextFocusD(4, kitsCountBind.get())
                } else {
                    verificationError(context.getString(com.goodswarehouse.scanner.R.string.error_prefix, prefixes))
                }
            } else {
                phial4Bind.set("")
                view?.onVibrate()
            }
        }
    }) {}

    val trackTextChangeListener = object : ScannerTextWatcher({ ean ->
        ean.checkReturnLabel().let { isValid ->
            if (isValid) {
                trackBind.set(ean)
            } else {
                trackBind.set("")
                view?.onVibrate()
            }
        }
    }) {}

    ////
    fun checkIfPrefixIsNeeded() {
        prefixes = getSkuPrefixes(trackModel.itemNo!!, localRepo.getPrefixesList())
    }

    //for debug
    fun updateDebugInfo() {
        debugTextBind.set("SPECIFIC_D\n" +
                "isPrefixExpected = ${!prefixes.isNullOrEmpty()}" + "\n" +
                prefixes
        )
    }


}