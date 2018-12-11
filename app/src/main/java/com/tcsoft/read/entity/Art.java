package com.tcsoft.read.entity;

/**
 * Created by xiansize on 2017/11/27.
 */
public class Art {


    private String title;
    private String type;
    private String author;
    private String totalTime;
    private String content;
    private String artId;
    private String music;
    private int tPage;
    private int cPage;

    public int gettPage() {
        return tPage;
    }

    public void settPage(int tPage) {
        this.tPage = tPage;
    }

    public int getcPage() {
        return cPage;
    }

    public void setcPage(int cPage) {
        this.cPage = cPage;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
