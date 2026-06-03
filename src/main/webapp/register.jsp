<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户注册 - 校园活动管理系统</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
  <div class="auth-card">
    <h1>创建账号</h1>
    <p class="auth-sub">校园活动管理系统 · 用户注册</p>

    <%
      Object err = request.getAttribute("error");
      if (err != null) {
    %>
      <div class="auth-msg error"><%= err %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/register">
      <div class="auth-field">
        <label for="username">用户名（3～50 字符，唯一）</label>
        <input type="text" id="username" name="username" required maxlength="50"
               autocomplete="username" placeholder="例如：zhangsan">
      </div>
      <div class="auth-field">
        <label for="password">密码（至少 6 位）</label>
        <input type="password" id="password" name="password" required minlength="6" autocomplete="new-password">
      </div>
      <div class="auth-field">
        <label for="password2">确认密码</label>
        <input type="password" id="password2" name="password2" required minlength="6" autocomplete="new-password">
      </div>
      <div class="auth-field">
        <label for="nickname">昵称（可选）</label>
        <input type="text" id="nickname" name="nickname" maxlength="100" placeholder="展示名称">
      </div>
      <div class="auth-actions">
        <button type="submit" class="auth-btn">注册</button>
        <a class="auth-link" href="<%= request.getContextPath() %>/login">已有账号？去登录</a>
      </div>
    </form>
    <p class="auth-hint">提示：请先执行项目根目录下 <code>sql/init.sql</code> 创建数据库与数据表；并确认 <code>DBUtil</code> 中的库名、账号密码与本地 MySQL 一致。</p>
  </div>
</body>
</html>
