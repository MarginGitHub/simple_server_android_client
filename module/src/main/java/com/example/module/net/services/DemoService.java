package com.example.module.net.services;

import com.example.module.beans.Result;
import com.example.module.beans.demo.RegisterResult;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by margin on 17-9-9.
 */

public interface DemoService {
    @FormUrlEncoded
    @POST("/v1/register")
    Observable<Result<RegisterResult>> register(@Field("mobile") String mobile, @Field("password") String password);
}
