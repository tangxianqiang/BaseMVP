# BaseMVP
Baae MVP use Rxjava+Retrofit and some base class.you can use it with working.Besides, there is workspace for java version.（使用契约接口实现的MVP，不会造成activity的内存泄漏，其中包含Rxjava、Retrofit的使用，功能齐全，包括异常处理和compose操作符的使用等，可以直接在项目中使用）
[Java版本](https://github.com/tangxianqiang/BaseMVP/blob/master/forJava)
## UML图
![](https://github.com/tangxianqiang/BaseMVP/blob/master/imgs/mvp_uml.png)<br/>
## 重要的类
1、统一错误处抽象理观察者</br>
```
abstract class ObserverImp<T> : Observer<T> {
...
override fun onError(e: Throwable) {
        ...
        if (e is HttpException) {
            when (e.code()) {
                UNAUTHORIZED -> onErr(UNAUTHORIZED, "")
                FORBIDDEN -> onErr(FORBIDDEN, "权限错误")          //权限错误，需要实现
                NOT_FOUND -> onErr(NOT_FOUND, "")
                REQUEST_TIMEOUT -> onErr(REQUEST_TIMEOUT, "")
                GATEWAY_TIMEOUT -> onErr(GATEWAY_TIMEOUT, "")
                INTERNAL_SERVER_ERROR -> onErr(INTERNAL_SERVER_ERROR, "")
                BAD_GATEWAY -> onErr(BAD_GATEWAY, "")
                SERVICE_UNAVAILABLE -> onErr(SERVICE_UNAVAILABLE, "")
                else -> onErr(ERR_CODE_NET, "")
            }
        } else if (e is SocketTimeoutException) {
            onErr(GATEWAY_TIMEOUT, "请求超时!")
        ..
    }
}
...
 companion object {
        //对应HTTP的状态码
        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504
        private val ERR_CODE_NET = 0x110
        private val ERR_CODE_UNKNOWN = 0x111
        private val ERR_CODE_LOGIC = 0x112
    }
```
2、释放activity的引用，防止内存泄漏</br>
```
    override fun attachView(view: T) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
        unSubscribe()
    }
```
3、用户可随时取消所订阅的事件</br>
```
    fun cancelCurrent() {
        if (subscription == null) {
            return
        }
        if (!subscription!!.isUnsubscribed) {
            subscription!!.unsubscribe()
            Log.i(TAG, "cancelCurrent: 您取消了一个订阅")
        }
    }
```
4、单例实现的转换器，统一指定每一个接口请求实在io线程，回调在ui线程</br>
```
    override fun call(tObservable: Observable<T>): Observable<T> {
        return tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
```
## Demo
请求成功</br>
![](https://github.com/tangxianqiang/BaseMVP/blob/master/imgs/success.png)<br/>
接口错误</br>
![](https://github.com/tangxianqiang/BaseMVP/blob/master/imgs/fail.png)<br/>
