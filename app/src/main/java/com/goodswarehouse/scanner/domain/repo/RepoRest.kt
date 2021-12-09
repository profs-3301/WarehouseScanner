package com.goodswarehouse.scanner.domain.repo

import com.goodswarehouse.scanner.domain.model.TrackRequest
import com.goodswarehouse.scanner.domain.model.TrackResponse
import io.reactivex.Observable

interface RepoRest {

    fun sendTrack (scans  : TrackRequest): Observable<TrackResponse>

}