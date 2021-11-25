package com.goodswarehouse.scanner.presentation.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodswarehouse.scanner.BR

interface Bindable {

    var binding: ViewDataBinding?

    fun AppCompatActivity.bind(layoutRes: Int, viewModel: Any? = null) {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        setViewModel(viewModel)
    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?, layoutRes: Int, viewModel: Any? = null): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        setViewModel(viewModel)
        return binding?.root
    }

    private fun setViewModel(viewModel: Any?) =
        viewModel?.apply {
            binding?.setVariable(BR.viewModel, this)
            binding?.executePendingBindings()
        }

}