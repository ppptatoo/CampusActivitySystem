package com.dao;

import com.entity.Competition;
import com.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetitionDao {

    // 这里必须补 userId
    private Competition mapRow(ResultSet rs) throws SQLException {
        Competition c = new Competition();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setTime(rs.getString("time"));
        c.setContent(rs.getString("content"));
        c.setLocation(rs.getString("location"));
        c.setUserId(rs.getInt("userId")); // 补上！
        return c;
    }

    public List<Competition> listAll() {
        List<Competition> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM competition ORDER BY id ASC";
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

    // INSERT 要加 userId
    public int addCompetition(Competition c) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO competition(title, time, content, location, userId) VALUES(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.getTitle());
            pstmt.setString(2, c.getTime());
            pstmt.setString(3, c.getContent());
            pstmt.setString(4, c.getLocation());
            pstmt.setInt(5, c.getUserId()); // 插入发布人
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, null);
        }
        return rows;
    }

    public int deleteCompetition(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM competition WHERE id=?";
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

    // 方法名和 Servlet 里一致：findById
    public Competition findById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Competition comp = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM competition WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                comp = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, rs);
        }
        return comp;
    }

    // update 不需要改 userId（只改内容）
    public int updateCompetition(Competition comp) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE competition SET title=?, time=?, content=?, location=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, comp.getTitle());
            pstmt.setString(2, comp.getTime());
            pstmt.setString(3, comp.getContent());
            pstmt.setString(4, comp.getLocation());
            pstmt.setInt(5, comp.getId());
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(conn, pstmt, null);
        }
        return rows;
    }
}