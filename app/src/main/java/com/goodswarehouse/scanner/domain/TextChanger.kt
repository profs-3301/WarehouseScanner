package com.goodswarehouse.scanner.domain

import android.text.Editable
import android.text.TextWatcher
import com.goodswarehouse.scanner.presentation.removeEanSpaces

open class TextChanger : TextWatcher {
    var ean: String = ""

    override fun afterTextChanged(s: Editable?) {
        ean = ""
        s?.toString()?.removeEanSpaces()?.apply {
            ean = this
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}