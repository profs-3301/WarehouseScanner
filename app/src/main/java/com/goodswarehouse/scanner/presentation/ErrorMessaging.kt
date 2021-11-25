package com.goodswarehouse.scanner.presentation

import android.content.Context
import com.goodswarehouse.scanner.domain.model.Scan

fun Context.showTrackAlertDialog(scans: List<Scan>, sku: String) {
    val scan = scans[0]
    val resultCode = scan.result.toInt()

    var header = "Error"
    var message = "Something went wrong"

    when (resultCode) {
        1 -> {
            header = "ERROR 1 >> = DUPLICATE"
            message = "the RM SHIPPING Track Label " +
                    "\n${scan.trackingId} has been scanned already\n" +
                    "– inform your manager and check " +
                    "if you have more duplicate labels"
        }
        2 -> {
            header = "ERROR 2 >> = TRACKING No. NOT FOUND "
            message = "the RM Track Label " +
                    "\n${scan.trackingId} is not recognised as an Order for despatch today" +
                    " – inform your manager for investigation and instruction"
        }

        3 -> {
            header = "ERROR 3 >>> DUPLICATE PHIAL SCAN"
            message = "the Phial Item No.s have already been scanned - please raise this with your manager"
        }
        4 -> {
            header = "ERROR 4 >>>  DUPLICATE RM RETURN LABEL"
            message = "the RM Return Label has already been scanned - please raise this with your manager "
        }

        5 -> {
            header = "ERROR 5 >> INVALID SKU or PACK QTY "
            message = "the Item SKU $sku is not recognised OR the Pack Qty does not match for the RM Track Label " +
                    "\n${scan.trackingId} – check for the correct Item SKU " +
                    "OR the Pack Qty on the RM Tracking Label for this order"
        }
        6 -> {
            header = "ERROR 6 >> INCORRECT SKU or KIT QTY"
            message = "the Item SKU $sku OR selected KIT Qty is not valid for the RM Track Label " +
                    "\n${scan.trackingId} – check the RM Tracking Label for the correct Item SKU and Kit Qty for this order"
        }
        else -> { // Note the block
            header = "ERROR >> QUARANTINED STOCK "
            message = "The Pallet and Lot No. has been " +
                    "quarantined and cannot be used for " +
                    "order fulfilment\n– inform your manager for investigation and instruction"
        }
    }

    this.showAlertDialog(message, header)

}