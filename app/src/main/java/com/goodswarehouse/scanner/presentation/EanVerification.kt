package com.goodswarehouse.scanner.presentation

import android.util.Log

//Format of a Pallet Label No. is "P-CTK-0000001"
fun String?.checkPalletNo(): Boolean {
    this?.removeEanSpaces()?.let {
        return (it.length == 13 && (it.getMatches("-") == 2))
    }
    return false
}

//Format of a Lot No. is "11-310121-271120-A00"
fun String?.checkLotNo(): Boolean {
    this?.removeEanSpaces()?.let {
        return (it.length == 20 && (it.getMatches("-") == 3))
    }
    return false
}

fun String?.checkItemNo(pallet: String?, lot: String?): Boolean {
    if (this.isNullOrBlank() || pallet.isNullOrBlank() || lot.isNullOrBlank())
        return false
    this.removeEanSpaces().let {
        return (it.length in 4..22 && it != pallet && it != lot)
    }
}

/*TRACK SCREEN*/

//trackingId
//the RM SHIPPING Track Label
//13 length
//ZD515128263GB
fun String?.checkShippingTrackLabel(): Boolean {
    this?.removeEanSpaces()?.let { return (it.length == 13 && (it.getMatches("-") == 0)) }
    return false
}

//Phial Item No. scan
//11 length
//AAD18173415
fun String?.checkPhialItemNo(): Boolean {
    this?.removeEanSpaces()?.let { return (it.length == 11 && (it.getMatches("-") == 0)) }
    return false
}

//RM Return Label scan
//13 length
//ZD515128282GB
fun String?.checkReturnLabel(): Boolean {
    this?.removeEanSpaces()?.let { return (it.length == 13 && (it.getMatches("-") == 0)) }
    return false
}

fun String.getMatches(search: String): Int =
    this.removeEanSpaces().let { return it.split(search).size - 1; }

fun String.removeEanSpaces(): String =
    this
        .replace(" ", "")
        .replace("\\n", "")
        .replace("\\s", "")
        .replace("#", "")