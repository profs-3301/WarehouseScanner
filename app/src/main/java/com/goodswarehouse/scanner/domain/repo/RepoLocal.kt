package com.goodswarehouse.scanner.domain.repo

interface RepoLocal {

    fun getDeviceId(): String?
    fun saveDeviceId(id: String)

    fun clearDeviceId()

    fun clearAll()

}