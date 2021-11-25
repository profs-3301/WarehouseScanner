package com.goodswarehouse.scanner.domain.appevent

import com.goodswarehouse.scanner.data.connectivity.bus.NetworkStateBus
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppEventInteractor  @Inject constructor(){

    @Inject
    lateinit var busNetworkState: NetworkStateBus

    @Inject
    lateinit var localRepo: LocalRepo

    @Inject
    lateinit var restRepo: RestRepo


}