package com.utils;

import com.entity.User;

/**
 * 角色与活动操作权限工具类。
 * <p>
 * 规则：管理员、负责人可发布活动；负责人仅可编辑/删除自己发布的活动；
 * 管理员可编辑/删除负责人发布的活动；普通用户仅浏览。
 * </p>
 */
public final class RoleUtil {

    private RoleUtil() {
    }

    public static boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }

    public static boolean isLeader(User user) {
        return user != null && "leader".equals(user.getRole());
    }

    /** 管理员或负责人 */
    public static boolean canPublish(User user) {
        return isAdmin(user) || isLeader(user);
    }

    /** 管理员可编辑任意活动；负责人仅可编辑自己发布的活动 */
    public static boolean canEdit(User user, Integer ownerId) {
        if (user == null || ownerId == null) {
            return false;
        }
        if (isAdmin(user)) {
            return true;
        }
        return isLeader(user) && user.getId().equals(ownerId);
    }

    public static boolean canEdit(User user, int ownerId) {
        return canEdit(user, Integer.valueOf(ownerId));
    }

    public static boolean canDelete(User user, Integer ownerId) {
        return canEdit(user, ownerId);
    }

    public static boolean canDelete(User user, int ownerId) {
        return canEdit(user, ownerId);
    }
}
