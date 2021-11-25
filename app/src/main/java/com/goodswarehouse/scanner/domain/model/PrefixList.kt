package com.goodswarehouse.scanner.domain.model

import com.google.gson.annotations.SerializedName

class PrefixList {
    @SerializedName("list") var list : List<PrefixModel> = listOf()
}