package com.goodswarehouse.scanner.domain.repo

import com.goodswarehouse.scanner.domain.model.PrefixList
import com.goodswarehouse.scanner.domain.model.PrefixModel

interface RepoLocal {

    fun getDeviceId(): String?
    fun saveDeviceId(id: String)

    fun clearDeviceId()

    fun clearAll()

    fun getSpecificA(): String?
    fun getSpecificListA(): List<String>
    fun saveSpecificListA(list: String)

    fun getSpecificB(): String?
    fun getSpecificListB(): List<String>
    fun saveSpecificListB(list: String)

    fun getSpecificC(): String?
    fun getSpecificListC(): List<String>
    fun saveSpecificListC(list: String)

    fun getSpecificD(): String?
    fun getSpecificListD(): List<String>
    fun saveSpecificListD(list: String)

    fun getPrefixesList(): List<PrefixModel>
    fun savePrefixesList(list: String)


}