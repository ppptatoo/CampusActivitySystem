package com.entity;

public class Lecture {
    private int id;
    private String title;
    private String time;
    private String address;
    private String content;
    private Integer userId; // 发布人ID

    public Lecture() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}