package com.goodswarehouse.scanner.data.connectivity;

import android.content.Context
import android.net.ConnectivityManager
import com.goodswarehouse.scanner.invoke

enum class NetworkState(val isOnline: Boolean) {
    ONLINE(true),
    OFFLINE(false)
}

val Context.isOnline: Boolean
    get() = networkState.isOnline

val Context.networkState: NetworkState
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnectedOrConnecting?.run {
        this({
            NetworkState.ONLINE
        }, {
            NetworkState.OFFLINE
        })
    } ?: NetworkState.OFFLINE