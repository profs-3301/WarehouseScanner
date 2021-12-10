package com.goodswarehouse.scanner.presentation.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.goodswarehouse.scanner.BuildConfig
import com.goodswarehouse.scanner.R
import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.domain.model.TrackModel
import com.goodswarehouse.scanner.presentation.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class BaseActivity : DaggerAppCompatActivity(), BaseView, Bindable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var localRepo: LocalRepo

    @Inject
    lateinit var mainViewModel: MainViewModel

    override var binding: ViewDataBinding? = null

    private val REQUEST_CODE = 101

    var trackModel: TrackModel = TrackModel()

    var isPhoneOnline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        beforeViewInit()
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        bind(getLayoutResource(), mainViewModel)
        mainViewModel.view = this
        mainViewModel.listenAllTheChanges()

        viewInit()

        mainViewModel.appVersionBind.set(getString(R.string.app_version, BuildConfig.VERSION_CODE, getString(R.string.versionSuffix)))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this, R.id.content).navigateUp()

    private fun getLayoutResource(): Int = R.layout.activity_base

    override fun showError(message: String, title: String) = showAlertDialog(message, title)

    override fun showMessage(message: String, title: String?): Unit? = showSnackBar(message)

    override fun onVibrate() = vibrate(500)

    override fun showSnackBar(message: String?): Unit? = binding?.root?.run { if (message != null) Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).show() }

    override fun showProgress() {
        mainViewModel.progress.set(true)
    }

    override fun hideProgress() {
        mainViewModel.progress.set(false)
    }

    override fun updateNetworkState(online: Boolean) {
        isPhoneOnline = online
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceId() {
        permissionCheck(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            if (this) {
                var imei = "imei"
                //does not work for some devices
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        imei = Build.getSerial()
                    }
                } catch (e: Exception){}

                mainViewModel.deviceIdBind.set(imei)
                localRepo.saveDeviceId(imei)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        showDebugMessage("onSaveInstanceState: ${oldInstanceState.isEmpty}")
        oldInstanceState.clear()
    }

}
