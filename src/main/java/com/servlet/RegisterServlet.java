package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.MessageDao;
import com.dao.UserDao;
import com.entity.User;
import com.utils.PasswordUtil;

/**
 * 用户注册 Servlet。
 * <ul>
 *   <li>GET：转发到 {@code register.jsp} 展示表单</li>
 *   <li>POST：校验参数 → 用户名唯一性 → 密码摘要入库 → 重定向到登录页</li>
 * </ul>
 */
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Session 中存放注册成功提示的键，登录页可读取并展示 */
    public static final String SESSION_REGISTER_OK = "REGISTER_SUCCESS_MSG";

    private final UserDao userDao = new UserDao();
    private final MessageDao messageDao = new MessageDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 已登录用户无需注册，直接跳转首页
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(LoginServlet.SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 统一 POST 编码，避免中文昵称乱码（Tomcat 默认 ISO-8859-1 对 body 不适用 UTF-8 时需显式设置）
        request.setCharacterEncoding("UTF-8");

        String username = trimToNull(request.getParameter("username"));
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String nickname = trimToNull(request.getParameter("nickname"));

        // 1) 基础校验
        if (username == null || username.length() < 3 || username.length() > 50) {
            request.setAttribute("error", "用户名长度应为 3～50 个字符。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "密码长度至少 6 位。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(password2)) {
            request.setAttribute("error", "两次输入的密码不一致。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 2) 用户名唯一性
        if (userDao.existsByUsername(username)) {
            request.setAttribute("error", "该用户名已被注册，请更换用户名。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 3) 摘要入库
        String hash = PasswordUtil.hash(password);
        if (hash == null) {
            request.setAttribute("error", "服务器内部错误：密码摘要失败，请稍后重试。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hash);
        user.setNickname(nickname);
        user.setRole("user");

        int rows = userDao.insert(user);
        if (rows != 1) {
            request.setAttribute("error", "注册失败，请检查数据库连接或表结构是否正确。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User registered = userDao.findByUsername(username);
        if (registered != null) {
            String displayName = nickname != null ? nickname : username;
            messageDao.sendWelcomeMessage(registered.getId(), displayName);
        }

        // 4) PRG：重定向到登录页，避免刷新重复提交
        request.getSession().setAttribute(SESSION_REGISTER_OK, "注册成功，请使用新账号登录。");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    /** 去首尾空白；全空白则返回 null */
    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
