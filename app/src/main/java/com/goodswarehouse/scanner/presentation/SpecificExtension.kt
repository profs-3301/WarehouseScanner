package com.goodswarehouse.scanner.presentation

import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.domain.model.PrefixModel

enum class SkuType {
    NORMAL,
    SPECIFIC_A,
    SPECIFIC_B,
    SPECIFIC_C,
    SPECIFIC_D
}

fun checkType(sku: String, repo: LocalRepo): Any {
    return when {
        repo.getSpecificListA().contains(sku) -> {
            SkuType.SPECIFIC_A
        }
        repo.getSpecificListB().contains(sku) -> {
            SkuType.SPECIFIC_B
        }
        repo.getSpecificListC().contains(sku) -> {
            SkuType.SPECIFIC_C
        }
        repo.getSpecificListD().contains(sku) -> {
            SkuType.SPECIFIC_D
        }
        else -> SkuType.NORMAL
    }
}

fun getSkuPrefixes(sku: String, prefixesList: List<PrefixModel>): List<String> {
    val prefixesFilteredList = prefixesList.filter {
        it.sku == sku
    }
    return if (prefixesFilteredList.isEmpty()) {
        listOf()
    } else
        prefixesFilteredList.first().prefixes
}

fun String.hasCorrectPrefix(prefixesList: List<String>): Boolean {
    if (prefixesList.isNullOrEmpty()){
        return true
    }

    val eanPrefix = this.take(3)
    prefixesList.map { prefix ->
        if (eanPrefix.contains(prefix, true))
            return true
    }
    return false
}