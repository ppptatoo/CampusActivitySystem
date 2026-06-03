package com.servlet;

import com.dao.LectureDao;
import com.entity.Lecture;
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
import java.util.Set;

import com.dao.FavoriteDao;

public class LectureServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LectureDao lectureDao = new LectureDao();
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

        try {
            switch (action) {
                case "list": list(request, response, loginUser); break;
                case "toAdd":
                    if (!RoleUtil.canPublish(loginUser)) {
                        response.sendRedirect(ctx + "/LectureServlet?action=list");
                        return;
                    }
                    request.getRequestDispatcher("/addLecture.jsp").forward(request, response);
                    break;
                case "add": add(request, response, ctx, loginUser); break;
                case "delete": delete(request, response, ctx, loginUser); break;
                case "toEdit": toEdit(request, response, loginUser); break;
                case "edit": edit(request, response, ctx, loginUser); break;
                default: list(request, response, loginUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(ctx + "/LectureServlet?action=list");
        }
    }

    private User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(LoginServlet.SESSION_USER);
    }

    private void list(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        List<Lecture> list = lectureDao.listAll();
        request.setAttribute("list", list);
        if (loginUser != null) {
            ActivityBrowseUtil.recordListBrowse(request, "lecture", 0, "讲座管理列表");
            request.setAttribute("favoriteKeys", favoriteDao.findKeysByUserId(loginUser.getId()));
        }
        request.getRequestDispatcher("/lecture.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response, String ctx, User loginUser)
            throws IOException, ServletException {
        if (!RoleUtil.canPublish(loginUser)) {
            response.sendRedirect(ctx + "/LectureServlet?action=list");
            return;
        }

        Lecture l = new Lecture();
        l.setTitle(request.getParameter("title"));
        l.setTime(request.getParameter("time"));
        l.setAddress(request.getParameter("address"));
        l.setContent(request.getParameter("content"));
        l.setUserId(loginUser.getId());

        lectureDao.add(l);
        ActivityBrowseUtil.notifyNewActivity(loginUser.getId(), "讲座", l.getTitle());
        response.sendRedirect(ctx + "/LectureServlet?action=list");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response, String ctx, User loginUser)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Lecture lec = lectureDao.findById(id);

        if (!RoleUtil.canDelete(loginUser, lec.getUserId())) {
            list(request, response, loginUser);
            return;
        }

        lectureDao.delete(id);
        response.sendRedirect(ctx + "/LectureServlet?action=list");
    }

    private void toEdit(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Lecture lec = lectureDao.findById(id);

        if (!RoleUtil.canEdit(loginUser, lec.getUserId())) {
            list(request, response, loginUser);
            return;
        }

        request.setAttribute("lec", lec);
        request.getRequestDispatcher("/editLecture.jsp").forward(request, response);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response, String ctx, User loginUser)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Lecture existing = lectureDao.findById(id);
        if (!RoleUtil.canEdit(loginUser, existing.getUserId())) {
            response.sendRedirect(ctx + "/LectureServlet?action=list");
            return;
        }

        Lecture l = new Lecture();
        l.setId(id);
        l.setTitle(request.getParameter("title"));
        l.setTime(request.getParameter("time"));
        l.setAddress(request.getParameter("address"));
        l.setContent(request.getParameter("content"));
        lectureDao.update(l);
        response.sendRedirect(ctx + "/LectureServlet?action=list");
    }
}
