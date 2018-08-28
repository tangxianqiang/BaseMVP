package com.tang.basemvp.http

import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source

/**
 *自定义ResponseBody，能实现文件进度监听回调
 */

class FileResBody(private val responseBody: ResponseBody, private val listener: DLProListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L
            internal var lastPro = 0

            override fun read(sink: Buffer, byteCount: Long): Long {
                try {
                    val bytesRead = super.read(sink, byteCount)
                    if (totalBytesRead == 0L && listener != null) {
                        listener.DLoadStart()
                    }
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                    if (null != listener) {
                        if (bytesRead == -1L) {
                            listener.DLoadSuccess()
                        }
                        if ((totalBytesRead * 100L / responseBody.contentLength()).toInt() > lastPro) {
                            listener.DLoadProgress((totalBytesRead * 100L / responseBody.contentLength()).toInt())
                        }
                        lastPro = (totalBytesRead * 100L / responseBody.contentLength()).toInt()
                    }
                    return bytesRead
                } catch (e: IOException) {
                    listener?.DLoadFail()
                }

                return 0L

            }
        }

    }
}
