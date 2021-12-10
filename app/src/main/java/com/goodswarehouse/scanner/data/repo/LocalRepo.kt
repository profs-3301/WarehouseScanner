package com.goodswarehouse.scanner.data.repo

import android.content.SharedPreferences
import com.goodswarehouse.scanner.PREF_DEVICE_ID
import com.goodswarehouse.scanner.domain.repo.RepoLocal
import com.goodswarehouse.scanner.presentation.clear
import com.goodswarehouse.scanner.putString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepo @Inject constructor(): RepoLocal {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun getDeviceId(): String? = prefs.getString(PREF_DEVICE_ID, null)

    override fun saveDeviceId(DeviceId: String) {
        prefs.putString(PREF_DEVICE_ID, DeviceId)
    }

    override fun clearDeviceId() {
        prefs.putString(PREF_DEVICE_ID, "")
    }

    override fun clearAll() {
        clearDeviceId()
        prefs.clear()
    }
}