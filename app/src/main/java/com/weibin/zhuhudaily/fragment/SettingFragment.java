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
import android.widget.TextView;
import android.widget.Toast;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.HttpCallbackListener;
import com.weibin.zhuhudaily.util.GETHttpSendUtil;
import com.weibin.zhuhudaily.util.NetWorkUtil;
import com.weibin.zhuhudaily.util.Utility;

import java.io.File;
import java.math.BigDecimal;


/**
 * Created by weibinhuang on 18-2-24.
 */

public class SettingFragment extends Fragment implements AddressInterface{

    private TextView mQueryVersion;

    private Toolbar mToolbar;

    private TextView mStoreView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting,container,false);
        mToolbar = view.findViewById(R.id.toolbar_setting);
        mStoreView = view.findViewById(R.id.store_setting);
        mStoreView.setTextSize(25);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.png_back);
        }
        setHasOptionsMenu(true);
        mQueryVersion = view.findViewById(R.id.version_setting);
        mQueryVersion.setText("当前版本:2.3.0" + "\t(点击查询)");
        try {
            Long storage = getFolderSize(getContext().getFilesDir());
            mStoreView.setText("文章数据:" + getFormatSize(storage) + "\t(点击清除)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mStoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFilesByDirectory(getContext().getFilesDir());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"清除成功",Toast.LENGTH_SHORT).show();
                        try {
                            Long storage = getFolderSize(getContext().getFilesDir());
                            mStoreView.setText("文章数据:" + getFormatSize(storage));
                        } catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
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
        mQueryVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetWorkUtil().isNetWorkAvailable(getContext()))
                sendRequestWithHttpURLConnection();
            }
        });
    }

    @Override
    public void sendRequestWithHttpURLConnection() {
        GETHttpSendUtil.sendHttpRequest(versionAddress, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final Utility utility = new Utility();
                utility.queryVersion(response);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"最新版本:" + utility.getLatest() +"\n"
                                + utility.getMsg(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
