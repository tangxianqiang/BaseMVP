package com.tang.basemvp.http;


import com.tang.basemvp.mvp.model.LoginModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 测试请换地址
 */

public interface HttpService {
    //网络请求基地址
    String BASE_URL = "https://rmm.xxx.com/";
    //登录
    @FormUrlEncoded
    @POST("Signin")
    Observable<LoginModel> signIn(@Field("TelephoneNumber") String telephoneNumber, @Field("VerificationCode") String verificationCode, @Field("Token") String token);
}