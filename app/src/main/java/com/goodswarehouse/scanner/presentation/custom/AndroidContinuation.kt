package com.goodswarehouse.scanner.presentation.custom

import kotlinx.coroutines.*


fun onUi(closure: () -> Unit) = GlobalScope.launch (Dispatchers.Main) {
    closure()
}

fun onBackground(closure: () -> Unit) = GlobalScope.launch (Dispatchers.IO) {
    closure()
}

fun <T> onImmediate(closure: () -> T?): T? = runBlocking {
    withContext(Dispatchers.IO) {
        closure()
    }
}

