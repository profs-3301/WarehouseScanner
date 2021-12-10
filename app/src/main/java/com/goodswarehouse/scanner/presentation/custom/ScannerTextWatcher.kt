package com.goodswarehouse.scanner.presentation.custom

import android.text.Editable
import android.text.TextWatcher
import com.goodswarehouse.scanner.presentation.printDebugLog
import com.goodswarehouse.scanner.presentation.removeEanSpaces

open class ScannerTextWatcher(val closure: (String) -> Unit) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        s?.toString()?.removeEanSpaces()?.let { ean ->
            if (ean.isNotBlank()) {
                ean.printDebugLog()
                closure(ean)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

