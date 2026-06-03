package com.entity;

import java.util.Date;

/** 用户收藏，对应 favorites 表 */
public class Favorite {
    private Integer id;
    private Integer userId;
    private String activityType;
    private Integer activityId;
    private String activityTitle;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getActivityTypeLabel() {
        if ("lecture".equals(activityType)) return "讲座";
        if ("club".equals(activityType)) return "社团活动";
        if ("competition".equals(activityType)) return "竞赛";
        if ("award".equals(activityType)) return "评优";
        return activityType;
    }

    public String getListUrl(String contextPath) {
        if ("lecture".equals(activityType)) {
            return contextPath + "/LectureServlet?action=list";
        }
        if ("club".equals(activityType)) {
            return contextPath + "/ClubServlet?action=list";
        }
        if ("competition".equals(activityType)) {
            return contextPath + "/CompetitionServlet";
        }
        if ("award".equals(activityType)) {
            return contextPath + "/AwardServlet";
        }
        return contextPath + "/index.jsp";
    }
}
