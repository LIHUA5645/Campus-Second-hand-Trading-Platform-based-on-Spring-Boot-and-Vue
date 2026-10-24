// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.service.AiFuWu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前台 AI 助手：用户提问（可带图片）→ AI 从在售商品里挑选推荐。
 * 密钥由管理员在后台配置，这里只负责调用。仅限登录用户使用。
 */
@RestController
@RequestMapping("/ai")
public class AiKongZhiQi {

    @Autowired
    private AiFuWu aiFuWu;
    @Autowired
    private YongHuRepository yongHuRepository;

    /** 简单的内存限流：同一用户 / 同一 IP 每分钟最多 12 次，防止刷接口烧密钥 */
    private static final Map<String, AtomicInteger> PIN_LV = new ConcurrentHashMap<>();
    private static volatile long DANG_QIAN_FEN_ZHONG = System.currentTimeMillis() / 60000;
    private static final int MEI_FEN_ZUI_DUO = 12;

    @GetMapping("/zhuangtai")
    public Map<String, Object> zhuangtai() {
        return aiFuWu.zhuangTai();
    }

    /**
     * 智能选品 / 识图推荐
     * 入参：{ "wenTi": "预算300的平板", "tuPian": "data:image/jpeg;base64,...", "shu": 6 }
     */
    @PostMapping("/wenda")
    public Map<String, Object> wenda(@RequestBody Map<String, Object> qingQiu,
                                     @RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        String wenTi = qingQiu.get("wenTi") == null ? "" : String.valueOf(qingQiu.get("wenTi"));
        String tuPian = qingQiu.get("tuPian") == null ? "" : String.valueOf(qingQiu.get("tuPian"));
        int shu = 6;
        try {
            if (qingQiu.get("shu") != null) shu = Integer.parseInt(String.valueOf(qingQiu.get("shu")));
        } catch (Exception e) {
            shu = 6;
        }
        if (wenTi.length() > 500) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "问题太长了，精简一下再问吧");
            return jieGuo;
        }
        if (tuPian.length() > 6 * 1024 * 1024) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "图片太大了，请换成 5MB 以内的图片");
            return jieGuo;
        }

        // 仅限登录用户使用：必须携带有效的 YongHuId 且该用户存在
        if (yongHuId == null || yongHuId.isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("weiDengLu", true);
            jieGuo.put("xinXi", "请先登录后再使用 AI 助手");
            return jieGuo;
        }
        Long yhId;
        try {
            yhId = Long.parseLong(yongHuId);
        } catch (Exception e) {
            jieGuo.put("chengGong", false);
            jieGuo.put("weiDengLu", true);
            jieGuo.put("xinXi", "请先登录后再使用 AI 助手");
            return jieGuo;
        }
        YongHu yh = yongHuRepository.findById(yhId).orElse(null);
        if (yh == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("weiDengLu", true);
            jieGuo.put("xinXi", "登录已失效，请重新登录");
            return jieGuo;
        }

        String yaoShi = "u" + yongHuId;
        long xianZai = System.currentTimeMillis() / 60000;
        if (xianZai != DANG_QIAN_FEN_ZHONG) {
            synchronized (AiKongZhiQi.class) {
                if (xianZai != DANG_QIAN_FEN_ZHONG) {
                    PIN_LV.clear();
                    DANG_QIAN_FEN_ZHONG = xianZai;
                }
            }
        }
        if (PIN_LV.computeIfAbsent(yaoShi, k -> new AtomicInteger(0)).incrementAndGet() > MEI_FEN_ZUI_DUO) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "问得太快啦，休息 1 分钟再来～");
            return jieGuo;
        }

        return aiFuWu.xuanShangPin(wenTi, tuPian, shu);
    }
}
