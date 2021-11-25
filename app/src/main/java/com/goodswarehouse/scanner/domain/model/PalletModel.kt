package com.goodswarehouse.scanner.domain.model

import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

class PalletRequest(
    var pallets: List<PalletModel> = listOf()
) {
    var requestGuid: String = UUID.randomUUID().toString()
}

@Parcelize
class PalletModel(
    var palletNo: String,
    var lotNo: String,

    @SerializedName("SKU")
    var itemNo: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}