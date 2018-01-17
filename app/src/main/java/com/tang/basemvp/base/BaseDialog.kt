package com.tang.basemvp.base

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * DialogFragment是google推荐使用的弹窗
 */

abstract class BaseDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        initOnStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView(view)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //设置dialog的样式
        val dialog = Dialog(activity,setStyle())
        config(dialog)
        return dialog
    }

    /**
     * 设置布局
     * @return
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 设置弹窗的样式
     * @param
     */
    protected abstract fun setStyle(): Int

    /**
     * 初始化控件
     * @param view
     */
    protected abstract fun initView(view: View)

    /**
     * 初始化数据，在初始化控件之前调用
     */
    protected abstract fun initData()

    /**
     * @param dialog
     */
    protected abstract fun config(dialog: Dialog)

    /**
     * 窗口的基本设置，包括宽高、动画、渐变、是否有navigationBar等
     */
    protected abstract fun initOnStart()
}
