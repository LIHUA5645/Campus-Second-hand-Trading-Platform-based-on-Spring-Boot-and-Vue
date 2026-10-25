// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.ZhiFuPeiZhi;
import com.ershoujiaoyi.repository.ZhiFuPeiZhiRepository;
import com.ershoujiaoyi.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台支付配置：管理员填写支付宝（沙箱/正式）参数，密钥 AES 加密落库、返回时打码。
 */
@RestController
@RequestMapping("/admin/zhifu")
public class AdminZhiFuKongZhiQi {

    @Autowired
    private ZhiFuPeiZhiRepository zhiFuPeiZhiRepository;
    @Autowired
    private AESUtil aesUtil;

    @GetMapping("")
    public Map<String, Object> huoQu() {
        Map<String, Object> jieGuo = new HashMap<>();
        List<ZhiFuPeiZhi> quanBu = zhiFuPeiZhiRepository.findAll();
        ZhiFuPeiZhi peiZhi = quanBu.isEmpty() ? null : quanBu.get(0);
        jieGuo.put("chengGong", true);
        if (peiZhi == null) {
            jieGuo.put("yiPeiZhi", false);
            return jieGuo;
        }
        jieGuo.put("yiPeiZhi", true);
        jieGuo.put("id", peiZhi.getId());
        jieGuo.put("mingCheng", peiZhi.getMingCheng());
        jieGuo.put("appId", peiZhi.getAppId());
        jieGuo.put("yingYongSiYao", daMa(aesUtil.jieMi(peiZhi.getYingYongSiYao())));
        jieGuo.put("zhiFuBaoGongYao", daMa(aesUtil.jieMi(peiZhi.getZhiFuBaoGongYao())));
        jieGuo.put("shouKuanZhangHao", peiZhi.getShouKuanZhangHao());
        jieGuo.put("shaXiang", peiZhi.getShaXiang() == null || peiZhi.getShaXiang());
        jieGuo.put("zhuangTai", peiZhi.getZhuangTai());
        jieGuo.put("fanHuiDiZhi", peiZhi.getFanHuiDiZhi());
        jieGuo.put("tongZhiDiZhi", peiZhi.getTongZhiDiZhi());
        jieGuo.put("beiZhu", peiZhi.getBeiZhu());
        return jieGuo;
    }

    @PostMapping("/baocun")
    public Map<String, Object> baoCun(@RequestBody Map<String, Object> qingQiu) {
        Map<String, Object> jieGuo = new HashMap<>();
        List<ZhiFuPeiZhi> quanBu = zhiFuPeiZhiRepository.findAll();
        ZhiFuPeiZhi peiZhi = quanBu.isEmpty() ? new ZhiFuPeiZhi() : quanBu.get(0);

        peiZhi.setMingCheng(quZi(qingQiu.get("mingCheng")));
        peiZhi.setAppId(quZi(qingQiu.get("appId")));
        // 密钥留空或仍是打码值时保持原值不变
        String xinSiYao = quZi(qingQiu.get("yingYongSiYao"));
        if (xinSiYao != null && !xinSiYao.isEmpty() && !xinSiYao.contains("***")) {
            peiZhi.setYingYongSiYao(aesUtil.jiaMi(xinSiYao));
        }
        String xinGongYao = quZi(qingQiu.get("zhiFuBaoGongYao"));
        if (xinGongYao != null && !xinGongYao.isEmpty() && !xinGongYao.contains("***")) {
            peiZhi.setZhiFuBaoGongYao(aesUtil.jiaMi(xinGongYao));
        }
        peiZhi.setShouKuanZhangHao(quZi(qingQiu.get("shouKuanZhangHao")));
        Object shaXiang = qingQiu.get("shaXiang");
        if (shaXiang != null) {
            peiZhi.setShaXiang(Boolean.valueOf(String.valueOf(shaXiang)));
        } else if (peiZhi.getShaXiang() == null) {
            peiZhi.setShaXiang(true);
        }
        Object zhuangTai = qingQiu.get("zhuangTai");
        if (zhuangTai != null) {
            peiZhi.setZhuangTai(Integer.valueOf(String.valueOf(zhuangTai)));
        } else if (peiZhi.getZhuangTai() == null) {
            peiZhi.setZhuangTai(1);
        }
        peiZhi.setFanHuiDiZhi(quZi(qingQiu.get("fanHuiDiZhi")));
        peiZhi.setTongZhiDiZhi(quZi(qingQiu.get("tongZhiDiZhi")));
        peiZhi.setBeiZhu(quZi(qingQiu.get("beiZhu")));
        if (peiZhi.getChuangJianShiJian() == null) {
            peiZhi.setChuangJianShiJian(new Date());
        }
        peiZhi.setGengXinShiJian(new Date());
        zhiFuPeiZhiRepository.save(peiZhi);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 启用 / 停用 */
    @PostMapping("/zhuangtai")
    public Map<String, Object> zhuangTai(@RequestParam Long id, @RequestParam Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        ZhiFuPeiZhi peiZhi = zhiFuPeiZhiRepository.findById(id).orElse(null);
        if (peiZhi == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "配置不存在");
            return jieGuo;
        }
        peiZhi.setZhuangTai(zhuangTai);
        peiZhi.setGengXinShiJian(new Date());
        zhiFuPeiZhiRepository.save(peiZhi);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    private String quZi(Object zhi) {
        return zhi == null ? null : String.valueOf(zhi);
    }

    /** 密钥打码：前 6 位 + *** + 后 4 位 */
    private String daMa(String zhi) {
        if (zhi == null || zhi.isEmpty()) return "";
        if (zhi.length() <= 10) return zhi.substring(0, Math.min(2, zhi.length())) + "***";
        return zhi.substring(0, 6) + "***" + zhi.substring(zhi.length() - 4);
    }
}
