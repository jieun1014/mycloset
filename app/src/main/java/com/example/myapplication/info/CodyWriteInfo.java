package com.example.myapplication.info;

public class CodyWriteInfo {
    private String profile;
    private String title;
    private String contents;
    private String uid;

    public CodyWriteInfo(String uid, String title, String contents, String profile){
        this.title = title;
        this.contents = contents;
        this.uid = uid;
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
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

    public void setProfile(String profile) {
        this.profile = profile;
        this.title = title;
        this.contents = contents;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "CodyWriteInfo{" +
                "profile='" + profile + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}


