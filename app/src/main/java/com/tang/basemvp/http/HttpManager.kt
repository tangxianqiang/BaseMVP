package com.tang.basemvp.http

import com.blankj.utilcode.util.NetworkUtils
import com.tang.basemvp.BuildConfig
import com.tang.basemvp.XXApplication

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络请求设置
 */

class HttpManager {

    /**
     * 缓存拦截器
     */
    class CacheInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!NetworkUtils.isAvailableByPing()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtils.isAvailableByPing()) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build()
            } else {
                // 无网络时，设置超时为4周
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build()
            }
            return chain.proceed(request)
        }
    }

    /**
     * 设置头拦截器
     */
    inner class HeadInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder()
                    .header("app-version", "")
                    .header("app-device", "")
                    .header("os-version", "")
                    .header("phone-model", "")
                    .header("Accept", "application/json")
                    .header("User-Agent", "Android/retrofit")
                    .header("channel", "")
                    .header("token", "")
                    .method(original.method(), original.body())
                    .build()
            return chain.proceed(request)
        }
    }

    /**
     * 设置公共参数拦截器
     */
    inner class CommonParameterInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("token", "")
                    .addQueryParameter("app_version", "1.0.0")
                    //...
                    .build()
            val request = originalRequest.newBuilder().url(modifiedUrl).build()
            return chain.proceed(request)
        }
    }

    companion object {
        //网络请求延迟时间，默认为20秒
        private val DEFAULT_TIMEOUT = 0x000014
        private var client: OkHttpClient? = null
        private var retrofit: Retrofit? = null
        private var httpService: HttpService? = null

        /**
         * 获取httpService实例
         * @return
         */
        fun getWorkHttpService(): HttpService? {
            httpService = getWorkRetrofit().create(HttpService::class.java)
            return httpService
        }

        private fun getWorkRetrofit(): Retrofit {
            if (retrofit == null) {
                synchronized(HttpManager::class.java) {
                    if (retrofit == null) {
                        //添加一个log拦截器,打印log
                        val httpLoggingInterceptor = HttpLoggingInterceptor()
                        if (BuildConfig.DEBUG) {//开发模式中记录整个body的日志
                            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                        } else {//开发模式中记录基本的一些日志，如状态值返回200
                            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                        }

                        //设置 请求的缓存的大小跟位置
                        val cacheFile = File(XXApplication.context.cacheDir, "cache")
                        //50Mb 缓存的大小
                        val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())

                        client = OkHttpClient.Builder()
                                //                            .addInterceptor(new CacheInterceptor())//添加缓存拦截器
                                .addInterceptor(httpLoggingInterceptor) //添加日志拦截器,所有的请求响应度看到
                                //                            .cache(cache)
                                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                                .build()

                        // 获取retrofit的实例
                        retrofit = Retrofit.Builder()
                                .baseUrl(HttpService.BASE_URL)
                                .client(client!!)
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                    }
                }
            }
            return retrofit!!
        }
    }

}
