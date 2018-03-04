package com.weibin.zhuhudaily.gsondb;

import java.util.List;

/**
 * Created by weibinhuang on 18-2-16.
 */

public class ShortComments {

    public List<ShortComments.shortComments> comments;
    public class shortComments {

        private String author;
        private int id;
        private String content;
        private int likes;
        private long time;
        private String avatar;

        public String getAuthor() {
            return author;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public int getLikes() {
            return likes;
        }

        public long getTime() {
            return time;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    public List<shortComments> getComments() {
        return comments;
    }
}
