package com.weibin.zhuhudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.adapter.ThemeAdapter;
import com.weibin.zhuhudaily.gsondb.Theme;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.NetWorkUtil;
import com.weibin.zhuhudaily.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibinhuang on 18-2-8.
 * 主题新闻碎片
 */

public class ThemeFragment extends Fragment implements AddressInterface {

    private RecyclerView mRec;

    private ThemeAdapter mThemeAdapter;

    private List<Theme.Stories> mThemeStories = new ArrayList<>();

    private List<Theme.editors> mEditorslist = new ArrayList<>();

    private Theme mTheme = new Theme();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_theme,container,false);
        mRec = view.findViewById(R.id.theme_recycler);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        mRec.setLayoutManager(layoutManager);
        mThemeAdapter = new ThemeAdapter(mTheme,mThemeStories,mEditorslist);
        mRec.setAdapter(mThemeAdapter);
        Toolbar mToolbar = view.findViewById(R.id.toolbar_face);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.png_home);
        }
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getmDrawerLayout().openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String id = bundle.getString("ThemeId");
        if (new NetWorkUtil().isNetWorkAvailable(getContext())){
            sendRequestWithHttpURLConnection();
        } else {
            File file = new File(getContext().getFilesDir().getPath() + "/" +
                    "theme" + id);
            if (file.exists()) {
                showTheme(new DownLoadUtil(getContext()).Read("theme" + id));
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"加载失败,我猜是没网",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    @Override
    public void sendRequestWithHttpURLConnection() {
        Bundle bundle = getArguments();
        final String ThemeId = bundle.getString("ThemeId");
        GETHttpSendUtil.sendHttpRequest(themeAddress + ThemeId,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        showTheme(response);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                        DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                        downLoadUtil.Write("theme" + ThemeId,inputStream);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    private void showTheme(final String response){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Utility utility = new Utility();
                utility.Theme(response);
                mThemeStories = utility.getThemestorilists();
                mTheme = utility.getTheme();
                mEditorslist = utility.getEditorsList();
                mThemeAdapter = new ThemeAdapter(mTheme,mThemeStories,mEditorslist);
                mRec.setAdapter(mThemeAdapter);
            }
        });
    }
}
