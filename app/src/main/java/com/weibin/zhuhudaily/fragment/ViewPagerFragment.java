package com.weibin.zhuhudaily.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.util.FragFactory;


/**
 * Created by weibinhuang on 18-2-3.
 * 轮播图碎片
 */

public class ViewPagerFragment extends Fragment {

    private ImageView mImageview;

    private TextView mTextview;

    private String mImage;

    private String mTitle;

    private int mId;


    public ViewPagerFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getFragmentView(inflater, container);
        return view;
    }


    private void initParams() {
        Bundle bundle = getArguments();

        if (bundle == null) {
            return;
        }
        mImage = bundle.getString("image");
        mTitle = bundle.getString("title");
        mId = bundle.getInt("Id");
    }
    private View getFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_top,container,false);
        mImageview = (ImageView) view.findViewById(R.id.photo_top);
        mTextview = (TextView) view.findViewById(R.id.title_top);
        changeLight(mImageview,-60);
        Glide.with(getContext()).load(mImage).into(mImageview);
        mTextview.setText(mTitle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                ArticalFragment af = new ArticalFragment();
                FragFactory fragFactory = new FragFactory(af,"frag_pager_artical",getContext(),
                        "Id",mId + "");
                fragFactory.produceFragTwo().show(af).commit();
            }
        });
        return view;
    }

    private static void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness,
                                    0, 1, 0, 0, brightness,
                                        0, 0, 1, 0, brightness,
                                            0, 0, 0, 1, 0 });
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
