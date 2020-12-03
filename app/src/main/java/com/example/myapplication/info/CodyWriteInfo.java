package com.example.myapplication.info;

public class CodyWriteInfo {
    private String profile;
    private String id;
    private String contents;

    public CodyWriteInfo(String id, String contents){
        this.id = id;
        this.contents = contents;
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

    @Override
    public String toString() {
        return "CodyInfo{" +
                "profile='" + profile + '\'' +
                ", id='" + id + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}

