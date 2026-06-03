<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户登录 - 校园活动管理系统</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
  <div class="auth-card">
    <h1>用户登录</h1>
    <p class="auth-sub">校园活动管理系统</p>

    <%-- 登录失败提示 --%>
    <%
      Object err = request.getAttribute("error");
      if (err != null) {
    %>
      <div class="auth-msg error"><%= err %></div>
    <% } %>

    <%-- 注册成功后的跳转提示 --%>
    <%
      Object info = request.getAttribute("info");
      if (info != null) {
    %>
      <div class="auth-msg ok"><%= info %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/login">
      <div class="auth-field">
        <label for="username">用户名</label>
        <input type="text" id="username" name="username" required maxlength="50" autocomplete="username" placeholder="请输入用户名">
      </div>
      <div class="auth-field">
        <label for="password">密码</label>
        <input type="password" id="password" name="password" required minlength="6" autocomplete="current-password" placeholder="请输入密码">
      </div>
      <div class="auth-actions">
        <button type="submit" class="auth-btn">登录</button>
        <a class="auth-link" href="<%= request.getContextPath() %>/register">没有账号？去注册</a>
      </div>
    </form>
  </div>
</body>
</html>
