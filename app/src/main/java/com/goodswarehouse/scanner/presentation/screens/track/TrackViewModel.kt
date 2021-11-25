package com.goodswarehouse.scanner.presentation.screens.track

import android.content.Context
import androidx.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.model.*
import com.goodswarehouse.scanner.domain.track.TrackEventInteractor
import com.goodswarehouse.scanner.presentation.base.BaseListItem
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import com.goodswarehouse.scanner.presentation.checkPhialItemNo
import com.goodswarehouse.scanner.presentation.checkShippingTrackLabel
import com.goodswarehouse.scanner.presentation.removeEanSpaces
import com.goodswarehouse.scanner.presentation.screens.track.list.ItemVM
import com.goodswarehouse.scanner.presentation.showDebugMessage
import java.util.*
import javax.inject.Inject


class TrackViewModel @Inject constructor(val context: Context) : BaseViewModel<TrackView>() {

    enum class SkuType {
        NORMAL,
        SPECIFIC_A,
        SPECIFIC_B,
        SPECIFIC_C
    }

    @Inject
    lateinit var localRepo: LocalRepo
    @Inject
    lateinit var restRepo: RestRepo

    @Inject
    lateinit var interactor: TrackEventInteractor

    var kitsList : MutableList<PhialModel> = mutableListOf()

    var kitsCountBind: ObservableField<Int> = ObservableField(0)

    var debugRequestBind : ObservableField<String> = ObservableField("")
    var debugResponseBind: ObservableField<String> = ObservableField("")

    var phialBind : ObservableField<String>  = ObservableField("")
    var phial2Bind: ObservableField<String>  = ObservableField("")
    var trackBind : ObservableField<String>  = ObservableField("")

    var phial1visibilityBind : ObservableField<Boolean> = ObservableField(false)
    var phial2visibilityBind : ObservableField<Boolean> = ObservableField(false)

    var isTrackEnabledBind: ObservableField<Boolean> = ObservableField(false)

    //isQtyNormalMode or freezeQtyMode
    var isQtyNormalModeBind: ObservableField<Boolean> = ObservableField(true)

    //check is contains SPECIFIC_SKU
    var skuModeBind: ObservableField<SkuType> = ObservableField(SkuType.NORMAL)

    var errorText = "Some fields contain incorrect scans"

    var isPrefixExpected = false

    //for debug
    var debugTextBind : ObservableField<String>  = ObservableField("")

    lateinit var trackModel: TrackModel

    fun updateKitsList() {
        kitsCountBind.get()?.apply {
            isTrackEnabledBind.set(this != 0)

            while (kitsList.size < this) {
                kitsList.add(PhialModel("", ""))
            }
            while (kitsList.size > this) {
                kitsList.removeAt(kitsList.size - 1)
            }
        }

            when (skuModeBind.get()) {
                SkuType.NORMAL -> {
                    view?.requestFocusTrack()
                }
                SkuType.SPECIFIC_A, SkuType.SPECIFIC_B -> {
                    if (phialBind.get().isNullOrBlank() && kitsCountBind.get()!! > 0) {
                        view?.requestFocusPhial()
                    }
                }
                SkuType.SPECIFIC_C -> {
                    if (kitsCountBind.get()!! > 1) {
                        view?.requestFocusTrack()
                    } else if (kitsCountBind.get()!! == 1 && phialBind.get().isNullOrBlank()) {
                        view?.requestFocusPhial()
                    }
                }
            }

        var count = 0

        var itemsVMList: MutableList<BaseListItem> = mutableListOf()

        while (count < kitsList.size) {
            itemsVMList.add(ItemVM(context, view, kitsList[count], false, count))
            count += 1
        }

    }

    fun onQtyModeChanged(buttonView: CompoundButton, isChecked: Boolean) {
       if (isChecked) {
            view?.freezeQtyMode(kitsCountBind.get() ?: 0)
        }
        isQtyNormalModeBind.set(!isChecked)
    }

    fun clear() {
        kitsCountBind.get()?.apply {
            if (this > 0) {
                phialBind.set("")
                phial2Bind.set("")
                trackBind.set("")
                kitsList = mutableListOf()

                //updated only for normal mode, not for freeze mode
                if (isQtyNormalModeBind.get() == true) {
                    kitsCountBind.set(0)
                    updateKitsCount(0)
                } else {
                    if (skuModeBind.get() == SkuType.SPECIFIC_A) {
                        view?.requestFocusPhial()
                    } else if (skuModeBind.get() == SkuType.SPECIFIC_B) {
                        view?.requestFocusPhial()
                    } else
                        if (skuModeBind.get() == SkuType.SPECIFIC_C && this == 1) {
                        view?.requestFocusPhial()
                    }
                }

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
    fun clearTrack() {
        trackBind.set("")
        view?.requestFocusTrack()
    }

    private fun verificationError() {
        view?.showError(errorText, "Error")
    }

    fun verification() {
        val phialEan  = phialBind.get()?.removeEanSpaces() ?: ""
        val phial2Ean = phial2Bind.get()?.removeEanSpaces() ?: ""
        val trackEan  = trackBind.get()?.removeEanSpaces() ?: ""
        val kitsQty   = kitsCountBind.get() ?: 0

        errorText = "Some fields contain incorrect scans"

        if (kitsQty > 0 && trackEan.checkShippingTrackLabel()) {
            if (skuModeBind.get() != SkuType.NORMAL) {
                if (kitsQty == 1) {
                    if (phialEan.checkPhialItemNo()) {
                        onSubmit(kitsQty)
                    } else {
                        errorText = "Wrong value for Phial field"
                        verificationError()
                    }
                } else {
                    if (skuModeBind.get() == SkuType.SPECIFIC_C) {
                        onSubmit(kitsQty)
                    } else
                        if (
                            phialEan.checkPhialItemNo() &&
                            phial2Ean.checkPhialItemNo() &&
                            phialEan != phial2Ean
                        ) {
                            onSubmit(kitsQty)
                        } else {
                            errorText = "Phial fields contain incorrect scans"
                            verificationError()
                        }
                }
            } else {
                onSubmit(kitsQty)
            }
        } else {
            errorText = "Some fields contain incorrect scans"
            verificationError()
        }
    }

    private fun checkPrefix(ean: String): Boolean {
        if (isPrefixExpected) {
            getFilteredPrefixesList().first().prefixes.map {
                if (it.contains(ean.take(3), true))
                    return true
            }
            return false
        } else {
            return true
        }
    }

    private fun getFilteredPrefixesList(): List<PrefixModel> {
        var sku: String? = trackModel.itemNo
        var prefixesList: List<PrefixModel> = localRepo.getPrefixesList()
        prefixesList = prefixesList.filter {
            it.sku == sku
        }
        return prefixesList
    }

    private fun onSubmit(kitsQty: Int) {

        context.showDebugMessage("onSubmit")

        trackModel.testKitQty = kitsQty
        trackModel.trackingId = trackBind.get()

        kitsList = mutableListOf()
        var count = 0

        //normal
        var phialNo = trackBind.get()
        var phial2No = ""
        var returnTrack = "DU111111111MY"

        if (skuModeBind.get() == SkuType.NORMAL) {
            while (count < kitsQty) {
                phialNo = UUID.randomUUID().toString().substring(0,11)
                kitsList.add(PhialModel(phialNo!!, returnTrack))
                count++
            }
        } else {
            phialNo  = UUID.randomUUID().toString().substring(0,11)
            phial2No = UUID.randomUUID().toString().substring(0,11)

            if (phialBind.get()?.removeEanSpaces()?.length!! > 5)
                phialNo = phialBind.get()?.removeEanSpaces().toString()

            if (phial2Bind.get()?.removeEanSpaces()?.length!! > 5)
                phial2No = phial2Bind.get()?.removeEanSpaces().toString()

            kitsList.add(PhialModel(phialNo, phialNo))

            if (skuModeBind.get() == SkuType.SPECIFIC_C) {

                if (kitsQty > 1) {
                    while (count < kitsQty - 1) {
                        phialNo = UUID.randomUUID().toString().substring(0, 11)
                        kitsList.add(PhialModel(phialNo, phialNo + "01"))
                        count++
                    }
                }

            } else if (kitsQty == 2) {
                kitsList.add(PhialModel(phial2No, phial2No + "01"))
            }
        }

        trackModel.setPhialsList(kitsList)

        debugRequestBind.set("")
        debugResponseBind.set(" --- ")

        view?.onSubmitClick()
    }

    fun saveAsOffLine() {
        interactor.save(trackModel)
        clear()
        if (isQtyNormalModeBind.get() != false){
            updateKitsCount(0)
        }
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
                    debugResponseBind.set(this.toJson())
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
            if (isQtyNormalModeBind.get() != false){
                updateKitsCount(0)
            }
        } else {
            if (WITH_LOGS) {
                context.addResponseLog("$response")
            }

            context.showDebugMessage("error: $scans")
            view?.showError(scans, trackModel.itemNo!!)
        }

    }

    private fun updateKitsCountForSpecificAB(isPhial2visibility: Boolean){
        phial2visibilityBind.set(isPhial2visibility)
        if (!isPhial2visibility) phial2Bind.set("")
    }

    private fun updateKitsCountForSpecificC(isSinge: Boolean){
        phial1visibilityBind.set(isSinge)
        //Specific C should be that if your select Qty of Kits 1 - then one Phial line shows
        //Specific C if you select qty of kits 2, 3 or 4 then NO phial line is shown
        if (!isSinge) phialBind.set("")
    }

    fun updateKitsCount(count: Int){
        if (count == 0) {
            view?.clearQty()
        }
        kitsCountBind.set(count)

        //TODO refactoring is needed
        if (skuModeBind.get() != SkuType.NORMAL) {
            if (skuModeBind.get() == SkuType.SPECIFIC_C)
                updateKitsCountForSpecificC(count == 1)
            else
                updateKitsCountForSpecificAB(count == 2)
        }

        if (count == 1){
            //clear, in this mode we don't use phial2
            phial2Bind.set("")
        }
        updateKitsList()
    }

    val trackTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkShippingTrackLabel().let { isValid ->
                    if (isValid){
                        trackBind.set(ean)
                        updateKitsList()
                    } else {
                        trackBind.set("")
                        view?.onVibrate()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val phialTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkPhialItemNo().let { isValid ->
                    if (isValid){
                        if (!checkPrefix(ean)) {
                            var prefixes = ""
                            getFilteredPrefixesList().first().prefixes.map {
                                if (prefixes.isNotEmpty()) {
                                    prefixes = "$prefixes or "
                                }
                                prefixes += it
                            }

                            phialBind.set("")
                            errorText = context.getString(R.string.error_prefix, prefixes)
                            verificationError()
                        } else {
                            phialBind.set(ean)

                            if (kitsCountBind.get() == 2) {
                                view?.requestFocusPhial2()
                            } else {
                                view?.requestFocusTrack()
                            }
                        }

                    } else {
                        phialBind.set("")
                        view?.onVibrate()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val phial2TextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkPhialItemNo().let { isValid ->
                    if (isValid) {
                        if (!checkPrefix(ean)) {
                            var prefixes = ""
                            getFilteredPrefixesList().first().prefixes.map {
                                if (prefixes.isNotEmpty()) {
                                    prefixes = "$prefixes or "
                                }
                                prefixes += it
                            }

                            phial2Bind.set("")
                            errorText = context.getString(R.string.error_prefix, prefixes)
                            verificationError()
                        } else {
                            phial2Bind.set(ean)
                            view?.requestFocusTrack()
                        }
                    } else {
                        phial2Bind.set("")
                        view?.onVibrate()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    //init UI modes
    fun checkSKU() {
        when {
            localRepo.getSpecificListA().contains(trackModel.itemNo) -> {
                skuModeBind.set(SkuType.SPECIFIC_A)

                phial1visibilityBind.set(true)
                phial2visibilityBind.set(false)
            }
            localRepo.getSpecificListB().contains(trackModel.itemNo) -> {
                skuModeBind.set(SkuType.SPECIFIC_B)

                phial1visibilityBind.set(true)
                phial2visibilityBind.set(false)
            }
            localRepo.getSpecificListC().contains(trackModel.itemNo) -> {
                skuModeBind.set(SkuType.SPECIFIC_C)

                phial1visibilityBind.set(kitsCountBind.get() == 1)
                phial2visibilityBind.set(false)
            }
            else -> {
                skuModeBind.set(SkuType.NORMAL)

                phial1visibilityBind.set(false)
                phial2visibilityBind.set(false)
            }
        }
    }

    fun checkIfPrefixIsNeeded() {
        isPrefixExpected = !getFilteredPrefixesList().isNullOrEmpty()
    }


    //for debug
    fun updateDebugInfo() {
        var prefixes = ""
        if (isPrefixExpected) {
            getFilteredPrefixesList().first().let {
                prefixes = "${it.sku}\n${it.prefixes}"
            }
        }

        debugTextBind.set(skuModeBind.get()?.name + "\n" +
                "isPrefixExpected = $isPrefixExpected" + "\n" +
                prefixes
        )
    }

}