package com.weibin.zhuhudaily.util;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by weibinhuang on 18-2-12.
 * 用文件的方式存储数据
 */

public class DownLoadUtil{
    private Context mContext;
    public DownLoadUtil (Context context) {
        mContext = context;
    }
    public void Write(String mOutFileName ,InputStream mInput) {
        try {
            Log.d("Write",mOutFileName);
            FileOutputStream fos = mContext.openFileOutput(mOutFileName,Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = mInput.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Read(String mInFileName) {
        try {
            Log.d("Read",mInFileName);
            InputStream in = mContext.openFileInput(mInFileName);
            byte[] allByte = new byte[in.available()];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = in.read(allByte)) != -1){
                sb.append(new String(allByte, 0,len));
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
