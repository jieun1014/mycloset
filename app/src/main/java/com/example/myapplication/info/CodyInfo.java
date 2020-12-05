package com.example.myapplication.info;

public class CodyInfo {
    private String Did;
    private String Category;
    private String title;
    private String contents;

    public CodyInfo(String Did, String Category, String title, String contents){
        this.Did = Did;
        this.title = title;
        this.contents = contents;
        this.Category = Category;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
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

}

