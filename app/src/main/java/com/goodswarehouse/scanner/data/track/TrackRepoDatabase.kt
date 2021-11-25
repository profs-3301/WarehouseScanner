package com.goodswarehouse.scanner.data.track

import javax.inject.Inject
import com.goodswarehouse.scanner.data.base.BaseRepo
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.domain.track.TrackRepo

class TrackRepoDatabase @Inject constructor() : BaseRepo(), TrackRepo {

    override fun getAll(): List<TrackModel> =
        database.track().getAll()


    override fun save(item: TrackModel) {
        database.track().save(item)
    }

    override fun delete(ids: List<String>) {
        database.track().delete(ids)
    }

    override fun deleteAll() {
        database.track().deleteAll()
    }

}