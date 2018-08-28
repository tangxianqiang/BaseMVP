package com.tang.basemvp.mvp.presenter

import com.tang.basemvp.base.ObserverImp
import com.tang.basemvp.base.RxPresenter
import com.tang.basemvp.http.HttpManager
import com.tang.basemvp.mvp.contract.LoginContract
import com.tang.basemvp.mvp.model.LoginModel
import com.tang.basemvp.transformer.ScheduleTransformer

/**
 *对应的登录的presenter
 */

class LoginPresenter : RxPresenter<LoginContract.View>(), LoginContract.Presenter {

    /**
     * 登录
     * @param telephoneNumber
     * @param verificationCode
     * @param token
     */
    override fun login(telephoneNumber: String, verificationCode: String, token: String) {
        val subscription = HttpManager.getWorkHttpService()!!.signIn(telephoneNumber, verificationCode, token)
                .compose(ScheduleTransformer.instance)
                .subscribe(object : ObserverImp<Any>() {

                    override fun onErr(errCode: Int, str: String) {
                        mView!!.err(errCode,str)
                    }

                    override fun doNext(loginModel: Any) {
                        mView!!.loginSus(loginModel as LoginModel)
                    }
                })
        addSubscribe(subscription)
    }
}


