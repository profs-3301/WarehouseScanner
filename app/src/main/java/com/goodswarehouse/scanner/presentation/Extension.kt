@file:Suppress("NOTHING_TO_INLINE")

package com.goodswarehouse.scanner.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.Navigation
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.goodswarehouse.scanner.BuildConfig
import org.jetbrains.anko.alert
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException

fun Fragment.navigate(@IdRes action: Int, @IdRes fragmentId: Int, closure: Bundle.() -> Unit) {
    try {
        if (view?.id == fragmentId)
            view?.apply {
                Navigation.findNavController(this).navigate(action, bundleOf().apply { closure(this) })
            }
    } catch (e: Exception) {
    }
}

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

/*private fun Activity.onPermissionsError(closure: Boolean.() -> Unit) {
    closure(false)
    longToast("You must grant\npermissions access\nto continue\n")
}*/

fun Activity.openSettings(){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Context.getScreenAspectRatio(higherThanOne: Boolean = true): Float {
    val point = getScreenPointDimens()
    return if (higherThanOne) {
        if (point.y > point.x) point.y.toFloat() / point.x.toFloat() else point.x.toFloat() / point.y.toFloat()
    } else {
        if (point.y < point.x) point.y.toFloat() / point.x.toFloat() else point.x.toFloat() / point.y.toFloat()
    }
}

fun Context.getScreenPointDimens(): Point {
    val point = Point()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            .getSize(point)
    return point
}

fun Activity.getScreenRect(): Rect {
    val displayRectangle = Rect()
    window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
    return displayRectangle
}

fun Fragment.getScreenRect(): Rect {
    val displayRectangle = Rect()
    activity?.window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)
    return displayRectangle
}

fun Context.dpToPixel(@Dimension(unit = Dimension.DP) dp: Float): Int =
        (dp * resources.displayMetrics.density).toInt()

fun Context.dp(@Dimension(unit = Dimension.DP) dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics())

inline fun <reified T : Activity?> Fragment.withActivity(block: T.() -> Unit) =
        activity?.let { block(activity as T) }

inline fun <reified T : Fragment> T.withContext(block: T.(context: Context) -> Unit) {
    activity?.apply {
        block(this)
    }
}

fun Activity.viewLink(link: String = "http://www.google.com") {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)));
}

fun ViewGroup.inflateView(@LayoutRes layout: Int): View =
        LayoutInflater.from(context).inflate(layout, this, false)


fun Fragment.getImageFromFilesystem(requestCode: Int, multipleFiles: Boolean = false, videoFiles: Boolean = false) {
    withActivity<Activity> {
        permissionCheck(android.Manifest.permission.READ_EXTERNAL_STORAGE) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multipleFiles)
            intent.type = "image/*, video/*"
            this@getImageFromFilesystem.startActivityForResult(intent, requestCode)
        }
    }
}

//SharedPreference

@SuppressLint("ApplySharedPref")
fun SharedPreferences.putString(key: String, value: String) = edit().putString(key, value).commit()

//Keyboard
fun EditText?.disableAutoShowKeyboard(){
    this?.showSoftInputOnFocus = false
}

fun EditText?.requestFocusDelayed() {
    this?.postDelayed({
        this.requestFocus()
    }, 200)
}

fun Activity.showSoftKeyboard() =
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)

fun View.showSoftKeyboard() =
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)

fun View.hideSoftKeyboard() =
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)


fun Activity.hideSoftKeyboard() {
    currentFocus?.let { focus ->
        focus.windowToken?.apply {
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this, 0)
        }
    }
}

fun Context.isConnected(): Boolean {
    val activeNetwork = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

fun Fragment.getImageFromCamera(requestCode: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    this@getImageFromCamera.startActivityForResult(intent, requestCode)
}

fun <T : Fragment> T.withExtras(extras: Bundle.() -> Unit): T =
        this.apply {
            bundleOf().apply {
                extras(this)
                arguments = this
            }
        }

inline fun <T : Any?> Bundle?.value(key: String): T {
    @Suppress("UNCHECKED_CAST")
    return this?.get(key) as T
}

fun Activity.getImageFromFilesystem(requestCode: Int, multipleFiles: Boolean = false) {
    permissionCheck(android.Manifest.permission.READ_EXTERNAL_STORAGE) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multipleFiles)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*, video/*"
        startActivityForResult(intent, requestCode)
    }
}

fun Activity.getImageFromCamera(requestCode: Int) {
    permissionCheck(android.Manifest.permission.CAMERA) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCode)
    }
}

fun Context.createTempFileFromBitmap(image: Bitmap): Uri? {
    try {

        val fileTemp = File(externalCacheDir, "profilePicture${System.currentTimeMillis()}.jpg")
        fileTemp.createNewFile()
        val fos = FileOutputStream(fileTemp)
        image.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.close()

        return Uri.fromFile(fileTemp)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return null
    } catch (e: MalformedURLException) {
        e.printStackTrace()
        return null
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

}

fun ImageView.loadBitmapFromFile(file: Uri) {
    setImageBitmap(MediaStore.Images.Media.getBitmap(this.context.contentResolver, file))
}

inline operator fun <reified T : Any?> Bundle.invoke(key: String): T? = get(key) as? T?

inline fun <reified T> Context.systemService(name: String) = getSystemService(name) as T

fun ClipData.items() = iterator().asSequence().toList()

operator fun TabLayout.iterator() = object : Iterator<TabLayout.Tab> {
    private var index = 0
    override fun hasNext() = index < tabCount
    override fun next() = getTabAt(index++) ?: throw IndexOutOfBoundsException()
}

val TabLayout.tabs
    get() = iterator().asSequence().toList()

operator fun ClipData.iterator() = object : Iterator<ClipData.Item> {
    private var index = 0
    override fun hasNext() = index < itemCount
    override fun next() = getItemAt(index++) ?: throw IndexOutOfBoundsException()
}

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clear() = edit().clear().commit()

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

inline val <reified T : Any> Class<T>.TAG: String get() = "$`package`$name"

inline val <reified T : Any> Class<T>.ID: Int get() = TAG.hashCode()

inline val <reified T> ObservableField<T>.value: T?
    get() = this.get()

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
//        onUi {
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        }
}

fun showDebugMessage(message: String) {
    if (BuildConfig.DEBUG)
        Log.d("Log0", "$message")
}


fun String.printDebugLog() {
    if (BuildConfig.DEBUG)
        Log.d("Log0", "$this")
}