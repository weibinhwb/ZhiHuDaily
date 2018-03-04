package com.weibin.zhuhudaily.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.fragment.HomeFragment;
import com.weibin.zhuhudaily.gsondb.HomeNews;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.Utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by weibinhuang on 18-2-22.
 * 下载服务
 */

public class DownService extends IntentService implements AddressInterface{

    private List<HomeNews.Stories> mStoriesList;
    private List<HomeNews.Top_Stories> mTopStoriesList;

    public DownService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //这个方法是在子线程里面执行的
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            sendRequestWithHttpURLConnection();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"下载完成",Toast.LENGTH_SHORT).show();
    }


    private Notification getNotification(int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getService(this, 0,intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle("下载中...");
        if (progress >= 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    @Override
    public void sendRequestWithHttpURLConnection() {
        startForeground(1,getNotification(0));
        DownLoadUtil downLoadUtil = new DownLoadUtil(this);
        Utility mUtility = new Utility();
        mUtility.News(downLoadUtil.Read("homeNews"));
        mStoriesList = mUtility.getmStorieslist();
        mTopStoriesList = mUtility.getmTorislist();
        for (int i = 0; i < mTopStoriesList.size(); i ++) {
            File file = new File(this.getFilesDir().getPath() + "/artical" + mTopStoriesList.get(i).getId());
            if (file.exists())
                continue;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(articalAddress + mTopStoriesList.get(i).getId());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                downLoadUtil.Write("artical" + mTopStoriesList.get(i).getId(),in);
                url = new URL(extraAddress + mTopStoriesList.get(i).getId());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                in = connection.getInputStream();
                downLoadUtil.Write("comment" + mTopStoriesList.get(i).getId(),in);
                int progress = (100 * i) / (mStoriesList.size() + mTopStoriesList.size());
                getNotificationManager().notify(1,getNotification(progress));
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }
        for (int j = 0; j < mStoriesList.size(); j ++) {
            File file = new File(this.getFilesDir().getPath() + "/artical" + mStoriesList.get(j).getMyId());
            if (file.exists())
                continue;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(articalAddress + mStoriesList.get(j).getMyId());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                downLoadUtil.Write("artical" + mStoriesList.get(j).getMyId(),in);
                url = new URL(extraAddress + mStoriesList.get(j).getMyId());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                in = connection.getInputStream();
                downLoadUtil.Write("comment" + mStoriesList.get(j).getMyId(),in);
                int progress = (100 * (j + mTopStoriesList.size())) / (mStoriesList.size() + mTopStoriesList.size());
                getNotificationManager().notify(1,getNotification(progress));
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }
    }
}
