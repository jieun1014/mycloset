package com.example.myapplication.info;

public class CommentReadInfo {
    private String CommentWriter;
    private String CommentContent;

    public CommentReadInfo(String CommentWriter, String CommentContent) {
        this.CommentWriter = CommentWriter;
        this.CommentContent = CommentContent;
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
}
