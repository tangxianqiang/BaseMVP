package com.tang.basemvp.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tang.basemvp.mvp.view.dialog.LoadingDialog

/**
 *
 */
abstract class BaseFragment<in V : BaseContract.BaseView, P : BaseContract.BasePresenter<V>>:Fragment(){
    //网络逻辑处理实例
    protected var mPresenter: P? = null
    //提示框
    private var loading: LoadingDialog? = null;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initData()
        mPresenter = initPresenter()
        mPresenter!!.attachView(this as V)
        return inflater!!.inflate(getLayoutId(), container, false);
        configView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.attachView(this as V)
    }

    /**
     * 显示加载中弹窗
     */
    fun showLoading(){
        if (loading == null) {
            loading = LoadingDialog()
            loading!!.setCallBack(object : LoadingDialog.OnDisMissCallBack {
                override fun disMiss() {
                    (mPresenter as RxPresenter<V>).cancelCurrent()
                }
            })
        }
        if (!loading!!.isVisible) {
            loading!!.show(activity.fragmentManager, "Loading")
        }
    }
    fun disMissLoading(){
        loading!!.dismiss()
    }
    protected abstract fun getLayoutId():Int
    protected abstract fun configView()
    protected abstract fun initData()
    protected abstract fun initPresenter():P
}