package com.example.myapplication.info;

public class CodyWriteInfo {
    private String profile;
    private String title;
    private String contents;

    public CodyWriteInfo(String id, String contents){
        this.title = id;
        this.contents = contents;
    }

    public String getProfile() {
        return profile;
    }

    public String getId() {
        return title;
    }

    public void setId(String id) {
        this.title = id;
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

    @Override
    public String toString() {
        return "CodyInfo{" +
                "profile='" + profile + '\'' +
                ", id='" + title + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}

