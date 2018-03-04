package com.weibin.zhuhudaily.util;

import com.google.gson.Gson;
import com.weibin.zhuhudaily.gsondb.Artical;
import com.weibin.zhuhudaily.gsondb.LongComments;
import com.weibin.zhuhudaily.gsondb.HomeNews;
import com.weibin.zhuhudaily.gsondb.ShortComments;
import com.weibin.zhuhudaily.gsondb.Theme;
import com.weibin.zhuhudaily.gsondb.Version;

import java.util.List;

/**
 * Created by weibinhuang on 18-1-29.
 * 解析工具
 */

public class Utility {

    private String mDate;

    private List<HomeNews.Stories> mStorieslist;

    private List<HomeNews.Top_Stories> mTorislist;

    private List<LongComments.Commnt> longCommentsList;

    private List<ShortComments.shortComments> shortCommentsList;

    private com.weibin.zhuhudaily.gsondb.extralArtical extralArtical;

    private Artical artical;

    private List<Theme.Stories> themestorilists;

    private List<Theme.editors> editorsList;

    private Theme theme;

    private int status;

    private String latest;

    private String msg;

    public void queryVersion (String response) {
        Version version = new Gson().fromJson(response ,Version.class);
        this.status = version.getStatus();
        this.latest = version.getLatest();
        this.msg = version.getMsg();
    }

    /**
     * 评论获取
     * @param response
     */
    public void longComments (String response) {
        LongComments c = new Gson().fromJson(response ,LongComments.class);
        this.longCommentsList = c.getComments();
    }

    public void shortComments (String response) {
        ShortComments c = new Gson().fromJson(response ,ShortComments.class);
        this.shortCommentsList = c.getComments();
    }

    /**
     * 额外信息获取
     * @param response
     */
    public void extrArtical (String response) {
        com.weibin.zhuhudaily.gsondb.extralArtical extralArtical = new Gson().fromJson(response, com.weibin.zhuhudaily.gsondb.extralArtical.class);
        this.extralArtical = extralArtical;
    }

    /**
     * 文章内容获取
     * @param resonse
     */
    public void Content (String resonse) {
        Artical artical = new Gson().fromJson(resonse,Artical.class);
        this.artical = artical;
    }

    /**
     * 主题日报内容获取
     * @param response
     */
    public void Theme(String response) {
        Theme theme = new Gson().fromJson(response, Theme.class);
        this.themestorilists = theme.getStories();
        this.editorsList = theme.getEditors();
        this.theme = theme;
    }

    /**
     * 首页新闻获取
     * @param response
     */
    public void News(String response) {
        try {
            HomeNews homeNews = new Gson().fromJson(response, HomeNews.class);
            this.mStorieslist = homeNews.getStories();//返回Stories里的数据
            this.mTorislist = homeNews.getTop_stories();
            this.mDate = homeNews.getDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 各种getter方法
     * @return 解析后的数据
     */
    public List<HomeNews.Stories> getmStorieslist() {
        return mStorieslist;
    }

    public List<HomeNews.Top_Stories> getmTorislist() {
        return mTorislist;
    }

    public String getmDate() {
        return mDate;
    }

    public List<LongComments.Commnt> getLongCommentsList() {
        return longCommentsList;
    }

    public List<ShortComments.shortComments> getShortCommentsList() {
        return shortCommentsList;
    }

    public com.weibin.zhuhudaily.gsondb.extralArtical getExtralArtical() {
        return extralArtical;
    }

    public Artical getArtical() {
        return artical;
    }

    public List<Theme.Stories> getThemestorilists() {
        return themestorilists;
    }

    public List<Theme.editors> getEditorsList() {
        return editorsList;
    }

    public Theme getTheme() {
        return theme;
    }

    public int getStatus() {
        return status;
    }

    public String getLatest() {
        return latest;
    }

    public String getMsg() {
        return msg;
    }
}
