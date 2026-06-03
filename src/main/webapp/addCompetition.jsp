<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (!RoleUtil.canPublish(loginUser)) {
        response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增竞赛 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">新增竞赛</h2>
            <form action="${pageContext.request.contextPath}/CompetitionServlet?action=add" method="post">
                <div class="form-group">
                    <label class="form-label">竞赛标题</label>
                    <input class="form-input" type="text" name="title" required placeholder="请输入竞赛标题">
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛时间</label>
                    <input class="form-input" type="date" name="time" value="<%= LocalDate.now() %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛地点</label>
                    <input class="form-input" type="text" name="location" required placeholder="请输入竞赛地点">
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛详情</label>
                    <textarea class="form-textarea" name="content" rows="5" required placeholder="请输入竞赛详情"></textarea>
                </div>
                <button type="submit" class="btn-submit">保存</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/CompetitionServlet">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
