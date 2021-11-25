package com.goodswarehouse.scanner.domain.base

open class BaseResponse<T> {

    val data: T? = null
    val error: BaseError? = null

}