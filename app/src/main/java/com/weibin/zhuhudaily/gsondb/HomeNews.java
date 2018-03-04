package com.weibin.zhuhudaily.gsondb;

import com.google.gson.annotations.SerializedName;


import java.util.Arrays;
import java.util.List;

/**
 * Created by weibinhuang on 18-1-29.
 */

public class HomeNews {

    private String date;

    private List<Stories> stories;

    private List<Top_Stories> top_stories;

    public class Stories{

        private String[] images;

        private int type;

        private String img;

        @SerializedName("id")
        private int myid;

        @SerializedName("myId")
        private int id;

        private String ga_prefix;

        private String title;

        public String[] getImages() {
            return images;
        }

        public int getMyId() {
            return myid;
        }

        public void setMyId(int myId) {
            this.myid = myId;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setImages(String[] images) {
            this.images = images;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class Top_Stories {

        public String image;

        public int type;

        public int id;

        public String ga_prefix;

        public String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<HomeNews.Stories> getStories() {
        return stories;
    }

    public void setStories(List<HomeNews.Stories> stories) {
        this.stories = stories;
    }

    public List<HomeNews.Top_Stories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<HomeNews.Top_Stories> top_stories) {
        this.top_stories = top_stories;
    }
}
