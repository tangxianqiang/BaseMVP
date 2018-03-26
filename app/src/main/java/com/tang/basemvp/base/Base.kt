package com.tang.basemvp.base

import java.io.Serializable

/**
 * Created by Administrator on 2017/8/15.
 * 序列化对象，模型的基类，对应mvp中的model
 * 基类模型抽出共同的属性，比如与后台接口约定好的出错标记、出错信息等
 */

open class Base : Serializable {
    /*后台接口设置的是布尔值的出错判断*/
    var State: Boolean = false
    /*出错信息*/
    lateinit var Message: String
}