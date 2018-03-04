package com.weibin.zhuhudaily.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.HttpCallbackListener;

import java.io.ByteArrayInputStream;

public class UpdateNewsService extends Service implements AddressInterface{
    public UpdateNewsService() {
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        updateNews();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000 * 3600;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, UpdateNewsService.class);
        PendingIntent pi = PendingIntent.getService(this,0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        GETHttpSendUtil.sendHttpRequest(bingpicAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                DownLoadUtil downLoadUtil = new DownLoadUtil(UpdateNewsService.this);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                downLoadUtil.Write("bingImage",inputStream);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void updateNews() {
        GETHttpSendUtil.sendHttpRequest(newsAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                DownLoadUtil downLoadUtil = new DownLoadUtil(UpdateNewsService.this);
                downLoadUtil.Write("homeNews",inputStream);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendRequestWithHttpURLConnection() {

    }
}
