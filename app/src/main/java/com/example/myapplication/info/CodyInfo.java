package com.example.myapplication.info;

public class CodyInfo {
    private String profile;
    private String id;
    private String contents;

    public CodyInfo(){

    }

    public String getProfile() {
        return profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setProfile(String profile) {
        this.profile = profile;
        this.id = id;
        this.contents = contents;
    }

}

