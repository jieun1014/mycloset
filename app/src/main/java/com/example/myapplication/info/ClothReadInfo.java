package com.example.myapplication.info;

public class ClothReadInfo {
    private String Image;
    private String Did;
    private String Title;

    public ClothReadInfo(String Image, String Did, String Title) {
        this.Image = Image;
        this.Did = Did;
        this.Title = Title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
