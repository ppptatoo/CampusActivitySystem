package com.servlet;

import com.dao.BrowseRecordDao;
import com.dao.FavoriteDao;
import com.dao.MessageDao;
import com.dao.UserDao;
import com.entity.Favorite;
import com.entity.User;
import com.utils.PasswordUtil;
import com.utils.RoleUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserCenterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDao userDao = new UserDao();
    private final FavoriteDao favoriteDao = new FavoriteDao();
    private final BrowseRecordDao browseRecordDao = new BrowseRecordDao();
    private final MessageDao messageDao = new MessageDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User loginUser = getLoginUser(request, response);
        if (loginUser == null) {
            return;
        }

        String action = request.getParameter("action");
        String ctx = request.getContextPath();

        if ("addFavorite".equals(action)) {
            addFavorite(request, response, loginUser, ctx);
            return;
        }
        if ("removeFavorite".equals(action)) {
            removeFavorite(request, response, loginUser, ctx);
            return;
        }
        if ("markRead".equals(action)) {
            markRead(request, response, loginUser, ctx);
            return;
        }
        if ("clearHistory".equals(action)) {
            browseRecordDao.clearByUserId(loginUser.getId());
            response.sendRedirect(ctx + "/UserCenterServlet?tab=history&msg=cleared");
            return;
        }

        loadTabData(request, loginUser);
        request.getRequestDispatcher("/userCenter.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User loginUser = getLoginUser(request, response);
        if (loginUser == null) {
            return;
        }

        String action = request.getParameter("action");
        String ctx = request.getContextPath();

        if ("updateProfile".equals(action)) {
            updateProfile(request, response, loginUser, ctx);
        } else if ("updatePassword".equals(action)) {
            updatePassword(request, response, loginUser, ctx);
        } else if ("markAllRead".equals(action)) {
            messageDao.markAllRead(loginUser.getId());
            response.sendRedirect(ctx + "/UserCenterServlet?tab=messages&msg=allRead");
        } else {
            response.sendRedirect(ctx + "/UserCenterServlet");
        }
    }

    private User getLoginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        User user = (User) session.getAttribute(LoginServlet.SESSION_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    private void loadTabData(HttpServletRequest request, User loginUser) {
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty()) {
            tab = "profile";
        }
        request.setAttribute("currentTab", tab);

        User freshUser = userDao.findById(loginUser.getId());
        if (freshUser != null) {
            request.setAttribute("profileUser", freshUser.safeCopyForSession());
        }

        request.setAttribute("favoriteList", favoriteDao.listByUserId(loginUser.getId()));
        request.setAttribute("historyList", browseRecordDao.listByUserId(loginUser.getId(), 50));
        request.setAttribute("messageList", messageDao.listByUserId(loginUser.getId()));
        request.setAttribute("unreadCount", messageDao.countUnread(loginUser.getId()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        request.setAttribute("dateFormat", sdf);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, User loginUser, String ctx)
            throws IOException {
        String nickname = trim(request.getParameter("nickname"));
        if (nickname == null) {
            response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=nicknameEmpty");
            return;
        }
        userDao.updateNickname(loginUser.getId(), nickname);
        refreshSessionUser(request, loginUser.getId());
        response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=profileOk");
    }

    private void updatePassword(HttpServletRequest request, HttpServletResponse response, User loginUser, String ctx)
            throws IOException {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty()) {
            response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=pwdEmpty");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=pwdMismatch");
            return;
        }

        User dbUser = userDao.findById(loginUser.getId());
        if (dbUser == null || !PasswordUtil.matches(oldPassword, dbUser.getPassword())) {
            response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=pwdWrong");
            return;
        }

        userDao.updatePassword(loginUser.getId(), PasswordUtil.hash(newPassword));
        response.sendRedirect(ctx + "/UserCenterServlet?tab=profile&msg=pwdOk");
    }

    private void addFavorite(HttpServletRequest request, HttpServletResponse response, User loginUser, String ctx)
            throws IOException {
        String activityType = request.getParameter("activityType");
        String activityIdStr = request.getParameter("activityId");
        String activityTitle = request.getParameter("activityTitle");
        String redirect = request.getParameter("redirect");

        if (activityType == null || activityIdStr == null || activityTitle == null) {
            response.sendRedirect(ctx + "/index.jsp");
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(loginUser.getId());
        favorite.setActivityType(activityType);
        favorite.setActivityId(Integer.parseInt(activityIdStr));
        favorite.setActivityTitle(activityTitle);
        favoriteDao.add(favorite);

        if (redirect != null && !redirect.isEmpty()) {
            response.sendRedirect(redirect);
        } else {
            response.sendRedirect(ctx + "/UserCenterServlet?tab=favorites&msg=favOk");
        }
    }

    private void removeFavorite(HttpServletRequest request, HttpServletResponse response, User loginUser, String ctx)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            favoriteDao.delete(Integer.parseInt(idStr), loginUser.getId());
        }
        response.sendRedirect(ctx + "/UserCenterServlet?tab=favorites&msg=favRemoved");
    }

    private void markRead(HttpServletRequest request, HttpServletResponse response, User loginUser, String ctx)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            messageDao.markRead(Integer.parseInt(idStr), loginUser.getId());
        }
        response.sendRedirect(ctx + "/UserCenterServlet?tab=messages");
    }

    private void refreshSessionUser(HttpServletRequest request, int userId) {
        User dbUser = userDao.findById(userId);
        if (dbUser != null) {
            request.getSession().setAttribute(LoginServlet.SESSION_USER, dbUser.safeCopyForSession());
        }
    }

    private static String trim(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
