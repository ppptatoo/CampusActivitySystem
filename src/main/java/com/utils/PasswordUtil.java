package com.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具类：对明文密码做简单不可逆摘要（SHA-256 + 固定盐）。
 * <p>
 * 说明：课程项目常用「摘要 + 盐」防止彩虹表；生产环境可换 BCrypt/Argon2。
 * 盐值需与注册、登录两端一致，修改后旧用户将无法登录，除非重置密码。
 * </p>
 */
public final class PasswordUtil {

    /** 与应用绑定的固定盐（简单场景；更高安全可为每用户随机盐并存库） */
    private static final String APP_SALT = "CampusActivitySystem_Salt_2026";

    private PasswordUtil() {
    }

    /**
     * 将明文密码转为 SHA-256（UTF-8 字节）十六进制字符串，便于存入 VARCHAR(64)。
     *
     * @param plain 明文密码，不允许为 null
     * @return 64 位十六进制小写字符串；算法不可用时返回 {@code null}
     */
    public static String hash(String plain) {
        if (plain == null) {
            return null;
        }
        String mixed = APP_SALT + plain;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(mixed.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验明文是否与已存储的摘要一致（区分大小写，存储为小写十六进制）。
     */
    public static boolean matches(String plain, String storedHash) {
        if (plain == null || storedHash == null) {
            return false;
        }
        String h = hash(plain);
        return storedHash.equalsIgnoreCase(h);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
