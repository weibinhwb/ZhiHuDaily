package com.weibin.zhuhudaily.fragment;


import android.content.res.Configuration;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.adapter.HomeAdapter;
import com.weibin.zhuhudaily.adapter.ViewPagerAdapter;
import com.weibin.zhuhudaily.gsondb.HomeNews;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.FragFactory;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.NetWorkUtil;
import com.weibin.zhuhudaily.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibinhuang on 18-2-3.
 * 首页新闻碎片
 * 优先从本地加载缓存;网络连接时,可进行刷新
 */

public class HomeFragment extends Fragment implements AddressInterface {

    private RecyclerView mRecyclerView;

    private List<HomeNews.Stories> mStoriesList = new ArrayList<>();

    private List<HomeNews.Top_Stories> mTstoList = new ArrayList<>();

    private HomeAdapter mAdapter;

    private ViewPagerAdapter viewPagerAdapter;

    private SwipeRefreshLayout mSwipe;

    private Toolbar mToolbar;

    private NetWorkUtil isNet = new NetWorkUtil();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recy_home);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_home);
        mToolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.png_home);
        }
        setHasOptionsMenu(true);
        initRecyclerView();
        initSwipe();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_home,menu);
    }

    /**
     * 侧滑菜单 还没写完
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ((MainActivity) getActivity()).getmDrawerLayout().openDrawer(GravityCompat.START);
                break;
            case R.id.night_mode_item:
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getActivity().getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                getActivity().recreate();
            case R.id.setting_item:
                Fragment mSettingFragment = new SettingFragment();
                FragFactory fragFactory = new FragFactory(mSettingFragment,"frag_setting",getContext());
                fragFactory.produceFragOne().hide(getActivity().getSupportFragmentManager().findFragmentByTag("frag_home"))
                        .show(mSettingFragment).commit();
                break;
            case R.id.bells_item:
                File file = new File(getActivity().getFilesDir().getPath() + "/login_name");
                if (file.exists()){
                    Fragment dataFragment = new DataFragment();
                    DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                    String name = downLoadUtil.Read("login_name");
                    FragFactory factory = new FragFactory(dataFragment,"frag_data",getContext(),"data_name",name);
                    factory.produceFragTwo().hide(getActivity().getSupportFragmentManager().findFragmentByTag("frag_home")).show(dataFragment)
                            .commit();
                } else {
                    Fragment loginFragment = new LoginFragment();
                    FragFactory factory = new FragFactory(loginFragment,"frag_login",getContext());
                    factory.produceFragOne().hide(getActivity().getSupportFragmentManager().findFragmentByTag("frag_home"))
                            .show(loginFragment).commit();
                }
            default:
                break;
        }
        return true;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String fileName = getContext().getFilesDir().getPath() + "/homeNews";
        File file = new File(fileName);
        if (file.exists()) {
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownLoadUtil loadUtil = new DownLoadUtil(getContext());
                        mRecyclerView.setVisibility(View.VISIBLE);
                        initData(loadUtil.Read("homeNews"));
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            if (isNet.isNetWorkAvailable(getContext())) {
                mRecyclerView.setVisibility(View.VISIBLE);
                sendRequestWithHttpURLConnection();
            } else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"没网...",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mTstoList);
        mAdapter = new HomeAdapter(mStoriesList, viewPagerAdapter,mRecyclerView,mAdapter,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSwipe() {
        mSwipe.setColorSchemeResources(R.color.colorefresh);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isNet.isNetWorkAvailable(getContext())) {
                                    sendRequestWithHttpURLConnection();
                                    mSwipe.setRefreshing(false);
                                } else {
                                    Toast.makeText(getContext(),"没网...",Toast.LENGTH_LONG).show();
                                    mSwipe.setRefreshing(false);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initData(String response) {
        Utility mUtility = new Utility();
        mUtility.News(response);
            mStoriesList = mUtility.getmStorieslist();
            mTstoList = mUtility.getmTorislist();
            viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),mTstoList);
            viewPagerAdapter.notifyDataSetChanged();
            mAdapter = new HomeAdapter(mStoriesList, viewPagerAdapter,mRecyclerView,mAdapter,getActivity());
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void sendRequestWithHttpURLConnection() {
        GETHttpSendUtil.sendHttpRequest(newsAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData(response);
                        }
                    });
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
                downLoadUtil.Write("homeNews",inputStream);
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
