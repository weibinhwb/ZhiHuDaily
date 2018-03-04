package com.weibin.zhuhudaily.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.NetWorkUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by weibinhuang on 18-3-3.
 */

public class WelcomeFragment extends Fragment implements AddressInterface{
    private ImageView bingImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_welcome,container,false);
        bingImage = (ImageView) view.findViewById(R.id.welcome_img);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String fileName = getContext().getFilesDir().getPath() + "/bingImage";
        File file = new File(fileName);
        if (file.exists()) {
            DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
            Glide.with(getContext()).load(downLoadUtil.Read("bingImage"))
                    .into(bingImage);
        } else if (new NetWorkUtil().isNetWorkAvailable(getContext())){
            sendRequestWithHttpURLConnection();
        } else {
            Glide.with(getContext()).load(R.drawable.ludashi).into(bingImage);
        }
    }

    @Override
    public void sendRequestWithHttpURLConnection() {
        GETHttpSendUtil.sendHttpRequest(bingpicAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(response).into(bingImage);
                    }
                });
                DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
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
