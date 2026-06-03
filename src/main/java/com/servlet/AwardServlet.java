package com.servlet;

import com.dao.AwardDao;
import com.dao.FavoriteDao;
import com.entity.Award;
import com.entity.User;
import com.utils.ActivityBrowseUtil;
import com.utils.RoleUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/AwardServlet")
public class AwardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AwardDao awardDao = new AwardDao();
    private final FavoriteDao favoriteDao = new FavoriteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        User loginUser = getLoginUser(request);

        if ("delete".equals(action)) {
            deleteAward(request, response, loginUser);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Award award = awardDao.getAwardById(id);
            if (!RoleUtil.canEdit(loginUser, award.getUserId())) {
                listAwards(request, response, loginUser);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/editAward.jsp?id=" + id);
        } else if ("toAdd".equals(action)) {
            if (!RoleUtil.canPublish(loginUser)) {
                response.sendRedirect(request.getContextPath() + "/AwardServlet");
                return;
            }
            request.getRequestDispatcher("/addAward.jsp").forward(request, response);
        } else {
            listAwards(request, response, loginUser);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        User loginUser = getLoginUser(request);

        if (!RoleUtil.canPublish(loginUser)) {
            response.sendRedirect(request.getContextPath() + "/AwardServlet");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Award award = awardDao.getAwardById(id);
            if (!RoleUtil.canEdit(loginUser, award.getUserId())) {
                listAwards(request, response, loginUser);
                return;
            }
            updateAward(request, response);
            return;
        }

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            String title = "";
            String details = "";
            String fileName = "";
            String filePath = "";
            String publishTime = "";

            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String value = item.getString("UTF-8");
                    if ("title".equals(fieldName)) title = value;
                    else if ("details".equals(fieldName)) details = value;
                    else if ("publishTime".equals(fieldName)) publishTime = value;
                } else if (item.getSize() > 0) {
                    fileName = new File(item.getName()).getName();
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    String saveFileName = uuid + "_" + fileName;
                    String uploadPath = getServletContext().getRealPath("/upload");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();
                    File saveFile = new File(uploadDir, saveFileName);
                    item.write(saveFile);
                    filePath = "/upload/" + saveFileName;
                }
            }

            if (title.isEmpty() || fileName.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/addAward.jsp?msg=fail");
                return;
            }

            if (publishTime == null || publishTime.trim().isEmpty()) {
                publishTime = java.time.LocalDate.now().toString();
            }

            Award award = new Award();
            award.setTitle(title);
            award.setFileName(fileName);
            award.setFilePath(filePath);
            award.setPublishTime(publishTime);
            award.setDetails(details);
            award.setUserId(loginUser.getId());

            int rows = awardDao.addAward(award);
            if (rows > 0) {
                ActivityBrowseUtil.notifyNewActivity(loginUser.getId(), "评优", title);
                response.sendRedirect(request.getContextPath() + "/AwardServlet");
            } else {
                response.sendRedirect(request.getContextPath() + "/addAward.jsp?msg=dbFail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/addAward.jsp?msg=error");
        }
    }

    private User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(LoginServlet.SESSION_USER);
    }

    private void listAwards(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        List<Award> awardList = awardDao.listAll();
        request.setAttribute("awardList", awardList);
        if (loginUser != null) {
            ActivityBrowseUtil.recordListBrowse(request, "award", 0, "评优管理列表");
            request.setAttribute("favoriteKeys", favoriteDao.findKeysByUserId(loginUser.getId()));
        }
        request.getRequestDispatcher("award.jsp").forward(request, response);
    }

    private void deleteAward(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Award award = awardDao.getAwardById(id);

        if (!RoleUtil.canDelete(loginUser, award.getUserId())) {
            listAwards(request, response, loginUser);
            return;
        }

        if (award.getFilePath() != null) {
            String realPath = getServletContext().getRealPath(award.getFilePath());
            File delFile = new File(realPath);
            if (delFile.exists()) delFile.delete();
        }
        awardDao.deleteAward(id);
        response.sendRedirect(request.getContextPath() + "/AwardServlet");
    }

    private void updateAward(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Award award = new Award();
        award.setId(id);
        award.setTitle(request.getParameter("title"));
        award.setPublishTime(request.getParameter("publishTime"));
        award.setDetails(request.getParameter("details"));
        awardDao.updateAward(award);
        response.sendRedirect(request.getContextPath() + "/AwardServlet");
    }
}
