package com.dao;

import com.entity.Favorite;
import com.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteDao {

    public boolean exists(int userId, String activityType, int activityId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND activity_type = ? AND activity_id = ? LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, activityType);
            ps.setInt(3, activityId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }

    public int add(Favorite favorite) {
        if (exists(favorite.getUserId(), favorite.getActivityType(), favorite.getActivityId())) {
            return 0;
        }
        String sql = "INSERT INTO favorites (user_id, activity_type, activity_id, activity_title) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, favorite.getUserId());
            ps.setString(2, favorite.getActivityType());
            ps.setInt(3, favorite.getActivityId());
            ps.setString(4, favorite.getActivityTitle());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    public int delete(int id, int userId) {
        String sql = "DELETE FROM favorites WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return 0;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    public List<Favorite> listByUserId(int userId) {
        String sql = "SELECT id, user_id, activity_type, activity_id, activity_title, create_time "
                + "FROM favorites WHERE user_id = ? ORDER BY create_time DESC";
        List<Favorite> list = new ArrayList<>();
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

    /** 返回当前用户已收藏的活动键集合，格式 type:id */
    public Set<String> findKeysByUserId(int userId) {
        Set<String> keys = new HashSet<>();
        for (Favorite f : listByUserId(userId)) {
            keys.add(f.getActivityType() + ":" + f.getActivityId());
        }
        return keys;
    }

    private Favorite mapRow(ResultSet rs) throws SQLException {
        Favorite f = new Favorite();
        f.setId(rs.getInt("id"));
        f.setUserId(rs.getInt("user_id"));
        f.setActivityType(rs.getString("activity_type"));
        f.setActivityId(rs.getInt("activity_id"));
        f.setActivityTitle(rs.getString("activity_title"));
        Timestamp ts = rs.getTimestamp("create_time");
        if (ts != null) {
            f.setCreateTime(new java.util.Date(ts.getTime()));
        }
        return f;
    }
}
