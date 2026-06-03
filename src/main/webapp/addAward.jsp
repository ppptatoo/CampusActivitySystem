<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (!RoleUtil.canPublish(loginUser)) {
        response.sendRedirect(request.getContextPath() + "/AwardServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>发布评优 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">发布评优</h2>
            <form action="${pageContext.request.contextPath}/AwardServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label class="form-label">评优标题</label>
                    <input class="form-input" type="text" name="title" required placeholder="请输入评优标题">
                </div>
                <div class="form-group">
                    <label class="form-label">发布时间</label>
                    <input class="form-input" type="date" name="publishTime" value="<%= LocalDate.now() %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">评优详情</label>
                    <textarea class="form-textarea" name="details" rows="5" placeholder="请输入评优详情说明"></textarea>
                </div>
                <div class="form-group">
                    <label class="form-label">附件上传</label>
                    <input class="form-input" type="file" name="file" required>
                </div>
                <button type="submit" class="btn-submit">发布</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/AwardServlet">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
