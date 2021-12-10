package com.goodswarehouse.scanner.data

import android.util.Base64
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.goodswarehouse.scanner.domain.repo.RepoLocal
import okhttp3.*
import okio.Buffer
import java.io.File

val defaultHeaders = arrayOf(
    "Content-Type" to "application/json",
    "Accept" to "*/*",
    "Accept-Encoding" to "gzip, deflate, br",
    "Connection" to "keep-alive"
)

fun OkHttpClient.Builder.addHeaders(vararg headers: kotlin.Pair<String, String>): OkHttpClient.Builder = addInterceptor {
    it.proceed(it.request().newBuilder().apply {
        hashMapOf<String, String>().apply { putAll(headers) }.forEach { addHeader(it.key, it.value) }
    }.build())
}

fun OkHttpClient.Builder.addHeadersWithDefault(tokenRepo: RepoLocal): OkHttpClient.Builder = addInterceptor {
    it.proceed(it.request().newBuilder().apply {
        hashMapOf<String, String>().apply {
            putAll(defaultHeaders)
        }.forEach { addHeader(it.key, it.value) }
    }.build())
}

fun convertSignIntoBase64(login: String, pass: String): String {
    val text = "$login:$pass"
    val data = text.toByteArray(charset("UTF-8"))

    return Base64.encodeToString(data, Base64.NO_WRAP)
}

fun File.toRequestBody(): RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), this)

fun String.toRequestBody(): RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), this)

fun Response.bodyToString(): String {
    try {
        return cloneResponseBody().string()
    } catch (e: Exception) {
        return "{}"
    }
}

fun Response.cloneResponseBody(): ResponseBody = ResponseBody
        .create(body()!!.contentType(), body()!!.contentLength(), Buffer().apply { body()?.source()?.let {
            writeAll(it)
        } })

fun Response.buildResponse(message: String): Response = newBuilder().message(message)
        .code(code())
        .body(body())
        .headers(headers())
        .networkResponse(networkResponse())
        .handshake(handshake())
        .build()

fun Interceptor.Chain.next(): Response = proceed(request())

fun String.toJsonObject(): JsonObject? {
    var json: JsonObject? = null
    try {
        json = JsonParser().parse(this).asJsonObject
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return json
}