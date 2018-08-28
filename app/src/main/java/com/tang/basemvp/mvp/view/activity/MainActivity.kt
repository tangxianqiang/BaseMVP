package com.tang.basemvp.mvp.view.activity

import com.blankj.utilcode.util.ToastUtils
import com.tang.basemvp.R
import com.tang.basemvp.base.BaseActivity
import com.tang.basemvp.mvp.contract.LoginContract
import com.tang.basemvp.mvp.model.LoginModel
import com.tang.basemvp.mvp.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 模拟登录的view层
 */
class MainActivity : BaseActivity<LoginContract.View,LoginContract.Presenter>(),LoginContract.View {
    override fun err(code: Int, message: String) {
        ToastUtils.showShort("错误码：$code\n错误信息：$message")
    }

    override fun loginSus(loginModel: LoginModel) {
        disMissLoading()
        if (loginModel.State) {
            ToastUtils.showShort("登录成功")
        }else{
            ToastUtils.showShort(loginModel.Message)
        }
    }

    override fun showError(e: Throwable) {
        disMissLoading()
    }

    override fun complete() {

    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun configView() {
        login.setOnClickListener({
            showLoading()
            mPresenter!!.login("18381309101","1111","") })
    }

    override fun initData() {

    }

    override fun initPresenter(): LoginContract.Presenter = LoginPresenter()

}
