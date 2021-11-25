package com.goodswarehouse.scanner

import android.content.Context
import android.widget.Toast
import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.presentation.custom.onUi
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val DEBUG_ROOT_FOLDER = "/storage/emulated/0/"
const val DEBUG_WORK_FOLDER = "$DEBUG_ROOT_FOLDER!Scanner"

private fun getDebugLog(track: TrackRequest): String {
    return  "trackingId: ${track.scans[0].trackingId} testKitQty: ${track.scans[0].testKitQty} " +
            if (track.scans.size == 1) {""} else {"count: ${track.scans.size}\n"} +
            "requestGuid: \n  ${track.requestGuid}" +
            "${track.scans.map { it.toJson() }}\n\n"
                    .replace("\"phials\":[],\"phialsString\":\"\",", "")
                    .replace("{\"deviceId\"", "\n  {\"deviceId\"")
                    .replace("\"trackingId\"", "\n\"trackingId\"")
                    .replace("}, {", "\n   },{")
}

fun Context.addResponseLog(response: String) {
    try {
        val fileName = getCurrentDate()

        if (!File(DEBUG_WORK_FOLDER).exists()) {
            File(DEBUG_WORK_FOLDER).mkdir()
        }

        val file = File(DEBUG_WORK_FOLDER, "${fileName}_response.txt")

        if (!file.exists()) {
            file.createNewFile()
        }

        val fw = FileWriter(file, true)
        fw.write("$response\n\n")
        fw.close()

    } catch (e: IOException) {
        e.printStackTrace()
        onUi {
            Toast.makeText(this, "DEBUG response ERROR: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

fun Context.addLog(track: TrackRequest) {

    val trackDebug = track
    trackDebug.scans.map {
//        it.phials = listOf()  //clear for debug
        it.phialsString = ""
    }
    val log = getDebugLog(trackDebug)

    var type = if (track.scans.size == 1) {""} else {" after offline"}

    try {
        val fileName = getCurrentDate()

        if (!File(DEBUG_WORK_FOLDER).exists()) {
            File(DEBUG_WORK_FOLDER).mkdir()
        }

        val file = File(DEBUG_WORK_FOLDER, "$fileName$type.txt")

        if (!file.exists()) {
            file.createNewFile()
        }

        val fw = FileWriter(file, true)
        fw.write("$log")
        fw.close()

    } catch (e: IOException) {
        e.printStackTrace()
       onUi {
           Toast.makeText(this, "DEBUG ERROR: ${e.message}", Toast.LENGTH_LONG).show()
       }
    }
}

private fun getCurrentDate(): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date())
}