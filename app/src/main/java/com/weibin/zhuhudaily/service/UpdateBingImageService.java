package com.weibin.zhuhudaily.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.HttpCallbackListener;

import java.io.ByteArrayInputStream;

import static com.weibin.zhuhudaily.util.AddressInterface.bingpicAddress;

public class UpdateBingImageService extends Service {
    public UpdateBingImageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 12 * 1000 * 3600;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, UpdateNewsService.class);
        PendingIntent pi = PendingIntent.getService(this,0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        GETHttpSendUtil.sendHttpRequest(bingpicAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                DownLoadUtil downLoadUtil = new DownLoadUtil(UpdateBingImageService.this);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                downLoadUtil.Write("bingImage",inputStream);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
