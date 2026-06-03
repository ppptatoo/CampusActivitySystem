<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (!RoleUtil.canPublish(loginUser)) {
        response.sendRedirect(request.getContextPath() + "/ClubServlet?action=list");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增社团活动 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">新增社团活动</h2>
            <form action="${pageContext.request.contextPath}/ClubServlet?action=add" method="post">
                <div class="form-group">
                    <label class="form-label">活动名称</label>
                    <input class="form-input" type="text" name="title" required placeholder="请输入活动名称">
                </div>
                <div class="form-group">
                    <label class="form-label">活动时间</label>
                    <input class="form-input" type="text" name="time" required placeholder="例如：2026-06-01">
                </div>
                <div class="form-group">
                    <label class="form-label">活动介绍</label>
                    <textarea class="form-textarea" name="content" rows="3" required placeholder="请输入活动介绍"></textarea>
                </div>
                <div class="form-group">
                    <label class="form-label">是否颁发证书</label>
                    <input class="form-input" type="text" name="has_cert" required placeholder="是 / 否">
                </div>
                <div class="form-group">
                    <label class="form-label">是否计入志愿时长</label>
                    <input class="form-input" type="text" name="has_volunteer" required placeholder="是 / 否">
                </div>
                <div class="form-group">
                    <label class="form-label">是否可评优获奖</label>
                    <input class="form-input" type="text" name="has_prize" required placeholder="是 / 否">
                </div>
                <button class="btn-submit" type="submit">提交发布</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/ClubServlet?action=list">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
