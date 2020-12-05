package com.example.myapplication.info;

import java.util.ArrayList;

public class CodyWriteInfo {
    private String Category;
    private ArrayList<String> profile;
    private String title;
    private String contents;
    private String uid;
    private String time;

    public CodyWriteInfo(String Category, String uid, String title, String contents, ArrayList<String> profile, String time) {
        this.Category = Category;
        this.uid = uid;
        this.title = title;
        this.contents = contents;
        this.profile = profile;
        this.time = time;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getProfile() {
        return profile;
    }

    public void setProfile(ArrayList<String> profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

