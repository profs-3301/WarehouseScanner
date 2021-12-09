package com.goodswarehouse.scanner.data.di

import com.goodswarehouse.scanner.data.connectivity.NetworkStateReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DataInjectionsModule{

    @ContributesAndroidInjector
    abstract fun injectNetworkStateReceiver(): NetworkStateReceiver

}