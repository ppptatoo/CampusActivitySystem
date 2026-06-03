<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dao.AwardDao" %>
<%@ page import="com.entity.Award" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    // 【关键】先定义变量，再 include header.jsp
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String idStr = request.getParameter("id");
    if (idStr == null || idStr.trim().isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/AwardServlet");
        return;
    }
    int id = Integer.parseInt(idStr);
    AwardDao dao = new AwardDao();
    Award award = dao.getAwardById(id);

    if (award == null || !RoleUtil.canEdit(loginUser, award.getUserId())) {
        response.sendRedirect(request.getContextPath() + "/AwardServlet");
        return;
    }

    String publishDate = award.getPublishDateValue();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑评优 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">编辑评优</h2>
            <form action="${pageContext.request.contextPath}/AwardServlet?action=update" method="post">
                <input type="hidden" name="id" value="<%= award.getId() %>">
                <div class="form-group">
                    <label class="form-label">评优标题</label>
                    <input class="form-input" type="text" name="title" value="<%= award.getTitle() %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">发布时间</label>
                    <input class="form-input" type="date" name="publishTime" value="<%= publishDate %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">评优详情</label>
                    <textarea class="form-textarea" name="details" rows="5" required><%= award.getDetails() != null ? award.getDetails() : "" %></textarea>
                </div>
                <div class="form-group">
                    <label class="form-label">当前附件</label>
                    <input class="form-input" type="text" value="<%= award.getFileName() != null ? award.getFileName() : "未上传附件" %>" readonly>
                </div>
                <button type="submit" class="btn-submit">保存修改</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/AwardServlet">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>