package com.tang.basemvp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.blankj.utilcode.util.Utils
import com.tang.basemvp.utils.SharedPreferencesUtil
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Administrator on 2017/8/15.
 */

class XXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        //初始化工具类
        Utils.init(this)
        SharedPreferencesUtil.init(this, "TOKEN_RMM", Context.MODE_PRIVATE)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

    }

    private fun registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
                    /**
                     * 监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity)

                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {

                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (null == mActivitys && mActivitys.isEmpty()) {
                        return
                    }
                    val flag = -1
                    if (finishedCallBack != null) {
                        finishedCallBack!!.finish(flag)
                    }

                    if (mActivitys.contains(activity)) {
                        /**
                         * 监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity)
                    }
                }
            })
        }
    }

    interface OnActivityFinished {
        fun finish(flag: Int)
    }

    companion object {
        lateinit var context: Context

        /**
         * 维护Activity 的list
         */
        private val mActivitys = CopyOnWriteArrayList<Activity>()

        /**
         * @param activity 添加一个activity到管理里
         */
        fun pushActivity(activity: Activity) {
            mActivitys.add(activity)
        }

        /**
         * @param activity 删除一个activity在管理里
         */
        fun popActivity(activity: Activity) {
            mActivitys.remove(activity)
        }


        /**
         * get current Activity 获取当前Activity（栈中最后一个压入的）
         */
        fun currentActivity(): Activity? {
            return if (mActivitys == null || mActivitys.isEmpty()) {
                null
            } else mActivitys[mActivitys.size - 1]
        }

        /**
         * 结束当前Activity（栈中最后一个压入的）
         */
        fun finishCurrentActivity() {
            if (mActivitys == null || mActivitys.isEmpty()) {
                return
            }
            val activity = mActivitys[mActivitys.size - 1]
            finishActivity(activity)
        }

        /**
         * 结束指定的Activity
         */
        fun finishActivity(activity: Activity?) {
            var activity = activity
            if (mActivitys == null || mActivitys.isEmpty()) {
                return
            }
            if (activity != null) {
                mActivitys.remove(activity)
                activity.finish()
                activity = null
            }
        }

        /**
         * 结束指定类名的Activity
         */
        @Synchronized
        fun finishActivity(cls: Class<*>) {

            if (mActivitys == null || mActivitys.isEmpty()) {
                return
            }
            for (activity in mActivitys) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }

        /**
         * 按照指定类名找到activity
         *
         * @param cls
         * @return
         */
        fun findActivity(cls: Class<*>): Activity? {
            var targetActivity: Activity? = null
            if (mActivitys != null) {
                for (activity in mActivitys) {
                    if (activity.javaClass == cls) {
                        targetActivity = activity
                        break
                    }
                }
            }
            return targetActivity
        }

        /**
         * @return 作用说明 ：获取当前最顶部activity的实例
         */
        val topActivity: Activity?
            get() {
                var mBaseActivity: Activity? = null
                synchronized(mActivitys) {
                    val size = mActivitys.size - 1
                    if (size < 0) {
                        return null
                    }
                    mBaseActivity = mActivitys[size]
                }
                return mBaseActivity
            }

        /**
         * @return 作用说明 ：获取当前最顶部的acitivity 名字
         */
        val topActivityName: String?
            get() {
                var mBaseActivity: Activity? = null
                synchronized(mActivitys) {
                    val size = mActivitys.size - 1
                    if (size < 0) {
                        return null
                    }
                    mBaseActivity = mActivitys[size]
                }
                return mBaseActivity!!.javaClass.name
            }

        /**
         * 结束所有Activity
         */
        fun finishAllActivity() {
            if (mActivitys == null) {
                return
            }
            for (activity in mActivitys) {
                activity.finish()
            }
            mActivitys.clear()
        }

        /**
         * 退出应用程序
         */
        fun appExit() {
            try {
                finishAllActivity()
            } catch (e: Exception) {
            }

        }

        fun setOnActivityFinished(finishedCall: OnActivityFinished) {
            finishedCallBack = finishedCall
        }

        private var finishedCallBack: OnActivityFinished? = null
    }
}
