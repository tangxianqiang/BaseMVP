package com.tang.basemvp.mvp.model

import com.tang.basemvp.base.Base
import java.io.Serializable

/**
 * 接口命名的字段是大写开头
 */

data class LoginModel(val Ext: ExtBean?,val Message: String,val State: Boolean) : Base(){

    /**
     * Ext : {"Authority":3,"Token":"213bfc688a7e099b1f2a27b64f59c9e1"}
     * Message : 登录成功
     * State : true
     */


    data class  ExtBean(val Authority: Int,val Token: String): Serializable{
        /**
         * Authority : 3
         * Token : 213bfc688a7e099b1f2a27b64f59c9e1
         */
    }
}
