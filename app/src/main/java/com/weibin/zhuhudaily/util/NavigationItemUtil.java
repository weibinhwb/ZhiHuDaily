package com.weibin.zhuhudaily.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.fragment.HomeFragment;
import com.weibin.zhuhudaily.fragment.ThemeFragment;

/**
 * Created by weibinhuang on 18-2-11.
 * 侧滑菜单的点击事件
 */

public class NavigationItemUtil extends NavigationView {
    private NavigationView mNavgationView = new NavigationView(getContext());
    private DrawerLayout mDrawerLayout;
    private Context mContext;
    private Activity mActivity;
    public NavigationItemUtil(Activity activity,Context context, NavigationView navigationView, DrawerLayout drawerLayout) {
        super(context);
        mContext = context;
        mNavgationView = navigationView;
        mDrawerLayout = drawerLayout;
        mActivity = activity;
    }
    public void Func () {
        mNavgationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment mThemeFragment = new ThemeFragment();
                Fragment mHomeFragment = ((AppCompatActivity) mActivity)
                        .getSupportFragmentManager().findFragmentByTag("frag_home");
                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FragFactory fragFactory = null;
                switch (item.getItemId()){
                    case R.id.zero:
                        fm.popBackStack();
                        ft.show((mHomeFragment));
                        ft.commit();
                        break;
                    case R.id.first:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","13");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.two:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","12");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.three:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","3");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.four:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","11");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.five:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","4");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.six:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","5");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.seven:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","6");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.eghit:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","10");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.nine:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","2");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.ten:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","7");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.eleven:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","9");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                    case R.id.twelve:
                        fragFactory = new FragFactory(mThemeFragment,"frag_theme",getContext(),
                                "ThemeId","8");
                        fragFactory.produceFragTwo().show(mThemeFragment).commit();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }
}
