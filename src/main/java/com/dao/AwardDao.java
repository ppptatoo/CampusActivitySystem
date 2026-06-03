package com.dao;

import com.entity.Award;
import com.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AwardDao {

    // 读取时带上 userId
    private Award mapRow(ResultSet rs) throws SQLException {
        Award award = new Award();
        award.setId(rs.getInt("id"));
        award.setTitle(rs.getString("title"));
        award.setFileName(rs.getString("file_name"));
        award.setFilePath(rs.getString("file_path"));
        award.setPublishTime(rs.getString("publish_time"));
        award.setDetails(rs.getString("details"));
        award.setUserId(rs.getInt("userId")); // 加上
        return award;
    }

    public List<Award> listAll() {
        List<Award> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM award ORDER BY id ASC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, rs);
        }
        return list;
    }

    // 插入时带上 userId
    public int addAward(Award award) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO award(title, file_name, file_path, publish_time, details, userId) VALUES(?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, award.getTitle());
            pstmt.setString(2, award.getFileName());
            pstmt.setString(3, award.getFilePath());
            pstmt.setString(4, award.getPublishTime());
            pstmt.setString(5, award.getDetails());
            pstmt.setInt(6, award.getUserId());
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, null);
        }
        return rows;
    }

    public int updateAward(Award award) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE award SET title=?, publish_time=?, details=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, award.getTitle());
            pstmt.setString(2, award.getPublishTime());
            pstmt.setString(3, award.getDetails());
            pstmt.setInt(4, award.getId());
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, null);
        }
        return rows;
    }

    public int deleteAward(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM award WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, null);
        }
        return rows;
    }

    public Award getAwardById(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Award award = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM award WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                award = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, rs);
        }
        return award;
    }
}