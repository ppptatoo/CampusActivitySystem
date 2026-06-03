package com.servlet;

import com.dao.ClubDao;
import com.dao.FavoriteDao;
import com.entity.Club;
import com.entity.User;
import com.utils.ActivityBrowseUtil;
import com.utils.RoleUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ClubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ClubDao clubDao = new ClubDao();
    private final FavoriteDao favoriteDao = new FavoriteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        String ctx = request.getContextPath();
        User loginUser = getLoginUser(request);
        if (loginUser == null) {
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        try {
            switch (action) {
                case "list":
                    list(request, response, loginUser);
                    break;

                case "toAdd":
                    if (!RoleUtil.canPublish(loginUser)) {
                        response.sendRedirect(ctx + "/ClubServlet?action=list");
                        return;
                    }
                    request.getRequestDispatcher("/addClub.jsp").forward(request, response);
                    break;

                case "add": {
                    if (!RoleUtil.canPublish(loginUser)) {
                        response.sendRedirect(ctx + "/ClubServlet?action=list");
                        return;
                    }
                    Club c = new Club();
                    c.setTitle(request.getParameter("title"));
                    c.setTime(request.getParameter("time"));
                    c.setContent(request.getParameter("content"));
                    c.setHas_cert(request.getParameter("has_cert"));
                    c.setHas_volunteer(request.getParameter("has_volunteer"));
                    c.setHas_prize(request.getParameter("has_prize"));
                    c.setUserId(loginUser.getId());
                    clubDao.add(c);
                    ActivityBrowseUtil.notifyNewActivity(loginUser.getId(), "社团活动", c.getTitle());
                    response.sendRedirect(ctx + "/ClubServlet?action=list");
                    break;
                }

                case "toEdit": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Club c = clubDao.findById(id);
                    if (!RoleUtil.canEdit(loginUser, c.getUserId())) {
                        response.sendRedirect(ctx + "/ClubServlet?action=list");
                        return;
                    }
                    request.setAttribute("club", c);
                    request.getRequestDispatcher("/editClub.jsp").forward(request, response);
                    break;
                }

                case "edit": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Club c = clubDao.findById(id);
                    if (!RoleUtil.canEdit(loginUser, c.getUserId())) {
                        response.sendRedirect(ctx + "/ClubServlet?action=list");
                        return;
                    }
                    Club update = new Club();
                    update.setId(id);
                    update.setTitle(request.getParameter("title"));
                    update.setTime(request.getParameter("time"));
                    update.setContent(request.getParameter("content"));
                    update.setHas_cert(request.getParameter("has_cert"));
                    update.setHas_volunteer(request.getParameter("has_volunteer"));
                    update.setHas_prize(request.getParameter("has_prize"));
                    clubDao.update(update);
                    response.sendRedirect(ctx + "/ClubServlet?action=list");
                    break;
                }

                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Club c = clubDao.findById(id);
                    if (!RoleUtil.canDelete(loginUser, c.getUserId())) {
                        response.sendRedirect(ctx + "/ClubServlet?action=list");
                        return;
                    }
                    clubDao.delete(id);
                    response.sendRedirect(ctx + "/ClubServlet?action=list");
                    break;
                }

                default:
                    list(request, response, loginUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(ctx + "/ClubServlet?action=list");
        }
    }

    private User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(LoginServlet.SESSION_USER);
    }

    private void list(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        List<Club> list = clubDao.listAll();
        request.setAttribute("list", list);
        ActivityBrowseUtil.recordListBrowse(request, "club", 0, "社团活动列表");
        request.setAttribute("favoriteKeys", favoriteDao.findKeysByUserId(loginUser.getId()));
        request.getRequestDispatcher("/club.jsp").forward(request, response);
    }
}
