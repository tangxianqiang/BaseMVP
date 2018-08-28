package com.tang.basemvp.mvp.contract

import com.tang.basemvp.base.BaseContract
import com.tang.basemvp.mvp.model.LoginModel

/**
 *登录的契约类
 */
interface LoginContract {
    interface View : BaseContract.BaseView {
        fun loginSus(loginModel: LoginModel) //登录成功
        fun err(code: Int, message: String) //出错
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun login(telephoneNumber: String, verificationCode: String, token: String) //登录
    }
}