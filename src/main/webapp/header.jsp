<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.servlet.LoginServlet" %>
<%@ page import="com.utils.ActivityBrowseUtil" %>

<%
    User currentUser = (User) session.getAttribute(LoginServlet.SESSION_USER);
    String ctx = request.getContextPath();
    String uri = request.getRequestURI();
    
    // 修复：重复变量问题 + 空指针安全
    int unreadCount = 0;
    if (currentUser != null) {
        unreadCount = ActivityBrowseUtil.getUnreadCount(currentUser);
    }
%>

<header class="site-header">

    <a href="<%= ctx %>/index.jsp" class="brand">
        校园活动管理系统<span>Campus Activity</span>
    </a>

    <nav class="site-nav">
        <a href="<%= ctx %>/index.jsp"<%= uri.endsWith("/index.jsp") ? " class=\"active\"" : "" %>>首页</a>
        <a href="<%= ctx %>/LectureServlet?action=list"<%= uri.contains("Lecture") || uri.contains("lecture") ? " class=\"active\"" : "" %>>讲座</a>
        <a href="<%= ctx %>/ClubServlet?action=list"<%= uri.contains("Club") || uri.contains("club") ? " class=\"active\"" : "" %>>社团活动</a>
        <a href="<%= ctx %>/CompetitionServlet"<%= uri.contains("Competition") || uri.contains("competition") ? " class=\"active\"" : "" %>>竞赛</a>
        <a href="<%= ctx %>/AwardServlet"<%= uri.contains("Award") || uri.contains("award") ? " class=\"active\"" : "" %>>评优</a>
        <a href="<%= ctx %>/UserCenterServlet"<%= uri.contains("UserCenter") || uri.contains("userCenter") ? " class=\"active\"" : "" %>>
            用户中心
            <% if (unreadCount > 0) { %>
                <span class="nav-badge"><%= unreadCount %></span>
            <% } %>
        </a>
    </nav>

    <% if (currentUser != null) { %>
    <div class="site-user">
        <span class="user-name">欢迎，<%= currentUser.getNickname() != null && !currentUser.getNickname().isEmpty() ? currentUser.getNickname() : currentUser.getUsername() %></span>
        <span class="user-role"><%= currentUser.getRoleLabel() %></span>
        <a href="<%= ctx %>/logout" class="logout-link">退出登录</a>
    </div>
    <% } %>

</header>
