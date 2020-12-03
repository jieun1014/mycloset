package com.example.myapplication.info;

public class ClothReadInfo {
    private String Image;
    private String Did;

    public ClothReadInfo(String Image, String Did) {
        this.Image = Image;
        this.Did = Did;
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
}
