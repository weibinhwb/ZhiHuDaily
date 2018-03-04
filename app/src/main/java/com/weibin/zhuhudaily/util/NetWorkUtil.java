package com.weibin.zhuhudaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by weibinhuang on 18-2-4.
 * 判断当前网络状况能否上网
 */

public class NetWorkUtil {

    public boolean isNetWorkAvailable(Context context) {
        ConnectivityManager cM = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cM != null) {
            NetworkInfo info = cM.getActiveNetworkInfo();
            if (info != null && info.isConnected()){
                if (info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
