package com.entity;

import java.util.Date;

/** 用户消息，对应 messages 表（系统通知 / 活动提醒） */
public class Message {
    public static final String TYPE_SYSTEM = "system";
    public static final String TYPE_ACTIVITY = "activity";

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String type;
    private Boolean isRead;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTypeLabel() {
        return TYPE_ACTIVITY.equals(type) ? "活动提醒" : "系统通知";
    }
}
