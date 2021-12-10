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
class TrackRequest(
    var scans: List<TrackModel> = listOf()
) : Parcelable {
    var requestGuid: String = UUID.randomUUID().toString()
}

@Entity(tableName = TABLE_TRACK)
@Parcelize
class TrackModel() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @SerializedName("itemNo")
    var itemNo: String? = ""

    @SerializedName("boxNo")
    var boxNo: String? = ""

    @SerializedName("productCnt")
    var productCnt: String? = ""

    @SerializedName("shopNo")
    var shopNo: String? = ""
}
