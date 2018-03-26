package com.tang.basemvp.base

import java.net.SocketTimeoutException
import java.net.UnknownHostException

import retrofit2.adapter.rxjava.HttpException
import rx.Observer

/**
 * Created by Administrator on 2018/3/26.
 */

abstract class ObserverImp<T> : Observer<T> {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        var e = e
        var throwable = e
        //获取最根源的异常
        while (throwable.cause != null) {
            if (e is HttpException) {
                break
            }
            e = throwable
            throwable = throwable.cause!!
        }
        if (e is HttpException) {
            when (e.code()) {
                UNAUTHORIZED -> onErr(UNAUTHORIZED, "")
                FORBIDDEN -> onErr(FORBIDDEN, "权限错误")          //权限错误，需要实现
                NOT_FOUND -> onErr(NOT_FOUND, "")
                REQUEST_TIMEOUT -> onErr(REQUEST_TIMEOUT, "")
                GATEWAY_TIMEOUT -> onErr(GATEWAY_TIMEOUT, "")
                INTERNAL_SERVER_ERROR -> onErr(INTERNAL_SERVER_ERROR, "")
                BAD_GATEWAY -> onErr(BAD_GATEWAY, "")
                SERVICE_UNAVAILABLE -> onErr(SERVICE_UNAVAILABLE, "")
                else -> onErr(ERR_CODE_NET, "")
            }
        } else if (e is SocketTimeoutException) {
            onErr(GATEWAY_TIMEOUT, "请求超时!")
        } else if (e is UnknownHostException) {
            onErr(ERR_CODE_NET, "网络连接失败!")
        } else {
            onErr(ERR_CODE_UNKNOWN, "未知错误!")
        }
    }

    override fun onNext(t: T) {
        val base = t as Base
        if (base.State) {
            doNext(t)
        } else {//网络接口内部逻辑出错
            onErr(ERR_CODE_LOGIC, base.Message)
        }
    }

    /**
     * 出错回调
     * @param errCode
     * @param str
     */
    protected abstract fun onErr(errCode: Int, str: String)

    /**
     * 在已经实现了接口业务逻辑出错判断后开始进行后面的流程
     * @see .onNext
     * @param t
     */
    protected abstract fun doNext(t: T)

    companion object {
        //对应HTTP的状态码
        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504
        private val ERR_CODE_NET = 0x110
        private val ERR_CODE_UNKNOWN = 0x111
        private val ERR_CODE_LOGIC = 0x112
    }
}
