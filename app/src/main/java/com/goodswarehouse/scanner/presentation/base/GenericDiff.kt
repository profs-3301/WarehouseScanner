package com.goodswarehouse.scanner.presentation.base

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

object GenericDiff : DiffObservableList.Callback<BaseListItem> {
    override fun areItemsTheSame(oldItem: BaseListItem?, newItem: BaseListItem?): Boolean =
        oldItem?.comparableId?.equals(newItem?.comparableId) == true


    override fun areContentsTheSame(oldItem: BaseListItem?, newItem: BaseListItem?): Boolean =
        oldItem?.equals(newItem) == true
}