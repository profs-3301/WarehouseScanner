package com.goodswarehouse.scanner.domain.model

data class TrackResponse(
    val requestGuid: String,
    val responseGuid: String,
    val scans: List<Scan>
)

data class Scan(
    val message: String,
    val result: String,
    val trackingId: String
)