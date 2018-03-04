package com.weibin.zhuhudaily.util;

/**
 * Created by weibinhuang on 18-2-15.
 */

public interface HttpCallbackListener {

    void onFinish (String response);

    void onError (Exception e);
}
