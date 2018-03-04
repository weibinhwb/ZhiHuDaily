package com.weibin.zhuhudaily.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by weibinhuang on 18-3-2.
 */

public class POSTHttpSendUtil {
    public static void sendHttpRequest (final String address,final String body,
                                        final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setRequestProperty("Content-Length",String.valueOf(body.length()));
                    httpURLConnection.setRequestProperty("Cache-Control","max-age=0");
                    httpURLConnection.setRequestProperty("Origin",address);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.getOutputStream().write(body.getBytes());
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream is = httpURLConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int len = 0;
                        byte buffer[] = new byte[1024];
                        while ((len = is.read(buffer)) != -1) {
                            baos.write(buffer,0,len);
                        }
                        is.close();
                        baos.close();
                        String result = new String(baos.toByteArray());
                        if (listener != null) {
                            listener.onFinish(result);
                        }
                }
            } catch (Exception e){
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
                finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}
