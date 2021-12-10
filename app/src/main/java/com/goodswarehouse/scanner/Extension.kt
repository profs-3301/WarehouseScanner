package com.goodswarehouse.scanner

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.goodswarehouse.scanner.presentation.base.BaseView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.reflect.Type
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

//Observable
fun <T> Observable<T>.applySchedulers(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.immediate(): Observable<T> =
    subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.io(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

fun <T> Observable<T>.ui(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.computation(): Observable<T> =
    subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation())

fun <T> Observable<T>.background(function: (error: Throwable) -> Unit): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
        .propagateErrors { function(it) }
        .retry()

fun <T> Observable<T>.propagateErrors(function: (error: Throwable) -> Unit): Observable<T> =
    doOnError {
        function(it)
    }.onExceptionResumeNext(Observable.empty<T>())
        .onErrorResumeNext(Observable.empty<T>())
        .onExceptionResumeNext(Observable.empty<T>())

fun <T> Observable<T>.default(function: (e: Throwable) -> Unit): Observable<T> =
    applySchedulers().propagateErrors(function).retry()

fun <T> Observable<T>.defaultActions(view: BaseView?): Observable<T> =
    doOnSubscribe { view?.showProgress() }
        .doFinally { view?.hideProgress() }
        .doOnTerminate { view?.hideProgress() }
        .doOnError { view?.hideProgress() }
        .doOnComplete { view?.hideProgress() }

fun Disposable.toDisposables(disposables: CompositeDisposable) {
    disposables.add(this)
}


//Boolean
operator inline fun <T : Any?> Boolean?.invoke(
    functionTrue: () -> T,
    functionFalse: () -> T,
    functionNull: () -> T
): T =
    if (this == true) functionTrue() else if (this == false) functionFalse() else functionNull()

operator inline fun Boolean.invoke(functionTrue: () -> Unit) {
    if (this) functionTrue()
}

//Any

inline operator fun <reified T, reified R> T?.invoke(
    functionTrue: T.() -> R,
    functionFalse: () -> R
): R =
    if (this != null)
        if (!(this is Boolean))
            functionTrue()
        else if (this)
            functionTrue()
        else
            functionFalse()
    else
        functionFalse()

inline fun <T> T?.isNull(block: () -> Unit) = (this == null){ block() }

//Int

val Int.bool: Boolean
    get() = this > 0

val Int?.value: Int
    get() = this ?: 0

//SharedPreference
fun SharedPreferences.putString(key: String, value: String?) = edit().putString(key, value).commit()
fun SharedPreferences.putLong(key: String, value: Long) = edit().putLong(key, value).commit()

fun Context.isConnected(): Boolean {
    val activeNetwork =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

//Any

fun Any.toJson(): String = Gson().toJson(this)

inline fun <reified T> String.toObject(): T =
    Gson().fromJson(this, T::class.java)

fun <T> String.toObject(type: Type): T = Gson().fromJson(this, type)

inline fun <T> Any.withErrorCaught(success: () -> T, error: (error: Throwable) -> T) =
    try {
        success.invoke()
    } catch (e: Throwable) {
        error.invoke(e)
    }

inline fun <T> Any.withErrorUncaught(success: () -> T) =
    try {
        success.invoke()
    } catch (e: Throwable) {
        throw e
    }


//For class in generics
inline fun <reified T> T.clazz() = T::class.java

//Long

fun Long.formatTimestamp(format: String): String = SimpleDateFormat(format, Locale.US).format(this)

fun Int.monthName(): String? = if (this in 0..11) {
    DateFormatSymbols().months[this]
} else null

fun Int.dayName(): String? = if (this in 0..7) {
    DateFormatSymbols().weekdays[this]
} else null

//String

fun String.removeBackslashes(): String = replace("\\", "")

fun String.formatDate(formatFrom: String, formatTo: String): String =
    SimpleDateFormat(formatTo, Locale.US).format(
        SimpleDateFormat(
            formatFrom,
            Locale.US
        ).parse(this).time
    )

fun String.toDate(formatFrom: String): Date = SimpleDateFormat(formatFrom, Locale.US).parse(this)


fun String.mimeType() =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(this))

fun String.lastPathSegment() = Uri.fromFile(File(this)).lastPathSegment

@Suppress("NOTHING_TO_INLINE")
inline fun String(bytes: ByteArray, charset: String = "UTF-8"): String =
    java.lang.String(bytes, charset) as String

@Suppress("NOTHING_TO_INLINE")
inline fun ByteArray?.convertToString() = String(this ?: byteArrayOf())

infix fun Long?.isSameDate(other: Long?): Boolean {
    if (this == null || other == null)
        return false
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = Date(this)
    cal2.time = Date(other)
    return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
}

val Date.endOfDay: Date
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
        cal.set(Calendar.SECOND, 59); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 999);
        return cal.time
    }

val Date.startOfDay: Date
    get() {
        val cal = Calendar.getInstance()
        cal.time = this
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hour to last hour
        cal.set(Calendar.MINUTE, 0); //set minutes to last minute
        cal.set(Calendar.SECOND, 0); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 0);
        return cal.time
    }

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)