package com.entity;

import java.util.Date;

/** 浏览记录，对应 browse_record 表 */
public class BrowseRecord {
    private Integer id;
    private Integer userId;
    private String activityType;
    private Integer activityId;
    private String activityTitle;
    private Date browseTime;

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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public Date getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(Date browseTime) {
        this.browseTime = browseTime;
    }

    public String getActivityTypeLabel() {
        if ("lecture".equals(activityType)) return "讲座";
        if ("club".equals(activityType)) return "社团活动";
        if ("competition".equals(activityType)) return "竞赛";
        if ("award".equals(activityType)) return "评优";
        return activityType;
    }
}
