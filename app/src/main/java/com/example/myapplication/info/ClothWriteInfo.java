package com.example.myapplication.info;

public class ClothWriteInfo {
    private String Category;
    private String Title;
    private String Contents;
    private String uploadTime;
    private String Uid;
    private String Image;

    public ClothWriteInfo(String Category, String Title, String Contents, String uploadTime, String Uid, String Image) {
        this.Category = Category;
        this.Title = Title;
        this.Contents = Contents;
        this.uploadTime = uploadTime;
        this.Uid = Uid;
        this.Image = Image;
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

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
