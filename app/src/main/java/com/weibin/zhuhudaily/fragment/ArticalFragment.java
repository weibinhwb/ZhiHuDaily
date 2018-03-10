package com.weibin.zhuhudaily.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.gsondb.Artical;
import com.weibin.zhuhudaily.gsondb.extralArtical;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.FragFactory;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.NetWorkUtil;
import com.weibin.zhuhudaily.util.POSTHttpSendUtil;
import com.weibin.zhuhudaily.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URLEncoder;


/**
 * Created by weibinhuang on 18-2-2.
 * 新闻详情碎片
 *加载的同时缓存了,下次打开就是缓存的
 */

public class ArticalFragment extends Fragment implements AddressInterface {

    private WebView webview_arti;
    private TextView popu_arti, comme_arti, title_arti, image_source_arti,collec_arti,back_arti,share_arti;
    private ImageView photo_arti;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        bundle.getString("Id");
        String fileName = getContext().getFilesDir().getPath() + "/comment" + bundle.getString("Id");
        File file = new File(fileName);
        if (!file.exists() && new NetWorkUtil().isNetWorkAvailable(getContext())) {
            sendRequestWithHttpURLConnection();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_artical,container,false);
        webview_arti = view.findViewById(R.id.web_view_);
        back_arti = view.findViewById(R.id.back_artical);
        share_arti = view.findViewById(R.id.share_artical);
        collec_arti = view.findViewById(R.id.collec_arc);
        popu_arti = view.findViewById(R.id.popularity_artical);
        comme_arti = view.findViewById(R.id.comments_artical);
        photo_arti = view.findViewById(R.id.photo_artical);
        title_arti = view.findViewById(R.id.title_artical);
        image_source_arti = view.findViewById(R.id.image_source);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        final String id = bundle.getString("Id");
        String fileName = getContext().getFilesDir().getPath() + "/artical" + id;
        File file = new File(fileName);
        if (file.exists()) {
            try {
                DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                showComments(downLoadUtil.Read("comment" + id));
                showArtical(downLoadUtil.Read("artical" + id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            back_arti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fm.popBackStack();
                    ft.show(fm.findFragmentByTag("frag_home"));
                    ft.commit();
                }
            });
        } catch (Exception e ){
            e.printStackTrace();
        }
        comme_arti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArgumentFragment af = new ArgumentFragment();
                FragFactory fragFactory = new FragFactory(af,"frag_comment",getContext(),"comments",id);
                fragFactory.produceFragTwo().commit();
            }
        });
        collec_arti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collecFunc();
            }
        });
    }

    @Override
    public void sendRequestWithHttpURLConnection() {
        Bundle bundle = getArguments();
        final String id = bundle.getString("Id");
        GETHttpSendUtil.sendHttpRequest(articalAddress + id,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        showArtical(response);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                        downLoadUtil.Write("artical" + id,inputStream);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
        GETHttpSendUtil.sendHttpRequest(extraAddress + id,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        showComments(response);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                        downLoadUtil.Write("comment" + id,inputStream);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    /**
     * 感谢热心网友的共享
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName('img'); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "var img = objs[i];   " +
                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                    "}" +
                    "})()");
            view.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistner.openImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class JavaScriptInterface {
        private Context context;
        private JavaScriptInterface(Context context) {
            this.context = context;
        }
        @JavascriptInterface
        public void openImage(String img) {
            BigImageFragment bif = new BigImageFragment();
            FragFactory fragFactory = new FragFactory(bif,"frag_bigImage",getContext(),"Image",img);
            fragFactory.produceFragTwo().commit();
        }
    }
    private void showComments(final String responseText) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utility utility = new Utility();
                utility.extrArtical(responseText);
                final extralArtical extralArtical = utility.getExtralArtical();
                try {
                    comme_arti.setText(extralArtical.getComments() + "");
                    popu_arti.setText(extralArtical.getPopularity() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void collecFunc () {
        Bundle bundle = getArguments();
        String id = bundle.getString("Id");
        Utility utility = new Utility();
        DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
        String name = downLoadUtil.Read("login_name");
        String content = downLoadUtil.Read("artical" + id);
        utility.Content(content);
        Artical articals = utility.getArtical();
        String title = articals.getTitle();
        try {
            name = URLEncoder.encode(name,"utf-8");
            title = URLEncoder.encode(title, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String body = "username=" + name + "&likes=" + title;
        POSTHttpSendUtil.sendHttpRequest(collcectAddress, body, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (response.equals("1")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void showArtical(String responseText){
        final Utility utility = new Utility();
        utility.Content(responseText);
        final Artical artical = utility.getArtical();
        Log.d("weibin", artical.getBody());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d("photoImage", "" + photo_arti);
                Glide.with(getContext())
                        .load(artical.getImage()).into(photo_arti);

                share_arti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT,artical.getShare_url());
                        startActivity(Intent.createChooser(sendIntent,"分享到..."));
                    }
                });
                title_arti.setText(artical.getTitle());
                image_source_arti.setText(artical.getImage_source());
                Log.d("at artical ",artical.getImage_source());
                WebSettings webSettings = webview_arti.getSettings();
                webview_arti.loadData(artical.getBody(), "text/html; charset=UTF-8", null);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webview_arti.setWebViewClient(new MyWebViewClient());
                webview_arti.addJavascriptInterface(new JavaScriptInterface(getContext()), "imagelistner");
                webSettings.setJavaScriptEnabled(true);//支持js
                webSettings.setDomStorageEnabled(true);
                webSettings.setDefaultTextEncodingName("UTF-8");
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            }
        });
    }
}
