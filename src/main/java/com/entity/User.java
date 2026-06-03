package com.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类，与表 {@code users} 字段对应。
 * <p>
 * 用于在 Servlet、DAO、JSP（仅展示非敏感字段）之间传递数据。
 * 注意：密码字段仅在 DAO 与登录校验链路中使用，放入 Session 前应清空。
 * </p>
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    private Integer id;

    /** 登录用户名，唯一 */
    private String username;

    /**
     * 密码摘要（SHA-256 十六进制），不是明文。
     * 放入 HttpSession 前建议置为 {@code null}，避免会话劫持后泄露摘要。
     */
    private String password;

    /** 昵称，可空 */
    private String nickname;

    /** 注册时间 */
    private Date createTime;

    /** 角色：admin 管理员 / leader 负责人 / user 普通用户 */
    private String role;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /** 角色中文展示名 */
    public String getRoleLabel() {
        if ("admin".equalsIgnoreCase(role)) {
            return "管理员";
        }
        if ("leader".equalsIgnoreCase(role)) {
            return "负责人";
        }
        return "普通用户";
    }

    /**
     * 返回供 Session 使用的「安全副本」：密码字段为 null，其它字段拷贝。
     */
    public User safeCopyForSession() {
        User u = new User();
        u.setId(this.id);
        u.setUsername(this.username);
        u.setNickname(this.nickname);
        u.setCreateTime(this.createTime);
        u.setRole(this.role);
        u.setPassword(null);
        return u;
    }
}
