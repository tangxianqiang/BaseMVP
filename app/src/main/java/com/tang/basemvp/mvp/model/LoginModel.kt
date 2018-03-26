package com.tang.basemvp.mvp.model

import com.tang.basemvp.base.Base
import java.io.Serializable

/**
 * 接口命名的字段是大写开头
 */

class LoginModel : Base() {

    /**
     * Ext : {"Authority":3,"Token":"213bfc688a7e099b1f2a27b64f59c9e1"}
     * Message : 登录成功
     * State : true
     */

    var ext: ExtBean? = null


    class ExtBean {
        /**
         * Authority : 3
         * Token : 213bfc688a7e099b1f2a27b64f59c9e1
         */

        var authority: Int = 0
        var token: String? = null
    }
}
