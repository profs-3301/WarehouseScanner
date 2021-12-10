package com.goodswarehouse.scanner.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DaggerViewModelFactory @Inject
constructor(private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            (viewModelsMap[modelClass]
                ?: viewModelsMap.toList().find { modelClass.isAssignableFrom(it.first) }?.second)?.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}