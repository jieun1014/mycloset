package com.example.myapplication.info;

import java.util.ArrayList;

public class WriteBoardInfo {
    private String Category;
    private String Title;
    private String Contents;
    private ArrayList<String> Images;
    private String Writer;
    private String WriteDate;
    private String time;

    public WriteBoardInfo(String Category, String Title, String Contents, ArrayList<String> Images,String Writer, String WriteDate, String time) {
        this.Category = Category;
        this.Title = Title;
        this.Contents = Contents;
        this.Images = Images;
        this.Writer = Writer;
        this.WriteDate = WriteDate;
        this.time = time;
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

    public ArrayList<String> getImages() {
        return Images;
    }

    public void setImages(ArrayList<String> images) {
        Images = images;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getWriteDate() {
        return WriteDate;
    }

    public void setWriteDate(String writeDate) {
        WriteDate = writeDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
