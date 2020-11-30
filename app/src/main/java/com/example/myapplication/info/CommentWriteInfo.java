package com.example.myapplication.info;

public class CommentWriteInfo {
    private String CommentWriter;
    private String CommentContent;
    private String Did;
    private String Time;
    private String Cid;
    private String Uid;

    public CommentWriteInfo(String CommentWriter, String CommentContent, String Did, String Time, String Cid, String Uid) {
        this.CommentWriter = CommentWriter;
        this.CommentContent = CommentContent;
        this.Did = Did;
        this.Time = Time;
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
