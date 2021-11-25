package com.goodswarehouse.scanner.data.base

import com.goodswarehouse.scanner.App
import com.goodswarehouse.scanner.data.di.REST_SERVER
import com.goodswarehouse.scanner.data.di.Rest
import retrofit2.Retrofit
import javax.inject.Inject

open class BaseRepo {

    @Inject
    @field:Rest(REST_SERVER)
    lateinit var retrofit: Retrofit

    val database by lazy { App.database }

    inline fun <reified T> provideRestService(): T = retrofit.create(T::class.java)

}