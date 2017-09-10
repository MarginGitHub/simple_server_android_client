package com.example.margin.demo.net;

import android.content.Context;
import android.util.Log;

import com.example.module.beans.Result;
import com.example.module.beans.demo.RegisterResult;
import com.example.module.net.common.NetServiceProvider;
import com.example.module.net.configs.DemoNetConfig;
import com.example.module.net.services.DemoService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by margin on 17-9-9.
 */

public class Net {
    private static final String TAG = Net.class.getSimpleName();
    private static Net sInstance;
    private static DemoService mProvider;
    private Map<String, List<Subscription>> mSubscriptions;

    private Net() {
        mSubscriptions = new HashMap<>();
    }
    public static void init(Context context) {
        mProvider = (DemoService)NetServiceProvider.instance(context)
                .provider(DemoService.class, DemoNetConfig.BASE_URL);
    }

    public static Net instance() {
        if (sInstance == null) {
            synchronized (Net.class) {
                if (sInstance == null) {
                    sInstance = new Net();
                }
            }
        }
        return sInstance;
    }

    public void addRequest(String requestId, Subscription s) {
        List<Subscription> subscriptions = mSubscriptions.get(requestId);
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
        }
        subscriptions.add(s);
        mSubscriptions.put(requestId, subscriptions);
    }

    public void releaseRequest(String requestId) {
        List<Subscription> subscriptions = mSubscriptions.get(requestId);
        if (subscriptions != null) {
            for (Subscription s:
                 subscriptions) {
                s.cancel();
            }
        }
        mSubscriptions.remove(requestId);
    }

    public void register(String mobile, String password, final OnNext<Result<RegisterResult>> next,
                         final OnError err, final String requestId){
        mProvider.register(mobile, password)
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<RegisterResult>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Result<RegisterResult> registerResultResult) {
                        next.onNext(registerResultResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        err.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(new Subscriber<Result<RegisterResult>>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        addRequest(requestId, s);
//                    }
//
//                    @Override
//                    public void onNext(Result<RegisterResult> registerResultResult) {
//                        next.onNext(registerResultResult);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        err.onError(t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//                    }
//                });
    }


    public interface OnNext<T> {
        void onNext(T data);
    }

    public interface OnError {
        void onError(Throwable t);
    }
}
