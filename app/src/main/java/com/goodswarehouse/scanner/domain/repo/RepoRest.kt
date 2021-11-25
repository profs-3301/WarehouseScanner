package com.goodswarehouse.scanner.domain.repo

import com.goodswarehouse.scanner.domain.model.PalletModel
import com.goodswarehouse.scanner.domain.model.PalletResponse
import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.domain.model.TrackResponse
import io.reactivex.Observable

interface RepoRest {

    fun sendPallet(pallet : PalletModel)     : Observable<PalletResponse>
    fun sendTrack (scans  : TrackRequest): Observable<TrackResponse>


}