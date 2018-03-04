package com.weibin.zhuhudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.adapter.ArgumentAdapter;
import com.weibin.zhuhudaily.gsondb.LongComments;
import com.weibin.zhuhudaily.gsondb.ShortComments;
import com.weibin.zhuhudaily.util.AddressInterface;
import com.weibin.zhuhudaily.util.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibinhuang on 18-2-15.
 */

public class ArgumentFragment extends Fragment implements AddressInterface{

    private ArgumentAdapter mArgumentAdapter;

    private List<LongComments.Commnt> mArgumentsList = new ArrayList<>();

    private List<ShortComments.shortComments> mShortArgumentList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequestWithHttpURLConnection();
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_arguments,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.argument_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mArgumentAdapter = new ArgumentAdapter(mArgumentsList,mShortArgumentList);
        mRecyclerView.setAdapter(mArgumentAdapter);
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void sendRequestWithHttpURLConnection() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Bundle bundle = getArguments();
        try {
            URL url = new URL(commentAddress + bundle.getString("comments") + "/long-comments");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            connection.disconnect();
            url = new URL(commentAddress + bundle.getString("comments") + "/short-comments");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            final StringBuilder responses = new StringBuilder();
            String lines;
            while ((lines = reader.readLine()) != null){
                responses.append(lines);
            }
            connection.disconnect();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utility utility = new Utility();
                    utility.longComments(response.toString());
                    utility.shortComments(responses.toString());
                    mArgumentAdapter = new ArgumentAdapter(utility.getLongCommentsList(),utility.getShortCommentsList());
                    mRecyclerView.setAdapter(mArgumentAdapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
