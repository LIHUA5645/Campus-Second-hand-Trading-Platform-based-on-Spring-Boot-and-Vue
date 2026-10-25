// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * API 密钥可逆加密工具：落库存密文（ENC: 前缀），调用第三方接口时再解密。
 * 密钥明文不入库；数据库即便被导出，拿到的也只是 ENC: 开头的密文。
 */
@Component
public class AESUtil {

    /** 密文前缀，便于区分「已加密」与历史遗留的明文，解密时遇到非此前缀直接原样返回 */
    private static final String QIAN_ZHUI = "ENC:";

    /** 固定 IV（AES/CBC 需要，项目内统一常量，非敏感） */
    private static final byte[] IV = "ErShouJiaYi2026!".getBytes(StandardCharsets.UTF_8);

    private final SecretKeySpec keySpec;

    public AESUtil(@Value("${ai.miYaoKey}") String miYaoKey) {
        byte[] yuan = miYaoKey.getBytes(StandardCharsets.UTF_8);
        byte[] zb = new byte[16];
        System.arraycopy(yuan, 0, zb, 0, Math.min(yuan.length, 16));
        this.keySpec = new SecretKeySpec(zb, "AES");
    }

    /** 加密；空值原样返回。结果形如 ENC:<base64> */
    public String jiaMi(String ming) {
        if (ming == null || ming.isEmpty()) return ming;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(IV));
            byte[] mi = c.doFinal(ming.getBytes(StandardCharsets.UTF_8));
            return QIAN_ZHUI + Base64.getEncoder().encodeToString(mi);
        } catch (Exception e) {
            throw new RuntimeException("API 密钥加密失败", e);
        }
    }

    /** 解密；空值返回 null；非 ENC: 开头的旧明文直接原样返回（兼容历史数据） */
    public String jieMi(String mi) {
        if (mi == null) return null;
        if (!mi.startsWith(QIAN_ZHUI)) return mi;
        try {
            String ben64 = mi.substring(QIAN_ZHUI.length());
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(IV));
            byte[] ming = c.doFinal(Base64.getDecoder().decode(ben64));
            return new String(ming, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
