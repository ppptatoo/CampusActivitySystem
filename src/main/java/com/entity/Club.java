package com.entity;

public class Club {
    private int id;
    private String title;
    private String time;
    private String content;
    private String has_cert;
    private String has_volunteer;
    private String has_prize;
    // 必须加
    private int userId;

    // 无参构造
    public Club() {}

    //  getter/setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getHas_cert() { return has_cert; }
    public void setHas_cert(String has_cert) { this.has_cert = has_cert; }
    public String getHas_volunteer() { return has_volunteer; }
    public void setHas_volunteer(String has_volunteer) { this.has_volunteer = has_volunteer; }
    public String getHas_prize() { return has_prize; }
    public void setHas_prize(String has_prize) { this.has_prize = has_prize; }

    // 关键：userId
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}