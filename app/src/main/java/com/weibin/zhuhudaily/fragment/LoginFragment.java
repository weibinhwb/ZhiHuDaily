package com.weibin.zhuhudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.POSTHttpSendUtil;

/**
 * Created by weibinhuang on 18-3-1.
 */

public class LoginFragment extends Fragment implements AddressInterface{

    private TextView mCheckView;
    private EditText mCheckName;
    private EditText mCheckPassword;
    private Toolbar mToolbar;
    private TextView mHaveOneView;
    static private boolean flag = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login,container,false);
        mCheckView = (TextView) view.findViewById(R.id.check_in);
        mCheckName = (EditText) view.findViewById(R.id.check_name);
        mCheckPassword = (EditText) view.findViewById(R.id.check_password);
        mToolbar = view.findViewById(R.id.toolbar_data);
        mHaveOneView = (TextView) view.findViewById(R.id.have_one);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("注册新账号");
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.png_back);
        }
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCheckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection();
            }
        });
        mHaveOneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    mToolbar.setTitle("登录");
                    mCheckView.setText("登录");
                    mHaveOneView.setText("注册新账号");
                    flag = false;
                } else {
                    mToolbar.setTitle("注册新账号");
                    mHaveOneView.setText("已经有一个账号?");
                    mCheckView.setText("注册");
                    flag = true;
                }
            }
        });
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
    public void sendRequestWithHttpURLConnection() {
        final String name = mCheckName.getText().toString();
        String password = mCheckPassword.getText().toString();
        String body = "username=" + name + "&password=" + password;
        if (name.length() < 5 || password.length() < 8 || name.length() > 10 || password.length() > 12){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"昵称,5~10个字符\n密码,密码8~12个字符",Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mToolbar.getTitle().equals("注册新账号")) {
            POSTHttpSendUtil.sendHttpRequest(checkInAddress, body,new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    if (response.equals("\t1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                mToolbar.setTitle("登录");
                                mCheckView.setText("登录");
                                mHaveOneView.setText("注册新账号");
                                flag = false;
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"昵称已存在,注册失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"请求失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            POSTHttpSendUtil.sendHttpRequest(checkloginAddress, body, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    if (response.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                                Fragment dataFragment = new DataFragment();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putString("data_name",name);
                                dataFragment.setArguments(bundle);
                                fm.popBackStack();
                                ft.remove(fm.findFragmentByTag("frag_login"));
                                ft.hide(fm.findFragmentByTag("frag_home"));
                                ft.add(R.id.frag_home,dataFragment);
                                ft.show(dataFragment);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}
