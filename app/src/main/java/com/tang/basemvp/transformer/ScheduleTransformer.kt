package com.tang.basemvp.transformer

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 单例实现的转换器，统一指定每一个接口请求实在io线程，回调在ui线程
 */

class ScheduleTransformer<T>
/**
 * 私有的构造函数
 */
private constructor() : Observable.Transformer<T, T> {
    /**
     *懒加载内部单例
     */
    private object TransformerHolder {
        private val instance: ScheduleTransformer<Any>? = null
        fun getInstance(): ScheduleTransformer<Any> {
            return instance ?: ScheduleTransformer<Any>()
        }
    }

    override fun call(tObservable: Observable<T>): Observable<T> {
        return tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 伴生
     */
    companion object {
        val instance: ScheduleTransformer<Any>
            get() = TransformerHolder.getInstance()
    }
}



