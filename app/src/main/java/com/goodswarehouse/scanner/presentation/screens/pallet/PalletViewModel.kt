package com.goodswarehouse.scanner.presentation.screens.pallet

import android.content.Context
import androidx.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.model.PalletModel
import com.goodswarehouse.scanner.domain.model.PalletResponse
import com.goodswarehouse.scanner.domain.model.QuarantinedPallets
import com.goodswarehouse.scanner.presentation.*
import com.goodswarehouse.scanner.presentation.base.BaseViewModel
import javax.inject.Inject

class PalletViewModel @Inject constructor(val context: Context) : BaseViewModel<PalletView>() {

    @Inject
    lateinit var localRepo: LocalRepo
    @Inject
    lateinit var restRepo: RestRepo

    var palletBind: ObservableField<String> = ObservableField(" ")
    var lotBind   : ObservableField<String> = ObservableField(" ")
    var itemBind  : ObservableField<String> = ObservableField(" ")

    var palletEan : String = " "
    var lotEan    : String = " "
    var itemEan   : String = " "

    fun clear() {
        palletEan = ""
        lotEan = ""
        itemEan = ""

        palletBind.set(" ")
        lotBind.set(" ")
        itemBind.set(" ")

        view?.requestFocusPallet()
    }

    fun testOpenTrack() {
        view?.submit("N-CTK-norm001", "11-lotNoN-000000-A00", "CTK-TEST-NORMAL", false)
        clear()
    }

    fun testOpenSpecificA() {
        view?.submit("A-CTK-aaaa001", "11-lotNoA-000000-A00", SPECIFIC_A_SKU_LIST.first(), false)
        clear()
    }
    fun testOpenSpecificB() {
        view?.submit("B-CTK-bbbb001", "11-lotNoB-000000-A00", SPECIFIC_B_SKU_LIST.first(), false)
        clear()
    }
    fun testOpenSpecificC() {
        view?.submit("C-CTK-cccc001", "11-lotNoC-000000-A00", SPECIFIC_C_SKU_LIST.first(), false)
        clear()
    }
    fun testOpenSpecificD() {
        view?.submit("D-CTK-dddd001", "11-lotNoD-000000-A00", SPECIFIC_D_SKU_LIST.first(), false)
        clear()
    }

    fun verification() {
        if (palletBind.get().checkPalletNo() && lotBind.get().checkLotNo()) {
            if (itemBind.get().checkItemNo(palletBind.get(), lotBind.get()))
                view?.onSubmitClick()
        } else {
            context.vibrate()
            Toast.makeText(context, "Some fields contain incorrect scans", Toast.LENGTH_LONG).show()
        }
    }

    fun saveAsOffLine() {
        view?.submit(palletEan, lotEan, itemEan, true)
        clear()
    }

    fun sendRequest() {
        restRepo.sendPallet(PalletModel(palletEan, lotEan, itemEan))
            .default { handleCommonErrors(it) }
            .defaultActions(view)
            .subscribe({
                it?.apply {
                    onSuccessfulResponse(this)
                }
            }, {
                context.showDebugMessage("ERROR: ${it.message}")
            })
            .toDisposables(disposables)
    }

    private fun onSuccessfulResponse(response: PalletResponse) {
        context.showDebugMessage(response.toString())

        val quarantinedList: List<QuarantinedPallets> = response.quarantinedPallets
        if (quarantinedList.isEmpty()) {
            view?.submit(palletEan, lotEan, itemEan, false)
            clear()
        } else {
            context.showDebugMessage("$quarantinedList")
            view?.showError("$quarantinedList", "Quarantined:")
        }
    }

    fun clearPallet() {
        palletEan = ""
        palletBind.set(palletEan)
        view?.requestFocusPallet()
    }

    fun clearLot() {
        lotEan = ""
        lotBind.set(lotEan)
        view?.requestFocusLot()
    }

    fun clearItem() {
        itemEan = ""
        itemBind.set(lotEan)
        view?.requestFocusItem()
    }


    ///
    val palletTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkPalletNo().let { isValid ->
                   if (isValid){
                       palletEan = ean
                       palletBind.set(ean)
                       view?.requestFocusLot()
                   } else {
                       palletEan = " "
                       palletBind.set(" ")
                   }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val lotTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkLotNo().let { isValid ->
                    if (isValid){
                        lotEan = ean
                        lotBind.set(ean)
                        view?.requestFocusItem()
                    } else {
                        lotEan = " "
                        lotBind.set(" ")
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val itemTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkItemNo(palletEan, lotEan).let { isValid ->
                    if (isValid){
                        itemEan = ean
                        itemBind.set(ean)
                    } else {
                        itemEan = " "
                        itemBind.set(" ")
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}