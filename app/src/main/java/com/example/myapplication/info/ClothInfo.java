package com.example.myapplication.info;

import com.example.myapplication.ClosetFragments.ClothViewer;

public class ClothInfo {
    private String Category;
    private String Title;
    private String Contents;
    private String uploadTime;

    public ClothInfo(String Category, String Title, String Contents, String uploadTime) {
        this.Category = Category;
        this.Title = Title;
        this.Contents = Contents;
        this.uploadTime = uploadTime;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
