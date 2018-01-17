package com.tang.basemvp.mvp.view.activity

import com.blankj.utilcode.util.ToastUtils
import com.tang.basemvp.R
import com.tang.basemvp.base.BaseActivity
import com.tang.basemvp.mvp.contract.LoginContract
import com.tang.basemvp.mvp.model.LoginModel
import com.tang.basemvp.mvp.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<LoginContract.View,LoginContract.Presenter>(),LoginContract.View {
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
