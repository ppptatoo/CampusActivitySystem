package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.UserDao;
import com.entity.User;
import com.utils.PasswordUtil;

/**
 * 用户登录 Servlet。
 * <ul>
 *   <li>GET：转发到 {@code login.jsp}</li>
 *   <li>POST：校验用户名密码 → 通过则把「不含密码」的用户对象放入 Session</li>
 * </ul>
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Session 中保存当前登录用户的键名。
     * 其它页面可通过 {@code session.getAttribute(LoginServlet.SESSION_USER)} 判断是否已登录。
     */
    public static final String SESSION_USER = "CURRENT_USER";

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 已登录用户直接跳转首页，避免重复登录
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 注册成功后的提示信息（一次性展示）
        if (session != null) {
            Object ok = session.getAttribute(RegisterServlet.SESSION_REGISTER_OK);
            if (ok != null) {
                request.setAttribute("info", ok);
                session.removeAttribute(RegisterServlet.SESSION_REGISTER_OK);
            }
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = trimToNull(request.getParameter("username"));
        String password = request.getParameter("password");
        
        if (username == null || password == null) {
            request.setAttribute("error", "请输入用户名和密码。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User dbUser = userDao.findByUsername(username);
        if (dbUser == null || dbUser.getPassword() == null) {
            request.setAttribute("error", "用户名或密码错误。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

    
        
        if (!PasswordUtil.matches(password, dbUser.getPassword())) {
            request.setAttribute("error", "用户名或密码错误。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 登录成功：Session 中只放「安全副本」，不包含密码摘要
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER, dbUser.safeCopyForSession());

        // 跳转到首页（可按项目需要改为活动列表等）
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
