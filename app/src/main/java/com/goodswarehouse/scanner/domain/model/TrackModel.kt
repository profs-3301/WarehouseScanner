package com.goodswarehouse.scanner.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.goodswarehouse.scanner.TABLE_TRACK
import com.goodswarehouse.scanner.toJson
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
class TrackRequest (
    var scans: List<TrackModel>
) : Parcelable {
    var requestGuid: String = UUID.randomUUID().toString()
}

@Entity(tableName = TABLE_TRACK)
@Parcelize
class TrackModel (

) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var palletNo: String? = null
    var lotNo: String? = null

    @SerializedName("sku")
    var itemNo: String? = null

    var testKitQty: Int? = null

    //RM SHIPPING Track Label
    var trackingId: String? = null

    @Ignore
    var phials: List<PhialModel> = listOf()

    @Expose(serialize = false)
    var phialsString : String = ""

    var user: String? = "noName"
    var deviceId: String = "DEVICEID-1234"
    var scanDate: String = getCurrentDate()

    private fun getCurrentDate(): String {
//      "2020-10-28T 10:42:46.202Z"
        val sdf = SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss.SSS'Z'", Locale.UK)
        return sdf.format(Date())
    }

    fun setPhialsList(list: MutableList<PhialModel>) {
        phials =  list
        phialsString =  list.toJson()

        scanDate = getCurrentDate()
    }

}

@Parcelize
class PhialModel(
    var phialNo: String,
    var returnTrack: String
) : Parcelable {
}


