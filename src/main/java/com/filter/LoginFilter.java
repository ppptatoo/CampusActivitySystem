package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.entity.User;
import com.servlet.LoginServlet;

/**
 * 登录拦截过滤器：未登录用户访问受保护资源时，重定向到登录页。
 * <p>
 * 白名单：登录、注册、静态资源（css/js/图片等）。
 * </p>
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // 无需初始化参数
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.substring(contextPath.length());

        if (isPublicPath(path)) {
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null) {
            Object obj = session.getAttribute(LoginServlet.SESSION_USER);
            if (obj instanceof User) {
                user = (User) obj;
            }
        }

        if (user != null) {
            chain.doFilter(req, resp);
            return;
        }

        response.sendRedirect(contextPath + "/login");
    }

    /**
     * 判断是否为无需登录即可访问的路径。
     */
    private boolean isPublicPath(String path) {
        if (path == null || path.isEmpty() || "/".equals(path)) {
            return false;
        }

        // 登录 / 注册 Servlet 及对应 JSP
        if ("/login".equals(path) || "/register".equals(path) || "/logout".equals(path)
                || "/login.jsp".equals(path) || "/register.jsp".equals(path)) {
            return true;
        }

        // 静态资源
        if (path.startsWith("/css/") || path.startsWith("/js/")
                || path.startsWith("/images/") || path.endsWith(".css")
                || path.endsWith(".js") || path.endsWith(".png")
                || path.endsWith(".jpg") || path.endsWith(".gif")
                || path.endsWith(".ico")) {
            return true;
        }

        return false;
    }

    @Override
    public void destroy() {
        // 无需释放资源
    }
}
