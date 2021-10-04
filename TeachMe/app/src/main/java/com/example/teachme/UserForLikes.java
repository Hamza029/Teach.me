package com.example.teachme;

public class UserForLikes {

    String flag;
    String likeKey;

    public UserForLikes(String flag, String likeKey) {
        this.flag = flag;
        this.likeKey = likeKey;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLikeKey() {
        return likeKey;
    }

    public void setLikeKey(String likeKey) {
        this.likeKey = likeKey;
    }
}
