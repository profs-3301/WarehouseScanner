package com.goodswarehouse.scanner.presentation.screens.track.list

import android.content.Context
import androidx.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import com.goodswarehouse.scanner.App
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.data.track.bus.FocusBus
import com.goodswarehouse.scanner.data.track.bus.FocusEvent
import com.goodswarehouse.scanner.domain.model.PhialModel
import com.goodswarehouse.scanner.presentation.base.BaseListItem
import com.goodswarehouse.scanner.presentation.checkPhialItemNo
import com.goodswarehouse.scanner.presentation.checkReturnLabel
import com.goodswarehouse.scanner.presentation.removeEanSpaces
import com.goodswarehouse.scanner.presentation.screens.track.TrackView
import com.goodswarehouse.scanner.presentation.showDebugMessage
import io.reactivex.disposables.Disposable
import javax.inject.Inject

data class ItemVM(
    val context: Context,
    val view: TrackView?,
    val item: PhialModel,
    val inFocus: Boolean = false,
    val position: Int
) : BaseListItem {

    override var comparableId: String? = position.toString()
    override val layoutRes: Int = R.layout.item_kit

    var phialBind: ObservableField<String?> = ObservableField(item.phialNo)
    var rmLabelBind: ObservableField<String?> = ObservableField(item.returnTrack)

    val focusPhialBind: ObservableField<Boolean> = ObservableField(inFocus)
    val focusLabelBind: ObservableField<Boolean> = ObservableField(false)

    @Inject
    lateinit var focusBus: FocusBus
    private var disposable: Disposable? = null

    init {
        App.get().appComponent.inject(this)

        disposable = focusBus.getEvents().subscribe({ t ->
            if (position == t.position){
                context.showDebugMessage("getEvents: $position => ${t.position}")
                changeFocus(true)
            }
        }, { it.printStackTrace() })

    }

    fun clearFocus() {
        focusPhialBind.set(false)
        focusLabelBind.set(false)
    }
    fun changeFocus(setPhial: Boolean) {
        clearFocus()
        if (setPhial)
            focusPhialBind.set(true)
        else
            focusLabelBind.set(true)
    }

    fun clearPhial() {
        phialBind.set(" ")
        changeFocus(true)
    }

    fun clearLot() {
        rmLabelBind.set(" ")
        changeFocus(false)
    }

    val phialTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkPhialItemNo().let { isValid ->
                    if (isValid){
                        item.phialNo = ean
                        phialBind.set(ean)
                        changeFocus(false)
                    } else {
                        item.phialNo = " "
                        phialBind.set(" ")
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val rmLabelTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            s?.toString()?.removeEanSpaces()?.let { ean ->
                ean.checkReturnLabel().let { isValid ->
                    if (isValid){

                        if (item.returnTrack != ean) {

                            item.returnTrack = ean
                            rmLabelBind.set(ean)
                            clearFocus()

                            context.showDebugMessage("post: ${position+1} / $ean< ${ean.length}")
                            focusBus.post(FocusEvent(position+1))
                        } else {}

                    } else {
                        item.returnTrack = " "
                        rmLabelBind.set(" ")
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


}