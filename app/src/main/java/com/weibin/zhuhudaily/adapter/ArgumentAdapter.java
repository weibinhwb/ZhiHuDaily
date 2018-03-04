package com.weibin.zhuhudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.gsondb.LongComments;
import com.weibin.zhuhudaily.gsondb.ShortComments;

import java.util.List;

/**
 * Created by weibinhuang on 18-2-15.
 * 长评论返回作者,内容,头像,时间,还有其他网友的回复,点赞人数
 * 短评论返回作者,有id,内容,点赞,时间,头像
 */

public class ArgumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LongComments.Commnt> mComments;

    private List<ShortComments.shortComments> mShortComments;

    private Context mContext;

    private final int TYPE_LONG = 0;

    private final int TYPE_SHORT = 1;

    private final int TYPE_LONG_TITLE = 2;

    private final int TYPE_SHORT_TITLE = 3;
    public ArgumentAdapter (List<LongComments.Commnt> commnts,List<ShortComments.shortComments> shortComments){
        mComments = commnts;
        mShortComments = shortComments;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == TYPE_LONG_TITLE){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_long_title,parent,false);
             return new LongTitleAdapter(view);
        } else if (viewType == TYPE_LONG){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_argument,parent,false);
            return new LongArgumentAdapter(view);
        } else if (viewType == TYPE_SHORT) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_argument,parent,false);
            return new ShortArgumentAdapter(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LongArgumentAdapter) {
            Glide.with(mContext).load(mComments.get(position - 1).getAvatar()).into(((LongArgumentAdapter) holder).faceView);
            ((LongArgumentAdapter) holder).nameView.setText(mComments.get(position - 1).getAuthor());
            ((LongArgumentAdapter) holder).argumentView.setText(mComments.get(position - 1).getContent());
        } else if (holder instanceof ShortArgumentAdapter) {
            Glide.with(mContext).load(mShortComments.get(getItemCount() - position - 1).getAvatar()).into(((ShortArgumentAdapter) holder).faceView);
            ((ShortArgumentAdapter) holder).nameView.setText(mShortComments.get(getItemCount() -position - 1).getAuthor());
            ((ShortArgumentAdapter) holder).argumentView.setText(mShortComments.get(getItemCount() - position - 1).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size() + mShortComments.size() + 1;
    }

    private static class LongTitleAdapter extends RecyclerView.ViewHolder {
        TextView titleView;
        public LongTitleAdapter(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.date_home);
        }
    }
    private static class LongArgumentAdapter extends RecyclerView.ViewHolder{
        ImageView faceView;
        TextView nameView;
        TextView argumentView;
        private LongArgumentAdapter(View itemView) {
            super(itemView);
            faceView = (ImageView) itemView.findViewById(R.id.author_face);
            nameView = (TextView) itemView.findViewById(R.id.author_name);
            argumentView = (TextView) itemView.findViewById(R.id.long_argument);
        }
    }

    private static class ShortArgumentAdapter extends RecyclerView.ViewHolder{
        ImageView faceView;
        TextView nameView;
        TextView argumentView;
        private ShortArgumentAdapter(View itemView) {
            super(itemView);
            faceView = (ImageView) itemView.findViewById(R.id.author_face);
            nameView = (TextView) itemView.findViewById(R.id.author_name);
            argumentView = (TextView) itemView.findViewById(R.id.long_argument);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_LONG_TITLE;
        }
        else if (position <= mComments.size()) {
            return TYPE_LONG;
        }
        else
            return TYPE_SHORT;
    }
}
