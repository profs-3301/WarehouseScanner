package com.goodswarehouse.scanner.domain.track

import android.content.Context
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.data.connectivity.bus.NetworkStateBus
import com.goodswarehouse.scanner.data.di.TRACK_DATABASE
import com.goodswarehouse.scanner.data.di.Track
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.model.Scan
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.presentation.custom.onBackground
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

import android.os.Bundle

import com.goodswarehouse.scanner.presentation.showDebugMessage
import org.jetbrains.anko.printManager


class TrackEventInteractor @Inject constructor() {

    @Inject
    @field:Track(TRACK_DATABASE)
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

            offlineList.map {
                try {
                    it.phials = it.phialsString.toObject()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }

            var track = TrackRequest(offlineList)
            try {
                context.addLog(track)
            } catch (e: Exception) {
                showDebugMessage("72 catch addLog(track)")
            }

            restRepo.sendTrack(track)
                    .subscribe({
                        it?.apply {

                            try {
                                if (WITH_LOGS) {
                                    val scans: List<Scan> = this.scans.filter { it.result != "0" }
                                    if (scans.isNotEmpty()) {
                                        context.addResponseLog("$this")
                                    }
                                }
                            } catch (e: Exception) {
                                showDebugMessage("87 catch addResponseLog")
                            }

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