package com.example.myapplication;

public class ReadBoardInfo {
    private String Did;
    private String Category;
    private String Title;
    private String Writer;
    private String WriteDate;

    public ReadBoardInfo(String Did, String Category, String Title, String Writer, String WriteDate) {
        this.Did = Did;
        this.Category = Category;
        this.Title = Title;
        this.Writer = Writer;
        this.WriteDate = WriteDate;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
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
}
