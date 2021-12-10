package com.goodswarehouse.scanner.presentation

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.KeyEvent
import android.widget.EditText

@BindingAdapter("gainFocus")
fun gainFocus(editText: EditText, focus: Boolean) {
    if (focus)
        editText.requestFocusDelayed()
    else
        editText.clearFocus()
}

@BindingAdapter("isKeyboardEnabled")
fun isKeyboardEnabled(editText: EditText, status: Boolean) {
    editText.showSoftInputOnFocus = status
    if (status) {
        editText.showSoftKeyboard()
    }
}

interface OnKeyPressedListener {
    fun onKeyPressed()
}

@BindingAdapter ("scrollUp")
fun scrollUp (recyclerView: RecyclerView, position : Int) {
    recyclerView.smoothScrollToPosition(position)
}

@BindingAdapter("isScannerKeyDoneEnabled")
fun isScannerKeyDoneEnabled(editText: EditText, status: Boolean) {
    editText.setOnEditorActionListener { _, keyCode, event ->
        if (event == null)
            return@setOnEditorActionListener false

        if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_TV_INPUT) {
            return@setOnEditorActionListener !status //true for disable button
        }
        return@setOnEditorActionListener false
    }
}

