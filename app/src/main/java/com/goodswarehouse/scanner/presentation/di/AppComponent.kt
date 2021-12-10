package com.goodswarehouse.scanner.presentation.di

import android.app.Application
import com.goodswarehouse.scanner.App
import com.goodswarehouse.scanner.data.di.BaseModule
import com.goodswarehouse.scanner.data.di.DataInjectionsModule
import com.goodswarehouse.scanner.data.di.RepoBindModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        BaseModule::class,
        UiInjectionsModule::class,
        RepoBindModule::class,
        DataInjectionsModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
