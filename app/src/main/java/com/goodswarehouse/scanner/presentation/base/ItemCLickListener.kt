package com.goodswarehouse.scanner.presentation.base

interface ItemCLickListener {
    fun onItemClick(item: BaseListItem, id: Int){}
    fun onItemClick(item: BaseListItem, isCorrect : Boolean){}
}