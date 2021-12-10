package com.goodswarehouse.scanner.presentation.di

import com.goodswarehouse.scanner.presentation.base.BaseActivity
import com.goodswarehouse.scanner.presentation.screens.acceptance.AcceptanceFragment
import com.goodswarehouse.scanner.presentation.screens.action.ActionFragment
import com.goodswarehouse.scanner.presentation.screens.departure.DepartureFragment
import com.goodswarehouse.scanner.presentation.screens.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiInjectionsModule {

    @ContributesAndroidInjector
    abstract fun injectMain(): BaseActivity

    //    FRAGMENTS
    @ContributesAndroidInjector
    abstract fun injectLoginFragment(): LoginFragment

    //
    @ContributesAndroidInjector
    abstract fun injectActionFragment(): ActionFragment

    @ContributesAndroidInjector
    abstract fun injectAcceptanceFragment(): AcceptanceFragment

    @ContributesAndroidInjector
    abstract fun injectDepartureFragment(): DepartureFragment
}