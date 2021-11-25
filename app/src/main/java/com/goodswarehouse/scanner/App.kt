package com.goodswarehouse.scanner

import androidx.room.Room
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.goodswarehouse.scanner.data.base.MainDatabase
import com.goodswarehouse.scanner.data.connectivity.NetworkStateReceiver
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.presentation.di.AppComponent
import com.goodswarehouse.scanner.presentation.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

class App : DaggerApplication() {

    companion object {
        @JvmStatic
        private lateinit var instance: App

        @JvmStatic
        fun getContext(): Context = instance

        @JvmStatic
        fun get(): App = getContext() as App

        val database by lazy { Room.databaseBuilder(getContext(), MainDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build() }

    }

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var localRepo: LocalRepo

    @Inject
    lateinit var restRepo: RestRepo

    init {
        instance = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build().apply {
            appComponent = this
            appComponent.inject(this@App)
        }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(NetworkStateReceiver(), IntentFilter(CONNECTIVITY_ACTION))
        registerErrorHandler()
    }

    private fun registerErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            if (it is ArrayIndexOutOfBoundsException && it.message?.contains("length=12; index=-1") == true) {
               Log.d("Log0", "\nlength=12 ERROR\n")
            }
        }
    }

}