package com.goodswarehouse.scanner.domain.model

import com.google.gson.annotations.SerializedName

class PrefixModel {

    @SerializedName("sku") var sku : String = ""
    @SerializedName("prefixes") var prefixes : List<String> = listOf()

}