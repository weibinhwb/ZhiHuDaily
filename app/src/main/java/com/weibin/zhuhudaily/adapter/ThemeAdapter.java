package com.weibin.zhuhudaily.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weibin.zhuhudaily.R;
import com.weibin.zhuhudaily.gsondb.Theme;

import java.util.Arrays;
import java.util.List;

/**
 * Created by weibinhuang on 18-2-8.
 * 主题日报的消息适配器
 */

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;


    private List<Theme.Stories> mThemeStories;

    final private int TYPE_EDITOR = 2;
    final private int TYPE_TITLE = 1;

    final private int TYPE_PHOTO = 0;

    private List<Theme.editors> mEditorslist;

    private Theme mTheme;

    public ThemeAdapter (Theme theme, List<Theme.Stories> storiesList, List<Theme.editors> editorsList
            ){
        this.mThemeStories = storiesList;
        this.mTheme = theme;
        this.mEditorslist = editorsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == TYPE_PHOTO){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.photo_theme, parent, false);
            return new PhotoViewHoler(view);
        }
        else if (viewType == TYPE_TITLE){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.title_theme, parent, false);
            return new TitlerViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (holder instanceof TitlerViewHolder){
            ((TitlerViewHolder)holder).titleView.setText(mThemeStories.get(position).getTitle());
            Glide.with(mContext).load(Arrays.toString(mThemeStories.get(position).getImages())
                    .replaceAll("^.*\\[", "")
                    .replaceAll("].*", ""))
                    .override(220,170)
                    .into(((TitlerViewHolder) holder).titleImageView);
        }
        else if (holder instanceof PhotoViewHoler){
            ((PhotoViewHoler) holder).textView.setText(mTheme.getDescription());
            Glide.with(mContext).load(mTheme.getBackground())
                    .into(((PhotoViewHoler) holder).imageView);
        }
    }
    private static class TitlerViewHolder extends RecyclerView.ViewHolder{
        View v;
        CardView cardView;
        ImageView titleImageView;
        TextView titleView;
        private TitlerViewHolder(View view) {
            super(view);
            v = view;
            cardView = (CardView) view;
            titleImageView = (ImageView) view.findViewById(R.id.photo_theme);
            titleView = (TextView) view.findViewById(R.id.text_theme);
        }
    }

    private static class PhotoViewHoler extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        private PhotoViewHoler(View view) {
            super(view);
            imageView = view.findViewById(R.id.photo_top_theme);
            textView = view.findViewById(R.id.title_top_theme);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_PHOTO;
        }
        else
            return TYPE_TITLE;
    }
    @Override
    public int getItemCount() {
        return mThemeStories.size() ;
    }
}
