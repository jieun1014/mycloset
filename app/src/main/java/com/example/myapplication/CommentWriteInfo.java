package com.example.myapplication;

public class CommentWriteInfo {
    private String CommentWriter;
    private String CommentContent;
    private String Did;
    private String Time;

    public CommentWriteInfo(String CommentWriter, String CommentContent, String Did, String Time) {
        this.CommentWriter = CommentWriter;
        this.CommentContent = CommentContent;
        this.Did = Did;
        this.Time = Time;
    }

    public String getCommentWriter() {
        return CommentWriter;
    }

    public void setCommentWriter(String commentWriter) {
        CommentWriter = commentWriter;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
