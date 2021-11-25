package com.goodswarehouse.scanner.domain.model

data class PalletResponse(
    val requestGuid: String,
    val responseGuid: String,
    val quarantinedPallets: List<QuarantinedPallets>
)

data class QuarantinedPallets(
    var palletNo: String,
    var LotNo: String,
    val quarantineDate: String
)