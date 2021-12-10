package com.goodswarehouse.scanner.data.connectivity

import android.content.Context
import android.content.Intent
import com.goodswarehouse.scanner.data.connectivity.bus.NetworkStateBus
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

class NetworkStateReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var bus: NetworkStateBus

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        bus.post(context.networkState)
    }

}