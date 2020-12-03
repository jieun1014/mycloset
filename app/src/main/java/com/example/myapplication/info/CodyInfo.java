package com.example.myapplication.info;

public class CodyInfo {
    private String profile;
    private String title;
    private String contents;

    public CodyInfo(){

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

}

