package com.weibin.zhuhudaily.gsondb;

import java.util.List;

/**
 * Created by weibinhuang on 18-2-8.
 *
 * 主题内容查看
 */

public class Theme {

    private List<Stories> stories;

    private List<Theme.editors> editors;

    public class Stories {

        private String[] images;

        private int id;

//        private int type;

        private String title;

        public String[] getImages() {
            return images;
        }
//
//        public void setImages(String[] images) {
//            this.images = images;
//        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private String description;

    private String background;

    private String name;

    public class editors {

        private String url;

        private String bio;

        private String avatar;

        private String name;

        private String image_source;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage_source() {
            return image_source;
        }

        public void setImage_source(String image_source) {
            this.image_source = image_source;
        }
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public List<editors> getEditors() {
        return editors;
    }

    public void setEditors(List<editors> editors) {
        this.editors = editors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
