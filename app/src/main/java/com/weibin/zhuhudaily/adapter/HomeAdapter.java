package com.weibin.zhuhudaily.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.fragment.ArticalFragment;
import com.weibin.zhuhudaily.gsondb.HomeNews;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.FragFactory;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.NetWorkUtil;
import com.weibin.zhuhudaily.util.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by weibinhuang on 18-1-31.
 * 首页新闻适配器
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AddressInterface {

    private String mTime;

    private Context mContext;

    final private long CurrentTime = System.currentTimeMillis();

    private long TargetTime = 0;

    private ViewPagerAdapter mTopStoAdapter;

    private RecyclerView mRecycler;

    private List<HomeNews.Stories> mStoriesList;

    final private int TYPE_PAGER = 1;

    final private int TYPE_TITLE = 2;

    final private int TYPE_LAST = 3;

    final private int HEAD_ = 1;

    final private int MORE_ = 2;

    private int PAST = 0;

    private HomeAdapter mAdapter;

    private Activity mActivity;

    private Handler mHanlder = new Handler();
    /**
     * 构造器
     * @param storiesList 消息列表
     * @param topStoriesAdapter 轮播图的适配器
     * @param rec 存放消息列表的Recyclerview
     * @param homeAdapter Recyclerview的设配器
     */

    public HomeAdapter(List<HomeNews.Stories> storiesList
            , ViewPagerAdapter topStoriesAdapter ,RecyclerView rec
            , HomeAdapter homeAdapter ,Activity activity) {
        mStoriesList = storiesList;
        mTopStoAdapter = topStoriesAdapter;
        mRecycler = rec;
        mAdapter = homeAdapter;
        mActivity = activity;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PAGER;
        }
        else if (position == mStoriesList.size() + 1) {
            return TYPE_LAST;
        }
        else
            return TYPE_TITLE;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_title,parent,false);
            return new TitleViewHolder(view);
        } else if (viewType == TYPE_PAGER) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_pager,parent,false);
            return new PagerViewHolder(view);
        } else if (viewType == TYPE_LAST) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.foot_item,parent,false);
            return new FootViewHolder(view);
        }
        return null;
    }
    @Override
    @TargetApi(16)
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof TitleViewHolder) {
            final HomeNews.Stories news_stories = mStoriesList.get(position - HEAD_);
            ((TitleViewHolder) holder).titleView.setText(news_stories.getTitle());
            ((AppCompatActivity) mActivity).getSupportActionBar().setSubtitle("");
            Glide.with(mContext)
                    .load(Arrays.toString(news_stories.getImages()).replaceAll("^.*\\[", "")
                    .replaceAll("].*", ""))
                    .into(((TitleViewHolder) holder).titleImageView);
            ((TitleViewHolder) holder).storiesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticalFragment af = new ArticalFragment();
                    FragFactory fragFactory = new FragFactory(af,"frag_artical",mContext
                            ,"Id",news_stories.getMyId() + "");
                    fragFactory.produceFragTwo().hide(((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag("frag_home"))
                            .show(af).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
            });
        }
        else if (holder instanceof PagerViewHolder) {
            ((PagerViewHolder) holder).viewPager.setAdapter(mTopStoAdapter);
            final ViewPager mViewPager = ((PagerViewHolder) holder).viewPager;
            mHanlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int currentItem = mViewPager.getCurrentItem();
                    if (currentItem == mViewPager.getAdapter().getCount() - 1){
                        mViewPager.setCurrentItem(1);
                    } else {
                        mViewPager.setCurrentItem(currentItem + 1);
                    }
                    mHanlder.postDelayed(this,3500);
                }
            },3500);
            if (((PagerViewHolder) holder).layout.getChildAt(4) == null){
                for (int i = 0; i < 5; i ++){
                    ImageView point = new ImageView(mContext);
                    point.setImageResource(R.drawable.shape_point_selector);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(15,15);
                    if ( i > 0){
                        params.leftMargin = 10;
                        point.setSelected(false);
                    } else {
                        point.setSelected(true);
                    }
                    point.setLayoutParams(params);
                    ((PagerViewHolder) holder).layout.addView(point);
                }
            }
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                int lastPosition;
                @Override
                public void onPageSelected(int position) {
                    position = position % mTopStoAdapter.getCount();
                    ((PagerViewHolder) holder).layout.getChildAt(position).setSelected(true);
                    ((PagerViewHolder) holder).layout.getChildAt(lastPosition).setSelected(false);
                    lastPosition = position;
                }
                @Override
                public void onPageScrollStateChanged(int state) {}
            });
        } else if (holder instanceof FootViewHolder) {
            if (isVisBottom(mRecycler)){
                mTime = TimeChange();
                ((FootViewHolder) holder).footView.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).footView.setText(">>>>>>>戳我<<<<<<<");
                ((AppCompatActivity) mActivity).getSupportActionBar().setSubtitle(mTime);
                File file = new File(mContext.getFilesDir().getPath() + "/before" + mTime);
                if (file.exists()) {
                    DownLoadUtil loadUtil = new DownLoadUtil(mContext);
                    Utility utility = new Utility();
                    utility.News(loadUtil.Read("before" + mTime));
                    mStoriesList.addAll(utility.getmStorieslist());
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (new NetWorkUtil().isNetWorkAvailable(mContext)){
                        sendRequestWithHttpURLConnection();
                        ((FootViewHolder) holder).footView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((FootViewHolder) holder).footView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        }
    }

    static class PagerViewHolder extends ViewHolder {
        ViewPager viewPager;
        LinearLayout layout;
        private PagerViewHolder(View view) {
            super(view);
            viewPager = (ViewPager) view.findViewById(R.id.pagers_home);
            layout = (LinearLayout) view.findViewById(R.id.pointGroup);
        }
    }
    private static class TitleViewHolder extends RecyclerView.ViewHolder{
        View storiesView;
        CardView cardView;
        ImageView titleImageView;
        TextView titleView;
        private TitleViewHolder (View view) {
            super(view);
            storiesView = view;
            cardView = (CardView) view;
            titleImageView = (ImageView) view.findViewById(R.id.title_image_);
            titleView = (TextView) view.findViewById(R.id.title_text_);
        }
    }
    private static class FootViewHolder extends ViewHolder {
        TextView footView;
        private FootViewHolder(View view) {
            super(view);
            footView = (TextView) view.findViewById(R.id.foot_home);
        }
    }

    @Override
    public int getItemCount() {
        return mStoriesList.size() + MORE_;
    }

    private boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layout.findLastVisibleItemPosition();
        int visibleItemCount = layout.getChildCount();
        return (lastVisibleItemPosition == mStoriesList.size());
    }

    private String TimeChange() {
        TargetTime = CurrentTime - PAST * 24 * 3600 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date_string = sdf.format(new Date(TargetTime));
        PAST++;
        return date_string;
    }
    @Override
    public void sendRequestWithHttpURLConnection() {
        GETHttpSendUtil.sendHttpRequest(beforeAddress + mTime, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    Utility utility = new Utility();
                    utility.News(response);
                    mStoriesList.addAll(utility.getmStorieslist());
                    mAdapter.notifyDataSetChanged();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
                    DownLoadUtil downLoadUtil = new DownLoadUtil(mContext);
                    downLoadUtil.Write("before" + mTime,inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
