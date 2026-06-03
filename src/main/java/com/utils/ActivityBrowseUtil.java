package com.utils;

import com.dao.BrowseRecordDao;
import com.dao.MessageDao;
import com.entity.User;
import com.servlet.LoginServlet;

import javax.servlet.http.HttpServletRequest;

/** 活动浏览记录辅助工具 */
public final class ActivityBrowseUtil {

    private static final BrowseRecordDao browseRecordDao = new BrowseRecordDao();
    private static final MessageDao messageDao = new MessageDao();

    private ActivityBrowseUtil() {
    }

    public static void recordListBrowse(HttpServletRequest request, String activityType, int activityId, String activityTitle) {
        User user = (User) request.getSession().getAttribute(LoginServlet.SESSION_USER);
        if (user != null && activityTitle != null && !activityTitle.isEmpty()) {
            browseRecordDao.record(user.getId(), activityType, activityId, activityTitle);
        }
    }

    public static void notifyNewActivity(int publisherId, String activityTypeLabel, String activityTitle) {
        messageDao.notifyActivityPublished(publisherId, activityTypeLabel, activityTitle);
    }

    public static int getUnreadCount(User user) {
        if (user == null) {
            return 0;
        }
        return messageDao.countUnread(user.getId());
    }
}
