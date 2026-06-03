package com.dao;

import com.entity.Message;
import com.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    public int insert(Message message) {
        String sql = "INSERT INTO messages (user_id, title, content, type, is_read) VALUES (?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, message.getUserId());
            ps.setString(2, message.getTitle());
            ps.setString(3, message.getContent());
            ps.setString(4, message.getType());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    /** 新活动发布时，向除发布者外的所有用户发送活动提醒 */
    public void notifyActivityPublished(int publisherId, String activityTypeLabel, String activityTitle) {
        String usersSql = "SELECT id FROM users WHERE id <> ?";
        String insertSql = "INSERT INTO messages (user_id, title, content, type, is_read) VALUES (?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement psUsers = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return;
            conn.setAutoCommit(false);

            psUsers = conn.prepareStatement(usersSql);
            psUsers.setInt(1, publisherId);
            rs = psUsers.executeQuery();

            psInsert = conn.prepareStatement(insertSql);
            String title = "新活动发布：" + activityTitle;
            String content = "【" + activityTypeLabel + "】发布了新活动「" + activityTitle + "」，请及时查看。";

            while (rs.next()) {
                psInsert.setInt(1, rs.getInt("id"));
                psInsert.setString(2, title);
                psInsert.setString(3, content);
                psInsert.setString(4, Message.TYPE_ACTIVITY);
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBUtil.closeQuietly(rs, psUsers, null);
            DBUtil.closeQuietly(null, psInsert, conn);
        }
    }

    /** 注册成功后发送欢迎系统通知 */
    public void sendWelcomeMessage(int userId, String nickname) {
        Message msg = new Message();
        msg.setUserId(userId);
        msg.setTitle("欢迎使用校园活动管理系统");
        msg.setContent("您好，" + nickname + "！您已成功注册，可以浏览讲座、社团、竞赛与评优信息，并在用户中心管理收藏与消息。");
        msg.setType(Message.TYPE_SYSTEM);
        insert(msg);
    }

    public List<Message> listByUserId(int userId) {
        String sql = "SELECT id, user_id, title, content, type, is_read, create_time "
                + "FROM messages WHERE user_id = ? ORDER BY create_time DESC";
        List<Message> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return list;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int countUnread(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE user_id = ? AND is_read = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return 0;
    }

    public int markRead(int messageId, int userId) {
        String sql = "UPDATE messages SET is_read = 1 WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    public int markAllRead(int userId) {
        String sql = "UPDATE messages SET is_read = 1 WHERE user_id = ? AND is_read = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    private Message mapRow(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setId(rs.getInt("id"));
        m.setUserId(rs.getInt("user_id"));
        m.setTitle(rs.getString("title"));
        m.setContent(rs.getString("content"));
        m.setType(rs.getString("type"));
        m.setIsRead(rs.getInt("is_read") == 1);
        Timestamp ts = rs.getTimestamp("create_time");
        if (ts != null) {
            m.setCreateTime(new java.util.Date(ts.getTime()));
        }
        return m;
    }
}
