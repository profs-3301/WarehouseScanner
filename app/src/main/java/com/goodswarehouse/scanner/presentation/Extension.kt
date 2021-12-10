@file:Suppress("NOTHING_TO_INLINE")

package com.goodswarehouse.scanner.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goodswarehouse.scanner.BuildConfig
import com.goodswarehouse.scanner.presentation.custom.onUi
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.longToast

fun Fragment.navigate(@IdRes action: Int, @IdRes fragmentId: Int, vararg extras: Pair<String, Any?>) {
    try {
        view?.apply {
            Navigation.findNavController(this).apply {
                if (currentDestination?.id == fragmentId)
                    navigate(action, bundleOf(*extras))
            }
        }
    } catch (e: Exception) {
    }
}

fun Activity.permissionCheck(vararg permissions: String, closure: Boolean.() -> Unit) {
    hideSoftKeyboard()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Dexter.withActivity(this)
                .withPermissions(
                        permissions.asList()
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                closure(true)
                            } else {
                                closure(false)
                                longToast("You must grant\npermissions access\nto continue\n")
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                        closure(false)
                    }
                })
                .withErrorListener {
                    closure(false)
                }
                .check()
    } else
        closure(true)
}

inline fun <reified T : Activity?> Fragment.withActivity(block: T.() -> Unit) =
        activity?.let { block(activity as T) }

inline fun <reified T : Fragment> T.withContext(block: T.(context: Context) -> Unit) {
    activity?.apply {
        block(this)
    }
}

fun EditText?.requestFocusDelayed() {
    this?.postDelayed({
        this.requestFocus()
    }, 200)
}

fun View.showSoftKeyboard() =
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)


fun Activity.hideSoftKeyboard() {
    currentFocus?.let { focus ->
        focus.windowToken?.apply {
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this, 0)
        }
    }
}

fun Context.showAlertDialog(messageText: String, titleText: String) {

    vibrate(1000)

    alert {
        title = titleText
        message = messageText

        isCancelable = false
        positiveButton("OK") { dialog ->
            dialog.cancel()
        }

    }.show()

}

fun Context.vibrate(duration: Long = 500) {
    try {
        val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (duration == 500L) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                val pattern = longArrayOf(
                    50, duration / 3,
                    50, duration / 3,
                    100, duration / 2
                )
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            }

        } else {
            vibrator.vibrate(duration)
        }
    } catch (e: Exception) {}
}

fun Context.showDebugMessage(message: String) {
    if (BuildConfig.DEBUG)
        Log.d("Log0", "$message")
        onUi {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
}

fun String.printDebugLog() {
    if (BuildConfig.DEBUG)
        Log.d("Log0", "$this")
}

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clear() = edit().clear().commit()