package com.goodswarehouse.scanner.data.repo

import android.content.SharedPreferences
import com.goodswarehouse.scanner.*
import com.goodswarehouse.scanner.domain.model.PrefixList
import com.goodswarehouse.scanner.domain.model.PrefixModel
import com.goodswarehouse.scanner.domain.repo.RepoLocal
import com.goodswarehouse.scanner.presentation.clear
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepo @Inject constructor(): RepoLocal {

    @Inject
    lateinit var prefs: SharedPreferences

//    getDeviceId
    override fun getDeviceId(): String? = prefs.getString(PREF_DEVICE_ID, null)

    override fun saveDeviceId(DeviceId: String) {
        prefs.putString(PREF_DEVICE_ID, DeviceId)
    }

    override fun clearDeviceId() {
        prefs.putString(PREF_DEVICE_ID, "")
    }

//    getSpecific
    override fun getSpecificA(): String? = prefs.getString(SPECIFIC_A, "")
    override fun getSpecificB(): String? = prefs.getString(SPECIFIC_B, "")
    override fun getSpecificC(): String? = prefs.getString(SPECIFIC_C, "")
    override fun getSpecificD(): String? = prefs.getString(SPECIFIC_D, "")


    override fun getSpecificListA(): List<String> {
        getSpecificA()?.apply {
            return this.toObject()
        }
        return listOf()
    }
    override fun getSpecificListB(): List<String> {
        getSpecificB()?.apply {
            return this.toObject()
        }
        return listOf()
    }
    override fun getSpecificListC(): List<String> {
        getSpecificC()?.apply {
            return this.toObject()
        }
        return listOf()
    }
    override fun getSpecificListD(): List<String> {
        getSpecificD()?.apply {
            return this.toObject()
        }
        return listOf()
    }

    override fun saveSpecificListA(list: String) { prefs.putString(SPECIFIC_A, list) }
    override fun saveSpecificListB(list: String) { prefs.putString(SPECIFIC_B, list) }
    override fun saveSpecificListC(list: String) { prefs.putString(SPECIFIC_C, list) }
    override fun saveSpecificListD(list: String) { prefs.putString(SPECIFIC_D, list) }

    ///prefix

    private fun getPrefixes(): String? = prefs.getString(PREFIXES, "")
    override fun getPrefixesList(): List<PrefixModel> {
        getPrefixes()?.apply {
            return this.toObject<PrefixList>().list
        }
        return listOf()
    }

    override fun savePrefixesList(list: String) { prefs.putString(PREFIXES, list) }

    override fun clearAll() {
        clearDeviceId()
        prefs.clear()
    }
}