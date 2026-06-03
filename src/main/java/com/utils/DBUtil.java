package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类（JDBC）
 * <p>
 * 负责加载 MySQL 驱动、获取连接，以及统一关闭资源，避免连接泄漏。
 * 连接参数请根据本机 MySQL 实际账号修改（与 sql/init_users.sql 中的库名保持一致）。
 * </p>
 */
public final class DBUtil {

    /** JDBC URL：库名 campus_activity，时区与编码建议固定，避免乱码与时区偏差 */
    private static final String URL = "jdbc:mysql://localhost:3306/campus_activity"
            + "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8"
            + "&allowPublicKeyRetrieval=true";

    /** 数据库登录用户名 */
    private static final String USER = "root";

    /** 数据库登录密码（请改为本机 MySQL 密码） */
    private static final String PASSWORD = "052647";

    /** MySQL 8 驱动类名 */
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private DBUtil() {
    }

    /**
     * 获取数据库连接（每次调用新建一条连接，适合课程项目；生产环境建议使用连接池）。
     *
     * @return 成功返回 {@link Connection}，失败返回 {@code null}
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER_CLASS);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] 未找到 MySQL 驱动类，请确认 WEB-INF/lib 已加入 mysql-connector-j。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DBUtil] 获取数据库连接失败，请检查 URL、账号密码、数据库是否已创建。");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 安静关闭 {@link ResultSet}，忽略空指针与已关闭异常。
     */
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /**
     * 安静关闭 {@link Statement} 及其子类（如 {@link java.sql.PreparedStatement}）。
     */
    public static void closeQuietly(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /**
     * 安静关闭 {@link Connection}。
     */
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /**
     * 依次关闭 ResultSet、Statement、Connection（顺序与创建相反）。
     */
    public static void closeQuietly(ResultSet rs, Statement st, Connection conn) {
        closeQuietly(rs);
        closeQuietly(st);
        closeQuietly(conn);
    }
    
    public static void closeQuietly(Connection conn, Statement st, ResultSet rs) {
        closeQuietly(rs);
        closeQuietly(st);
        closeQuietly(conn);
    }
}
