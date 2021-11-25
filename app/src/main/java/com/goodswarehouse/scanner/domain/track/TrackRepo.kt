package com.goodswarehouse.scanner.domain.track

import com.goodswarehouse.scanner.domain.model.TrackModel

interface TrackRepo {

    fun getAll(): List<TrackModel>

    fun save(item: TrackModel)

    fun delete(ids: List<String>)
    fun deleteAll()

}