package com.servlet;

import com.dao.CompetitionDao;
import com.dao.FavoriteDao;
import com.entity.Competition;
import com.entity.User;
import com.utils.ActivityBrowseUtil;
import com.utils.RoleUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/CompetitionServlet")
public class CompetitionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CompetitionDao competitionDao = new CompetitionDao();
    private final FavoriteDao favoriteDao = new FavoriteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        User loginUser = getLoginUser(request);

        if ("delete".equals(action)) {
            deleteCompetition(request, response, loginUser);
        } else if ("edit".equals(action)) {
            toEdit(request, response, loginUser);
        } else if ("toAdd".equals(action)) {
            if (!RoleUtil.canPublish(loginUser)) {
                response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
                return;
            }
            request.getRequestDispatcher("/addCompetition.jsp").forward(request, response);
        } else {
            listCompetitions(request, response, loginUser);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        User loginUser = getLoginUser(request);

        if ("add".equals(action)) {
            addCompetition(request, response, loginUser);
        } else if ("update".equals(action)) {
            updateCompetition(request, response, loginUser);
        } else {
            listCompetitions(request, response, loginUser);
        }
    }

    private User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(LoginServlet.SESSION_USER);
    }

    private void listCompetitions(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        List<Competition> list = competitionDao.listAll();
        request.setAttribute("competitionList", list);
        if (loginUser != null) {
            ActivityBrowseUtil.recordListBrowse(request, "competition", 0, "竞赛管理列表");
            request.setAttribute("favoriteKeys", favoriteDao.findKeysByUserId(loginUser.getId()));
        }
        request.getRequestDispatcher("competition.jsp").forward(request, response);
    }

    private void addCompetition(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException {
        if (!RoleUtil.canPublish(loginUser)) {
            response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
            return;
        }

        Competition comp = buildFromRequest(request);
        comp.setUserId(loginUser.getId());
        competitionDao.addCompetition(comp);
        ActivityBrowseUtil.notifyNewActivity(loginUser.getId(), "竞赛", comp.getTitle());
        response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
    }

    private void deleteCompetition(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException, ServletException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Competition comp = competitionDao.findById(id);

        if (!RoleUtil.canDelete(loginUser, comp.getUserId())) {
            listCompetitions(request, response, loginUser);
            return;
        }

        competitionDao.deleteCompetition(id);
        response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
    }

    private void toEdit(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Competition comp = competitionDao.findById(id);

        if (!RoleUtil.canEdit(loginUser, comp.getUserId())) {
            listCompetitions(request, response, loginUser);
            return;
        }

        request.setAttribute("competition", comp);
        request.getRequestDispatcher("/editCompetition.jsp").forward(request, response);
    }

    private void updateCompetition(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Competition existing = competitionDao.findById(id);
        if (!RoleUtil.canEdit(loginUser, existing.getUserId())) {
            response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
            return;
        }

        Competition comp = buildFromRequest(request);
        comp.setId(id);
        competitionDao.updateCompetition(comp);
        response.sendRedirect(request.getContextPath() + "/CompetitionServlet");
    }

    private Competition buildFromRequest(HttpServletRequest request) {
        Competition comp = new Competition();
        comp.setTitle(request.getParameter("title"));
        comp.setTime(request.getParameter("time"));
        comp.setContent(request.getParameter("content"));
        comp.setLocation(request.getParameter("location"));
        return comp;
    }
}
