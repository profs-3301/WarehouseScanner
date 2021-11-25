package com.goodswarehouse.scanner.data.repo

import android.content.SharedPreferences
import com.goodswarehouse.scanner.data.base.BaseRepo
import com.goodswarehouse.scanner.domain.model.*
import com.goodswarehouse.scanner.domain.repo.RepoRest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RestRepo @Inject constructor() : BaseRepo(), RepoRest {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun sendPallet(pallet : PalletModel): Observable<PalletResponse> =
        provideRestService<RestService>()
            .sendPallet(
                PalletRequest(
                    listOf(
                        pallet
                    )
                )
            )

    override fun sendTrack(track: TrackRequest): Observable<TrackResponse> =
        provideRestService<RestService>()
            .sendTrack(
                track
            )

    interface RestService {
        @POST("nhsPalletScan")
        fun sendPallet(
            @Body body: PalletRequest
        ): Observable<PalletResponse>

        @POST("nhsPackScan")
        fun sendTrack(
            @Body body: TrackRequest
        ): Observable<TrackResponse>
    }

}