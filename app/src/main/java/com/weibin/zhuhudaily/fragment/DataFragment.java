package com.weibin.zhuhudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.activity.MainActivity;
import com.weibin.zhuhudaily.util.DownLoadUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by weibinhuang on 18-3-2.
 */

public class DataFragment extends Fragment {

    private CardView dataCard;
    private TextView dataName;
    private ImageView dataImage;
    private Toolbar mToolbar;
    private Button mCheckOut;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_data,container,false);
        dataCard = (CardView)view.findViewById(R.id.card_face_data);
        dataName = (TextView)view.findViewById(R.id.name_data);
        dataImage = (ImageView)view.findViewById(R.id.face_data);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar_data_new);
        mCheckOut = (Button)view.findViewById(R.id.check_out);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("我的信息");
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.png_back);
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fm.popBackStack();
                ft.show(getActivity().getSupportFragmentManager()
                        .findFragmentByTag("frag_home"));
                ft.commit();
                break;
            default:
        }
        return true;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String name = bundle.getString("data_name");
        DownLoadUtil downLoadUtil = new DownLoadUtil(getContext());
        downLoadUtil.Write("login_name",getStringStream(name));
        dataName.setText(name);
        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getActivity().getFilesDir().getPath() + "/login_name");
                if (file.exists() && file.delete()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"登出成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fm.popBackStack();
                    ft.show(getActivity().getSupportFragmentManager()
                            .findFragmentByTag("frag_home"));
                    ft.commit();
                }
            }
        });

    }
    public InputStream getStringStream (String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")){
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(sInputString.getBytes());
                return inputStream;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
