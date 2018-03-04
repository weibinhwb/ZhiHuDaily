package com.weibin.zhuhudaily.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.fragment.DataFragment;
import com.weibin.zhuhudaily.fragment.LoginFragment;
import com.weibin.zhuhudaily.fragment.HomeFragment;
import com.weibin.zhuhudaily.fragment.WelcomeFragment;
import com.weibin.zhuhudaily.service.DownService;
import com.weibin.zhuhudaily.service.UpdateBingImageService;
import com.weibin.zhuhudaily.service.UpdateNewsService;
import com.weibin.zhuhudaily.util.DownLoadUtil;
import com.weibin.zhuhudaily.util.FragFactory;
import com.weibin.zhuhudaily.util.NavigationItemUtil;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private Handler mHanlder = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WelcomeFragment wf = new WelcomeFragment();
        FragFactory fragFactory = new FragFactory(wf,"frag_welcome",MainActivity.this);
        fragFactory.produceFragOne().commit();
        mHanlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment mHomeFragment = new HomeFragment();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.frag_home,mHomeFragment,"frag_home");
                ft.commit();
            }
        },3000);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.zero);
        NavigationItemUtil nav = new NavigationItemUtil(this,this,navigationView,mDrawerLayout);
        View headLayout = navigationView.inflateHeaderView(R.layout.side_header);
        TextView downIntent = (TextView) headLayout.findViewById(R.id.down_side);
        downIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downService = new Intent(MainActivity.this, DownService.class);
                startService(downService);
                getmDrawerLayout().closeDrawers();
            }
        });
        CardView faceView = (CardView) headLayout.findViewById(R.id.card_face_side);
        faceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(MainActivity.this.getFilesDir().getPath() + "/login_name");
                if (file.exists()){
                    Fragment dataFragment = new DataFragment();
                    DownLoadUtil downLoadUtil = new DownLoadUtil(MainActivity.this);
                    String name = downLoadUtil.Read("login_name");
                    FragFactory fragFactory = new FragFactory(dataFragment,"frag_data",MainActivity.this,"data_name",name);
                    fragFactory.produceFragTwo().hide(getSupportFragmentManager().findFragmentByTag("frag_home")).show(dataFragment)
                            .commit();
                    getmDrawerLayout().closeDrawers();
                } else {
                    Fragment loginFragment = new LoginFragment();
                    FragFactory fragFactory = new FragFactory(loginFragment,"frag_login",MainActivity.this);
                    fragFactory.produceFragOne()
                            .hide(getSupportFragmentManager().findFragmentByTag("frag_home"))
                            .show(loginFragment).commit();
                    getmDrawerLayout().closeDrawers();
                }
            }
        });
        nav.Func();
        Intent updateNews = new Intent(this, UpdateNewsService.class);
        Intent upbingImage = new Intent(this, UpdateBingImageService.class);
        startService(upbingImage);
        startService(updateNews);
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }
}
