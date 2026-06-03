<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dao.ClubDao" %>
<%@ page import="com.entity.Club" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Club club = (Club) request.getAttribute("club");
    if (club == null) {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/ClubServlet?action=list");
            return;
        }
        ClubDao dao = new ClubDao();
        club = dao.findById(Integer.parseInt(idStr));
    }

    if (club == null || !RoleUtil.canEdit(loginUser, club.getUserId())) {
        response.sendRedirect(request.getContextPath() + "/ClubServlet?action=list");
        return;
    }
    request.setAttribute("club", club);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑社团活动 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">编辑社团活动</h2>
            <form action="${pageContext.request.contextPath}/ClubServlet?action=edit" method="post">
                <input type="hidden" name="id" value="${club.id}">
                <div class="form-group">
                    <label class="form-label">活动名称</label>
                    <input class="form-input" type="text" name="title" value="${club.title}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">活动时间</label>
                    <input class="form-input" type="text" name="time" value="${club.time}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">活动介绍</label>
                    <textarea class="form-textarea" name="content" rows="3" required>${club.content}</textarea>
                </div>
                <div class="form-group">
                    <label class="form-label">是否颁发证书</label>
                    <input class="form-input" type="text" name="has_cert" value="${club.has_cert}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">是否计入志愿时长</label>
                    <input class="form-input" type="text" name="has_volunteer" value="${club.has_volunteer}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">是否可评优获奖</label>
                    <input class="form-input" type="text" name="has_prize" value="${club.has_prize}" required>
                </div>
                <button class="btn-submit" type="submit">保存修改</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/ClubServlet?action=list">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
