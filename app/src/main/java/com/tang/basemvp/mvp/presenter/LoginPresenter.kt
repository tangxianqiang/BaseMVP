package com.tang.basemvp.mvp.presenter

import com.tang.basemvp.base.RxPresenter
import com.tang.basemvp.http.HttpManager
import com.tang.basemvp.mvp.contract.LoginContract
import com.tang.basemvp.mvp.model.LoginModel
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
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
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LoginModel> {
                    override fun onCompleted() {
                        mView!!.complete()
                    }

                    override fun onError(e: Throwable) {
                        mView!!.showError(e)
                    }

                    override fun onNext(loginModel: LoginModel) {
                        mView!!.loginSus(loginModel)
                    }
                })
        addSubscribe(subscription)
    }
}

