<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园活动管理系统 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
<%@ include file="header.jsp" %>
<%
    User user = (User) session.getAttribute(LoginServlet.SESSION_USER);
    String displayName = user != null
            ? (user.getNickname() != null && !user.getNickname().isEmpty() ? user.getNickname() : user.getUsername())
            : "";
    boolean canPublish = RoleUtil.canPublish(user);
%>
<main class="main-content">
    <div class="home-hero">
        <h1>欢迎使用校园活动管理系统</h1>
        <p>您好，<%= displayName %>（<%= user != null ? user.getRoleLabel() : "" %>），
            <% if (canPublish) { %>请选择要管理的模块<% } else { %>请浏览以下活动模块，或在用户中心查看收藏与消息<% } %></p>
    </div>
    <div class="home-cards">
        <a href="${pageContext.request.contextPath}/LectureServlet?action=list" class="home-card">
            <div class="icon">📚</div>
            <div class="label">讲座</div>
        </a>
        <a href="${pageContext.request.contextPath}/ClubServlet?action=list" class="home-card">
            <div class="icon">🎯</div>
            <div class="label">社团活动</div>
        </a>
        <a href="${pageContext.request.contextPath}/CompetitionServlet" class="home-card">
            <div class="icon">🏆</div>
            <div class="label">竞赛</div>
        </a>
        <a href="${pageContext.request.contextPath}/AwardServlet" class="home-card">
            <div class="icon">⭐</div>
            <div class="label">评优</div>
        </a>
        <a href="${pageContext.request.contextPath}/UserCenterServlet" class="home-card">
            <div class="icon">👤</div>
            <div class="label">用户中心</div>
        </a>
    </div>
</main>
</body>
</html>
