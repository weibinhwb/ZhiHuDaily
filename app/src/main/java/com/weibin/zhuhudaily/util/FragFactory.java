package com.weibin.zhuhudaily.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.weibin.zhuhudaily.R;

/**
 * Created by weibinhuang on 18-2-25.
 */

public class FragFactory{
    private Fragment mFragment;
    private String mName;
    private Bundle mBundle;
    private String mKey;
    private String mValue;
    private Context mContext;

    public FragFactory(Fragment mFragment,String mName,Context mContext) {
        this.mFragment = mFragment;
        this.mName = mName;
        this.mContext = mContext;
    }

    public FragFactory(Fragment mFragment, String mName,Context mContext, String mKey, String mValue) {
        this.mFragment = mFragment;
        this.mName = mName;
        this.mKey = mKey;
        this.mValue = mValue;
        this.mContext = mContext;
    }

    public FragmentTransaction produceFragOne (){
        FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frag_home,mFragment,mName);
        ft.addToBackStack(null);
        return ft;
    }
    public FragmentTransaction produceFragTwo (){
        FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mBundle = new Bundle();
        mBundle.putString(mKey,mValue);
        mFragment.setArguments(mBundle);
        ft.add(R.id.frag_home,mFragment,mName);
        ft.addToBackStack(null);
        return ft;
    }
}
