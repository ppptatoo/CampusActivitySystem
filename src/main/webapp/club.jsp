<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.entity.Club" %>
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
    <title>社团活动</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main class="main-content">
    <div class="page-header-card">
        <div>
            <h1 class="page-title">社团活动</h1>
            <p class="page-subtitle">浏览社团活动信息<% if (canPublish) { %>，发布与维护活动<% } %>。</p>
        </div>
        <% if (canPublish) { %>
        <a href="${pageContext.request.contextPath}/ClubServlet?action=toAdd" class="btn-add">+ 发布新活动</a>
        <% } %>
    </div>

    <div class="table-card">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>活动名称</th>
                    <th>时间</th>
                    <th>介绍</th>
                    <th>证书</th>
                    <th>志愿时长</th>
                    <th>奖项</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Club> list = (List<Club>) request.getAttribute("list");
                    if (list != null && !list.isEmpty()) {
                        for (Club club : list) {
                            boolean canEdit = RoleUtil.canEdit(loginUser, club.getUserId());
                            String favKey = "club:" + club.getId();
                            boolean isFav = favoriteKeys.contains(favKey);
                            String encTitle = URLEncoder.encode(club.getTitle(), "UTF-8");

                            // 方案：不定义 ctx，直接在代码块里用 request.getContextPath()
                            String favUrl = request.getContextPath() + "/UserCenterServlet?action=addFavorite&activityType=club&activityId="
                                    + club.getId() + "&activityTitle=" + encTitle
                                    + "&redirect=" + URLEncoder.encode(request.getContextPath() + "/ClubServlet?action=list", "UTF-8");
                %>
                <tr>
                    <td><%= club.getId() %></td>
                    <td><%= club.getTitle() %></td>
                    <td><%= club.getTime() %></td>
                    
                    <td class="detail-cell">
    					<div style="white-space:normal; word-break:break-all; overflow:visible;">
        					<%= club.getContent() %>
    					</div>
					</td>
                    <td><%= club.getHas_cert() %></td>
                    <td><%= club.getHas_volunteer() %></td>
                    <td><%= club.getHas_prize() %></td>
                    <td class="action-cell">
                        <% if (!isFav) { %>
                        <a class="action-link" href="<%= favUrl %>">收藏</a>
                        <% } else { %>
                        <span class="text-muted">已收藏</span>
                        <% } %>
                        <% if (canEdit) { %>
                        <a class="action-link" href="${pageContext.request.contextPath}/ClubServlet?action=toEdit&id=<%= club.getId() %>">编辑</a>
                        <a class="action-delete" href="${pageContext.request.contextPath}/ClubServlet?action=delete&id=<%= club.getId() %>" onclick="return confirm('确定删除？')">删除</a>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr class="empty-row">
                    <td colspan="8">暂无数据</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</main>

</body>
</html>
