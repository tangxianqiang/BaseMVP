package com.tang.basemvp.base

import android.util.Log
import rx.Observable

import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * 基于Rx的Presenter封装,控制订阅的生命周期(presenter的基类，对应mvp中的presenter)
 */

open class RxPresenter<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {
    //View的引用，可操控View的逻辑
    protected var mView: T? = null
    //观察者订阅管理对象
    private var mCompositeSubscription: CompositeSubscription? = null
    //当前最新的订阅者
    private var subscription: Subscription? = null

    /**
     * 取消所有的订阅 Unsubscribes itself and all inner subscriptions.
     */
    private fun unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription!!.unsubscribe()
        }
    }

    /**
     * 添加订阅
     * @param subscription
     */
    fun addSubscribe(subscription: Subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeSubscription()
        }
        this.subscription = subscription
        mCompositeSubscription!!.add(subscription)
    }

    override fun attachView(view: T) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
        unSubscribe()
    }

    /**
     * 取消当前的订阅者，当前的请求也会中断
     */
    fun cancelCurrent() {
        if (subscription == null) {
            return
        }
        if (!subscription!!.isUnsubscribed) {
            subscription!!.unsubscribe()
            Log.i(TAG, "cancelCurrent: 您取消了一个订阅")
        }
    }

    companion object {
        private val TAG = "RxPresenter"
    }
}
