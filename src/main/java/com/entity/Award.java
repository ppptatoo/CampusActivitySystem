package com.entity;

/**
 * 评优实体类：对应数据库 award 表
 */
public class Award {
    private Integer id;
    private String title;
    private String fileName;
    private String filePath;
    private String publishTime;
    private String details;
    private Integer userId; // 发布人ID（新增）
    private String username; // 发布人账号（新增，用于页面显示）

    public Award() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    // 新增 getter/setter
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublishDateValue() {
        if (publishTime == null || publishTime.trim().isEmpty()) {
            return "";
        }
        if (publishTime.contains("年") && publishTime.contains("月") && publishTime.contains("日")) {
            String value = publishTime.replace("年", "-").replace("月", "-").replace("日", "");
            String[] parts = value.split("-");
            if (parts.length == 3) {
                return String.format("%s-%02d-%02d", parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
        }
        if (publishTime.contains(" ")) {
            return publishTime.substring(0, publishTime.indexOf(" "));
        }
        return publishTime.replace('/', '-');
    }
}
