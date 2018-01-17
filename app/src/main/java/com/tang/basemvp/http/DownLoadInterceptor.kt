package com.tang.basemvp.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 文件下载拦截器
 */

class DownLoadInterceptor(private val listener: DLProListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
                .body(FileResBody(originalResponse.body(), listener))
                .build()
    }
}
