// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.util;

import com.ershoujiaoyi.entity.AiPeiZhi;
import com.ershoujiaoyi.repository.AiPeiZhiRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时的密钥迁移：把数据库里历史遗留的明文密钥（不带 ENC: 前缀）就地加密为密文。
 * 新保存的密钥已由控制器层统一加密，此组件只负责把存量数据补齐，跑过一次后即可长期不再改动。
 */
@Component
public class MiYaoQianYi implements CommandLineRunner {

    private final AiPeiZhiRepository aiPeiZhiRepository;
    private final AESUtil aesUtil;

    public MiYaoQianYi(AiPeiZhiRepository aiPeiZhiRepository, AESUtil aesUtil) {
        this.aiPeiZhiRepository = aiPeiZhiRepository;
        this.aesUtil = aesUtil;
    }

    @Override
    public void run(String... args) {
        for (AiPeiZhi p : aiPeiZhiRepository.findAll()) {
            String m = p.getMiYao();
            if (m != null && !m.isEmpty() && !m.startsWith("ENC:")) {
                p.setMiYao(aesUtil.jiaMi(m));
                aiPeiZhiRepository.save(p);
            }
        }
    }
}
