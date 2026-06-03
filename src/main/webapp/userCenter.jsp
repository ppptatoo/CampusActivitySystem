<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.entity.Favorite" %>
<%@ page import="com.entity.BrowseRecord" %>
<%@ page import="com.entity.Message" %>

<%
    String currentTab = (String) request.getAttribute("currentTab");
    if (currentTab == null) currentTab = "profile";

    User profileUser = (User) request.getAttribute("profileUser");
    List<Favorite> favoriteList = (List<Favorite>) request.getAttribute("favoriteList");
    List<BrowseRecord> historyList = (List<BrowseRecord>) request.getAttribute("historyList");
    List<Message> messageList = (List<Message>) request.getAttribute("messageList");

    SimpleDateFormat sdf = (SimpleDateFormat) request.getAttribute("dateFormat");
    if (sdf == null) sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    String msg = request.getParameter("msg");
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户中心 - 校园活动管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-center.css">
</head>
<body>

<%@ include file="header.jsp" %>

<main class="main-content">
    <div class="page-header-card">
        <div>
            <h1 class="page-title">用户中心</h1>
            <p class="page-subtitle">管理个人信息、收藏、浏览记录与消息通知。</p>
        </div>
    </div>

    <div class="uc-layout">
        <aside class="uc-sidebar">
            <div class="uc-sidebar-title">功能菜单</div>
            <ul class="uc-tab-list">
                <li><a href="${pageContext.request.contextPath}/UserCenterServlet?tab=profile" class="<%= "profile".equals(currentTab) ? "active" : "" %>">个人信息</a></li>
                <li><a href="${pageContext.request.contextPath}/UserCenterServlet?tab=favorites" class="<%= "favorites".equals(currentTab) ? "active" : "" %>">我的收藏</a></li>
                <li><a href="${pageContext.request.contextPath}/UserCenterServlet?tab=history" class="<%= "history".equals(currentTab) ? "active" : "" %>">浏览记录</a></li>
                <li>
                    <a href="${pageContext.request.contextPath}/UserCenterServlet?tab=messages" class="<%= "messages".equals(currentTab) ? "active" : "" %>">
                        消息中心
                        <%-- 这里直接用 EL 表达式判断，不再定义变量 --%>
                        <c:if test="${unreadCount > 0}">
                            <span class="uc-tab-badge">${unreadCount}</span>
                        </c:if>
                    </a>
                </li>
            </ul>
        </aside>

        <div class="uc-panel">
            <% if ("profileOk".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">个人信息已更新。</div>
            <% } else if ("pwdOk".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">密码修改成功。</div>
            <% } else if ("pwdWrong".equals(msg)) { %>
            <div class="uc-msg uc-msg-error">原密码错误，请重试。</div>
            <% } else if ("pwdMismatch".equals(msg)) { %>
            <div class="uc-msg uc-msg-error">两次输入的新密码不一致。</div>
            <% } else if ("pwdEmpty".equals(msg) || "nicknameEmpty".equals(msg)) { %>
            <div class="uc-msg uc-msg-error">请填写完整信息。</div>
            <% } else if ("favOk".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">收藏成功。</div>
            <% } else if ("favRemoved".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">已取消收藏。</div>
            <% } else if ("allRead".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">全部消息已标记为已读。</div>
            <% } else if ("cleared".equals(msg)) { %>
            <div class="uc-msg uc-msg-success">浏览记录已清空。</div>
            <% } %>

            <% if ("profile".equals(currentTab)) { %>
            <h2 class="uc-panel-title">个人信息</h2>
            <% if (profileUser != null) { %>
            <dl class="uc-info-grid">
                <dt>用户名</dt><dd><%= profileUser.getUsername() %></dd>
                <dt>昵称</dt><dd><%= profileUser.getNickname() != null ? profileUser.getNickname() : "未设置" %></dd>
                <dt>角色</dt><dd><%= profileUser.getRoleLabel() %></dd>
                <dt>注册时间</dt><dd><%= profileUser.getCreateTime() != null ? sdf.format(profileUser.getCreateTime()) : "-" %></dd>
            </dl>
            <% } %>

            <div class="uc-section">
                <h3 class="uc-section-title">修改昵称</h3>
                <form action="${pageContext.request.contextPath}/UserCenterServlet?action=updateProfile" method="post" class="form-shell" style="max-width:100%;padding:0;border:none;box-shadow:none;">
                    <div class="form-group">
                        <label class="form-label">昵称</label>
                        <input class="form-input" type="text" name="nickname" value="<%= profileUser != null && profileUser.getNickname() != null ? profileUser.getNickname() : "" %>" required maxlength="100">
                    </div>
                    <button type="submit" class="btn-submit" style="width:auto;padding:10px 24px;">保存昵称</button>
                </form>
            </div>

            <div class="uc-section">
                <h3 class="uc-section-title">修改密码</h3>
                <form action="${pageContext.request.contextPath}/UserCenterServlet?action=updatePassword" method="post">
                    <div class="form-group">
                        <label class="form-label">原密码</label>
                        <input class="form-input" type="password" name="oldPassword" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">新密码</label>
                        <input class="form-input" type="password" name="newPassword" required minlength="6">
                    </div>
                    <div class="form-group">
                        <label class="form-label">确认新密码</label>
                        <input class="form-input" type="password" name="confirmPassword" required minlength="6">
                    </div>
                    <button type="submit" class="btn-submit" style="width:auto;padding:10px 24px;">修改密码</button>
                </form>
            </div>

            <% } else if ("favorites".equals(currentTab)) { %>
            <h2 class="uc-panel-title">我的收藏</h2>
            <div class="table-card" style="box-shadow:none;border:none;">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>类型</th>
                            <th>活动标题</th>
                            <th>收藏时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (favoriteList != null && !favoriteList.isEmpty()) {
                            for (Favorite f : favoriteList) { %>
                        <tr>
                            <td><%= f.getActivityTypeLabel() %></td>
                            <td><%= f.getActivityTitle() %></td>
                            <td><%= f.getCreateTime() != null ? sdf.format(f.getCreateTime()) : "-" %></td>
                            <td class="action-cell">
                                <a class="action-link" href="${pageContext.request.contextPath}<%= f.getListUrl("") %>">查看</a>
                                <a class="action-delete" href="${pageContext.request.contextPath}/UserCenterServlet?action=removeFavorite&id=<%= f.getId() %>" onclick="return confirm('确定取消收藏？')">取消收藏</a>
                            </td>
                        </tr>
                        <%  }
                           } else { %>
                        <tr class="empty-row"><td colspan="4">暂无收藏，可在活动列表中点击「收藏」</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <% } else if ("history".equals(currentTab)) { %>
            <h2 class="uc-panel-title">浏览记录</h2>
            <div class="uc-inline-actions">
                <a class="btn-secondary" href="${pageContext.request.contextPath}/UserCenterServlet?action=clearHistory" onclick="return confirm('确定清空全部浏览记录？')">清空记录</a>
            </div>
            <div class="table-card" style="box-shadow:none;border:none;">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>类型</th>
                            <th>浏览内容</th>
                            <th>浏览时间</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (historyList != null && !historyList.isEmpty()) {
                            for (BrowseRecord r : historyList) { %>
                        <tr>
                            <td><%= r.getActivityTypeLabel() %></td>
                            <td><%= r.getActivityTitle() %></td>
                            <td><%= r.getBrowseTime() != null ? sdf.format(r.getBrowseTime()) : "-" %></td>
                        </tr>
                        <%  }
                           } else { %>
                        <tr class="empty-row"><td colspan="3">暂无浏览记录</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <% } else if ("messages".equals(currentTab)) { %>
            <h2 class="uc-panel-title">消息中心</h2>
            <div class="uc-inline-actions">
                <form action="${pageContext.request.contextPath}/UserCenterServlet?action=markAllRead" method="post" style="margin:0;">
                    <button type="submit" class="btn-secondary">全部标记已读</button>
                </form>
            </div>
            <% if (messageList != null && !messageList.isEmpty()) {
                for (Message m : messageList) {
                    boolean unread = m.getIsRead() != null && !m.getIsRead();
            %>
            <div class="message-item<%= unread ? " unread" : "" %>">
                <div class="message-item-header">
                    <div>
                        <span class="message-item-title"><%= m.getTitle() %></span>
                        <span class="message-tag <%= Message.TYPE_ACTIVITY.equals(m.getType()) ? "activity" : "" %>"><%= m.getTypeLabel() %></span>
                    </div>
                    <span class="message-item-time"><%= m.getCreateTime() != null ? sdf.format(m.getCreateTime()) : "" %></span>
                </div>
                <div class="message-item-content"><%= m.getContent() %></div>
                <% if (unread) { %>
                <div style="margin-top:10px;">
                    <a class="action-link" href="${pageContext.request.contextPath}/UserCenterServlet?action=markRead&id=<%= m.getId() %>">标记已读</a>
                </div>
                <% } %>
            </div>
            <%  }
               } else { %>
            <p class="text-muted" style="text-align:center;padding:32px 0;">暂无消息</p>
            <% } %>
            <% } %>
        </div>
    </div>
</main>

</body>
</html>