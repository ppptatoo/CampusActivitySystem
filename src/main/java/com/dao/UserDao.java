package com.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.entity.User;
import com.utils.DBUtil;

public class UserDao {

    /**
     * 判断用户名是否已存在（注册时唯一性校验）。
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }

    /**
     * 插入新用户（密码应为 {@link com.utils.PasswordUtil#hash(String)} 后的摘要）。
     *
     * @param user 实体（username、password 必填；nickname 可空）
     * @return 受影响行数，正常为 1；失败为 0
     */
    public int insert(User user) {
        String sql = "INSERT INTO users (username, password, nickname, role) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return 0;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            String role = user.getRole();
            if (role == null || role.trim().isEmpty()) {
                role = "user";
            }
            ps.setString(4, role);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    /**
     * 按用户名查询用户（登录时使用；返回对象中的 password 为库中摘要）。
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, nickname, role, create_time FROM users WHERE username = ? LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setNickname(rs.getString("nickname"));
                u.setRole(rs.getString("role"));
                if (u.getRole() == null || u.getRole().trim().isEmpty()) {
                    u.setRole("user");
                }
                Timestamp ts = rs.getTimestamp("create_time");
                if (ts != null) {
                    u.setCreateTime(new java.util.Date(ts.getTime()));
                }
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public User findById(int id) {
        String sql = "SELECT id, username, password, nickname, role, create_time FROM users WHERE id = ? LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    public int updateNickname(int userId, String nickname) {
        String sql = "UPDATE users SET nickname = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return 0;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, nickname);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    public int updatePassword(int userId, String passwordHash) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return 0;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, passwordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closeQuietly(null, ps, conn);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNickname(rs.getString("nickname"));
        u.setRole(rs.getString("role"));
        if (u.getRole() == null || u.getRole().trim().isEmpty()) {
            u.setRole("user");
        }
        Timestamp ts = rs.getTimestamp("create_time");
        if (ts != null) {
            u.setCreateTime(new java.util.Date(ts.getTime()));
        }
        return u;
    }
}
