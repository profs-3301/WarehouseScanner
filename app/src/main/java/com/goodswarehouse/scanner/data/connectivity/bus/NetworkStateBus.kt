package com.goodswarehouse.scanner.data.connectivity.bus

import com.goodswarehouse.scanner.data.connectivity.NetworkState
import com.goodswarehouse.scanner.data.util.RxBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateBus @Inject constructor(): RxBus<NetworkState>()