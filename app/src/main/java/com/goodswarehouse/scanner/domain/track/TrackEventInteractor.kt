package com.goodswarehouse.scanner.domain.track

import android.content.Context
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.data.connectivity.bus.NetworkStateBus
import com.goodswarehouse.scanner.data.di.TRACK_DATABASE
import com.goodswarehouse.scanner.data.di.Track
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.presentation.custom.onBackground
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class TrackEventInteractor @Inject constructor() {

    @Inject
    @Track(TRACK_DATABASE)
    lateinit var repoDb: TrackRepo

    @Inject
    lateinit var restRepo: RestRepo

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var busNetworkState: NetworkStateBus

    fun save(item: TrackModel) = onBackground {
        repoDb.save(item)
    }

    fun deleteAll() = onBackground {
        repoDb.deleteAll()
    }

    fun uploadOfflineScans(disposables: CompositeDisposable) {
        onBackground {
            val offlineList: List<TrackModel> = repoDb.getAll()
            if (offlineList.isEmpty())
                return@onBackground

            val track = TrackRequest(offlineList)

            restRepo.sendTrack(track)
                    .subscribe({
                        it?.apply {

                            repoDb.delete(this.scans.map {
                                it.trackingId
                            })

                        }
                    }, {
                        it.printStackTrace()
                    })
                    .toDisposables(disposables)

        }
    }

}