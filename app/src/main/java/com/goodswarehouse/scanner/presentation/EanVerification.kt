package com.goodswarehouse.scanner.presentation

fun String.removeEanSpaces(): String =
    this
        .replace(" ", "")
        .replace("\\n", "")
        .replace("\\s", "")
        .replace("#", "")