// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {
    // 对明文密码做 MD5（32 位小写十六进制）加密后存储 / 比对
    public static String jiaMi(String yuan) {
        if (yuan == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] shuZu = md.digest(yuan.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : shuZu) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 加密失败", e);
        }
    }
}
