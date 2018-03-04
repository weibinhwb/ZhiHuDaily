package com.weibin.zhuhudaily.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.weibin.zhuhudaily.R;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by weibinhuang on 18-2-14.
 */

public class ArticalShareFragment extends Fragment {
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_share,container,false);
        webView = (WebView) view.findViewById(R.id.web_share_test);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String url = bundle.getString("png_share");
            webView.loadUrl(url);
        }
        return view;
    }
}
