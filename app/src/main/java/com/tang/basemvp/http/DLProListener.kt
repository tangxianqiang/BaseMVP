package com.tang.basemvp.http

/**
 * Created by tang on 2017/11/29.
 */

interface DLProListener {
    /** 开始下载文件  */
    fun DLoadStart()

    /** 下载文件成功  */
    fun DLoadSuccess()

    /** 下载文件进度  */
    fun DLoadProgress(progress: Int)

    /** 下载文件失败  */
    fun DLoadFail()
}
