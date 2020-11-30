package com.example.myapplication.info;

public class CommentReadInfo {
    private String CommentWriter;
    private String CommentContent;
    private String Cid;
    private String Uid;

    public CommentReadInfo(String CommentWriter, String CommentContent, String Cid, String Uid) {
        this.CommentWriter = CommentWriter;
        this.CommentContent = CommentContent;
        this.Cid = Cid;
        this.Uid = Uid;
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

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
