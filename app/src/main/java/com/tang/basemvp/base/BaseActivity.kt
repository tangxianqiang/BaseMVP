package com.tang.basemvp.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tang.basemvp.mvp.view.dialog.LoadingDialog

/**
 * 所有Activity的基类
 */
abstract class  BaseActivity< in V : BaseContract.BaseView, P : BaseContract.BasePresenter<V>>:AppCompatActivity(){
    //网络逻辑处理实例
    protected var mPresenter: P? = null
    //提示框
    private  var loading: LoadingDialog? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        mPresenter = initPresenter()
        mPresenter!!.attachView(this as V)
        setContentView(getLayoutId())
        configView()
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.detachView()
    }

    /**
     * 显示加载中弹窗
     */
    fun showLoading(){
        loading = loading?:LoadingDialog()
        loading?.setCallBack(object : LoadingDialog.OnDisMissCallBack {
            override fun disMiss() {
                (mPresenter as RxPresenter<V>).cancelCurrent()
            }
        })
        if (loading?.isVisible == false) {
            loading?.show(fragmentManager, "Loading")
        }
    }
    fun disMissLoading(){
        loading?.dismiss()
    }
    protected abstract fun getLayoutId():Int
    protected abstract fun configView()
    protected abstract fun initData()
    protected abstract fun initPresenter():P
}