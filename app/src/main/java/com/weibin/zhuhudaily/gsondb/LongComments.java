package com.weibin.zhuhudaily.gsondb;

import java.util.List;

/**
 * Created by weibinhuang on 18-2-6.
 */

public class LongComments {


    private List<Commnt> comments;

    public List<Commnt> getComments() {
        return comments;
    }

    public class Commnt {
        private String author;

        private String content;

        private String avatar;

        private long time;
//
//        private List<LongComments.Commnt.Reply> reply_to;
//
//        class Reply {
//
//            String content;
//
//            int status;
//
//            int id;
//
//            String author;
//
//            String error_msg;
//
//            public String getError_msg() {
//                return error_msg;
//            }
//
//            public String getContent() {
//                return content;
//            }
//
//            public void setContent(String content) {
//                this.content = content;
//            }
//
//            public int getStatus() {
//                return status;
//            }
//
//            public void setStatus(int status) {
//                this.status = status;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getAuthor() {
//                return author;
//            }
//
//            public void setAuthor(String author) {
//                this.author = author;
//            }
//            public List<Reply> getReply_to() {
//                return reply_to;
//            }
//
//        }

        private int id;

        private  int likes;


        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }
    }
}
