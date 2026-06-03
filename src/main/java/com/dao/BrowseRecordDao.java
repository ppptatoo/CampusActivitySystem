package com.dao;

import com.entity.BrowseRecord;
import com.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BrowseRecordDao {

    /** 记录浏览：同一用户同一活动仅保留最新一条 */
    public void record(int userId, String activityType, int activityId, String activityTitle) {
        String deleteSql = "DELETE FROM browse_record WHERE user_id = ? AND activity_type = ? AND activity_id = ?";
        String insertSql = "INSERT INTO browse_record (user_id, activity_type, activity_id, activity_title) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return;
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, userId);
            ps.setString(2, activityType);
            ps.setInt(3, activityId);
            ps.executeUpdate();
            ps.close();

            ps = conn.prepareStatement(insertSql);
            ps.setInt(1, userId);
            ps.setString(2, activityType);
            ps.setInt(3, activityId);
            ps.setString(4, activityTitle);
            ps.executeUpdate();

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
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    public List<BrowseRecord> listByUserId(int userId, int limit) {
        String sql = "SELECT id, user_id, activity_type, activity_id, activity_title, browse_time "
                + "FROM browse_record WHERE user_id = ? ORDER BY browse_time DESC LIMIT ?";
        List<BrowseRecord> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return list;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                BrowseRecord r = new BrowseRecord();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setActivityType(rs.getString("activity_type"));
                r.setActivityId(rs.getInt("activity_id"));
                r.setActivityTitle(rs.getString("activity_title"));
                Timestamp ts = rs.getTimestamp("browse_time");
                if (ts != null) {
                    r.setBrowseTime(new java.util.Date(ts.getTime()));
                }
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return list;
    }

    public int clearByUserId(int userId) {
        String sql = "DELETE FROM browse_record WHERE user_id = ?";
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
}
