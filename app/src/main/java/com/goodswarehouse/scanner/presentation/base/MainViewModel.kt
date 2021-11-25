package com.goodswarehouse.scanner.presentation.base

import android.content.Context
import androidx.databinding.ObservableField
import com.goodswarehouse.scanner.data.connectivity.bus.NetworkStateBus
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.domain.track.TrackEventInteractor
import com.goodswarehouse.scanner.io
import com.goodswarehouse.scanner.toDisposables
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor() : BaseViewModel<BaseView>() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var localRepo: LocalRepo

    @Inject
    lateinit var restRepo: RestRepo

    val progress: ObservableField<Boolean> = ObservableField()

    @Inject
    lateinit var trackEventInteractor: TrackEventInteractor

    @Inject
    lateinit var busNetworkState: NetworkStateBus

    var appVersionBind: ObservableField<String> = ObservableField("")
    var deviceIdBind: ObservableField<String?> = ObservableField("")
    var isPhoneOnlineBind: ObservableField<Boolean?> = ObservableField(false)

    fun listenAllTheChanges() = subscribeNetworkStateBus(disposables).toDisposables(disposables)

    private fun subscribeNetworkStateBus(disposables: CompositeDisposable) =
            busNetworkState.getEvents().io()
                    .subscribe(
                            {
                                isPhoneOnlineBind.set(it.isOnline)
                                view?.updateNetworkState(it.isOnline)

                                if (it.isOnline){
                                    trackEventInteractor.uploadOfflineScans(disposables)
                                }
                            },
                            {
                                it.printStackTrace()
                            }
                    )

}