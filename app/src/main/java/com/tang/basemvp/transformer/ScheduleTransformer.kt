package com.tang.basemvp.transformer

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Administrator on 2018/3/26.
 * override fun call(t: Observable<T>?): Observable<T> {
return t!!.subscribeOn(Schedulers.io())
.unsubscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread())
}
class TransformerHolder (){
companion object {
var instance : ScheduleTransformer<Any>? = null;

}
}
 */
class ScheduleTransformer<T>
/**
 * 私有的构造函数
 */
private constructor() : Observable.Transformer<T, T> {
    /**
     *内部单例
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

    companion object {
        val instance: ScheduleTransformer<Any>
            get() = TransformerHolder.getInstance()
    }
}



