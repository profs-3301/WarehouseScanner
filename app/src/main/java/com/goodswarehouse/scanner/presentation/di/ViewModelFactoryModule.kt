package com.goodswarehouse.scanner.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goodswarehouse.scanner.presentation.base.MainViewModel
import com.goodswarehouse.scanner.presentation.screens.acceptance.AcceptanceViewModel
import com.goodswarehouse.scanner.presentation.screens.action.ActionViewModel
import com.goodswarehouse.scanner.presentation.screens.departure.DepartureViewModel
import com.goodswarehouse.scanner.presentation.screens.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(myViewModel: MainViewModel): ViewModel

    //
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(myViewModel: LoginViewModel): ViewModel

    //
    @Binds
    @IntoMap
    @ViewModelKey(ActionViewModel::class)
    internal abstract fun bindActionViewModel(myViewModel: ActionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AcceptanceViewModel::class)
    internal abstract fun bindAcceptanceViewModel(myViewModel: AcceptanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DepartureViewModel::class)
    internal abstract fun bindDepartureViewModel(myViewModel: DepartureViewModel): ViewModel

}