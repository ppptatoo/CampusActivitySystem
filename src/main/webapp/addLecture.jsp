<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (!RoleUtil.canPublish(loginUser)) {
        response.sendRedirect(request.getContextPath() + "/LectureServlet?action=list");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增讲座 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">新增讲座</h2>
            <form action="${pageContext.request.contextPath}/LectureServlet?action=add" method="post">
                <div class="form-group">
                    <label class="form-label">讲座标题</label>
                    <input class="form-input" type="text" name="title" required placeholder="请输入讲座标题">
                </div>
                <div class="form-group">
                    <label class="form-label">举办时间</label>
                    <input class="form-input" type="text" name="time" required placeholder="例如：2026-06-01 14:00">
                </div>
                <div class="form-group">
                    <label class="form-label">活动地点</label>
                    <input class="form-input" type="text" name="address" required placeholder="请输入活动地点">
                </div>
                <div class="form-group">
                    <label class="form-label">讲座内容</label>
                    <textarea class="form-textarea" name="content" rows="4" required placeholder="请输入讲座内容介绍"></textarea>
                </div>
                <button class="btn-submit" type="submit">提交发布</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/LectureServlet?action=list">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
