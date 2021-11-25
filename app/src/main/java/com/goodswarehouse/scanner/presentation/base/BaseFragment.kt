package com.goodswarehouse.scanner.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodswarehouse.scanner.domain.model.Scan
import com.goodswarehouse.scanner.presentation.withActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.*
import javax.inject.Inject

abstract class BaseFragment<T : ViewModel> : BaseView, DaggerFragment(), Bindable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override var binding: ViewDataBinding? = null

    val viewModel: T? by lazy { ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeViewInit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        bind((inflater), container, getLayoutResource(), viewModel)

    @StyleRes
    open fun customTheme(): Int? = null

    abstract fun getViewModelClass(): Class<T>

    fun setTitle(title: String) {
        activity?.title = title.uppercase()
    }

    fun addBackArrow(enable : Boolean) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(enable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (viewModel as? BaseViewModel<BaseView>)?.view = this
        viewInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (viewModel as? BaseViewModel<BaseView>)?.dispose()
    }

    override fun onDetach() {
        super.onDetach()
        clearFindViewByIdCache()
    }

    protected abstract fun getLayoutResource(): Int

    override fun showError(message: String, title: String) = withActivity<BaseActivity> {
        showError(message, title)
    }

    override fun showError(scans: List<Scan>, sku: String) = withActivity<BaseActivity> {
        showError(scans, sku)
    }

    override fun showMessage(message: String, title: String?): Unit? = withActivity<BaseActivity> { showMessage(message, title) }

    override fun onVibrate() = withActivity<BaseActivity> { onVibrate() }

    override fun showSnackBar(message: String?): Unit? = withActivity<BaseActivity> { showSnackBar(message) }

    override fun showProgress() {
        withActivity<BaseActivity> { showProgress() }
    }

    override fun hideProgress() {
        withActivity<BaseActivity> { hideProgress() }
    }


}