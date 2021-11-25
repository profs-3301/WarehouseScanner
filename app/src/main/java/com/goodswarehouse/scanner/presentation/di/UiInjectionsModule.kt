package com.goodswarehouse.scanner.presentation.di

import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.screens.acceptance.AcceptanceFragment
import com.goodswarehouse.scanner.presentation.screens.action.ActionFragment
import com.goodswarehouse.scanner.presentation.screens.departure.DepartureFragment
import com.goodswarehouse.scanner.presentation.screens.login.LoginFragment
import com.goodswarehouse.scanner.presentation.screens.pallet.PalletFragment
import com.goodswarehouse.scanner.presentation.screens.track.TrackFragment
import com.goodswarehouse.scanner.presentation.screens.track.d.TrackDFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiInjectionsModule {

    @ContributesAndroidInjector
    abstract fun injectMain(): BaseActivity

    //    FRAGMENTS
    @ContributesAndroidInjector
    abstract fun injectLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun injectPalletFragment(): PalletFragment

    @ContributesAndroidInjector
    abstract fun injectTrackFragment(): TrackFragment

    @ContributesAndroidInjector
    abstract fun injectTrackDFragment(): TrackDFragment

    //
    @ContributesAndroidInjector
    abstract fun injectActionFragment(): ActionFragment

    @ContributesAndroidInjector
    abstract fun injectAcceptanceFragment(): AcceptanceFragment

    @ContributesAndroidInjector
    abstract fun injectDepartureFragment(): DepartureFragment
}