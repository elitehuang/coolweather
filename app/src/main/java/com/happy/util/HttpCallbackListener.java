package com.happy.util;

/**
 * Created by 黄金云 on 2017/8/16.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
