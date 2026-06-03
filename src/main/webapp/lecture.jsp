<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.entity.Lecture" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.RoleUtil" %>
<%@ page import="java.net.URLEncoder" %>

<%
    User loginUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    boolean canPublish = RoleUtil.canPublish(loginUser);

    Set<String> favoriteKeys = (Set<String>) request.getAttribute("favoriteKeys");
    if (favoriteKeys == null) favoriteKeys = java.util.Collections.emptySet();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>讲座管理 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main class="main-content">
    <div class="page-header-card">
        <div>
            <h1 class="page-title">讲座管理</h1>
            <p class="page-subtitle">浏览、查询校园讲座信息<% if (canPublish) { %>，发布与维护讲座<% } %>。</p>
        </div>
        <% if (canPublish) { %>
        <a href="${pageContext.request.contextPath}/LectureServlet?action=toAdd" class="btn-add">+ 发布新讲座</a>
        <% } %>
    </div>

    <div class="table-card">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>讲座标题</th>
                    <th>时间</th>
                    <th>地点</th>
                    <th>内容</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Lecture> list = (List<Lecture>) request.getAttribute("list");
                    if (list != null && !list.isEmpty()) {
                        for (Lecture lec : list) {
                            boolean canEdit = RoleUtil.canEdit(loginUser, lec.getUserId());

                            String favKey = "lecture:" + lec.getId();
                            boolean isFav = favoriteKeys.contains(favKey);
                            String encTitle = URLEncoder.encode(lec.getTitle(), "UTF-8");
                            
                            // 关键修改：不定义全局 ctx，直接用 request.getContextPath()
                            String favUrl = request.getContextPath() + "/UserCenterServlet?action=addFavorite&activityType=lecture&activityId="
                                    + lec.getId() + "&activityTitle=" + encTitle
                                    + "&redirect=" + URLEncoder.encode(request.getContextPath() + "/LectureServlet?action=list", "UTF-8");
                %>
                <tr>
                    <td><%= lec.getId() %></td>
                    <td><%= lec.getTitle() %></td>
                    <td><%= lec.getTime() %></td>
                    <td><%= lec.getAddress() %></td>
                    <td class="detail-cell" title="<%= lec.getContent() %>">
   						<div style="white-space:normal; word-break:break-all; overflow:visible;">
        					<%= lec.getContent() %>
    					</div>
					</td>
                    <td class="action-cell">
                        <% if (!isFav) { %>
                        <a class="action-link" href="<%= favUrl %>">收藏</a>
                        <% } else { %>
                        <span class="text-muted">已收藏</span>
                        <% } %>
                        <% if (canEdit) { %>
                        <a class="action-link" href="${pageContext.request.contextPath}/LectureServlet?action=toEdit&id=<%= lec.getId() %>">编辑</a>
                        <a class="action-delete" href="${pageContext.request.contextPath}/LectureServlet?action=delete&id=<%= lec.getId() %>" onclick="return confirm('确定删除该讲座？')">删除</a>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr class="empty-row">
                    <td colspan="6">暂无讲座数据</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</main>

</body>
</html>