<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dao.CompetitionDao" %>
<%@ page import="com.entity.Competition" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Competition comp = (Competition) request.getAttribute("competition");
    if (comp == null) {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
            return;
        }
        CompetitionDao dao = new CompetitionDao();
        comp = dao.findById(Integer.parseInt(idStr));
    }

    if (comp == null || !RoleUtil.canEdit(loginUser, comp.getUserId())) {
        response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
        return;
    }

    String competitionDate = comp.getTime() != null ? comp.getTime().replace('/', '-') : "";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑竞赛 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="form-shell">
        <div class="form-page">
            <h2 class="form-page-title">编辑竞赛</h2>
            <form action="${pageContext.request.contextPath}/CompetitionServlet?action=update" method="post">
                <input type="hidden" name="id" value="<%= comp.getId() %>">
                <div class="form-group">
                    <label class="form-label">竞赛标题</label>
                    <input class="form-input" type="text" name="title" value="<%= comp.getTitle() %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛时间</label>
                    <input class="form-input" type="date" name="time" value="<%= competitionDate %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛地点</label>
                    <input class="form-input" type="text" name="location" value="<%= comp.getLocation() != null ? comp.getLocation() : "" %>" required>
                </div>
                <div class="form-group">
                    <label class="form-label">竞赛详情</label>
                    <textarea class="form-textarea" name="content" rows="5" required><%= comp.getContent() %></textarea>
                </div>
                <button type="submit" class="btn-submit">保存修改</button>
            </form>
            <a class="back-link" href="${pageContext.request.contextPath}/CompetitionServlet">返回列表</a>
        </div>
    </div>
</main>
</body>
</html>
