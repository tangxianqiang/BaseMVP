package com.tang.basemvp.base

/**
 * 对应mvp中的contract契约接口
 */

interface BaseContract {
    interface BasePresenter<in T> {
        /*该方法可以获取到View实例对象*/
        fun attachView(view: T)

        /*释放View对象的引用，gc才能回收View*/
        fun detachView()
    }

    interface BaseView {
        fun showError(e: Throwable)
        fun complete()
    }
}
