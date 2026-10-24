// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.AiPeiZhi;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.AiPeiZhiRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.service.AiFuWu;
import com.ershoujiaoyi.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员：AI 接口配置。支持一次性添加多个 API 密钥，按顺序逐个尝试调用，
 * 某个密钥失效时自动切换下一个。密钥只在服务端保存，返回前台一律打码。
 */
@RestController
@RequestMapping("/admin/ai")
public class AdminAiKongZhiQi {

    @Autowired
    private AiPeiZhiRepository aiPeiZhiRepository;
    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private AiFuWu aiFuWu;
    @Autowired
    private AESUtil aesUtil;

    @GetMapping("/liebiao")
    public Map<String, Object> liebiao(@RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        List<Map<String, Object>> lieBiao = new ArrayList<>();
        for (AiPeiZhi p : aiPeiZhiRepository.findAllByOrderByPaiXuAscIdAsc()) {
            lieBiao.add(zhuanMap(p, false));
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    /** 新增或编辑一条配置；miYao 为空表示不修改原密钥 */
    @PostMapping("/baocun")
    public Map<String, Object> baocun(@RequestBody AiPeiZhi peiZhi,
                                      @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        String cuoWu = xiaoYan(peiZhi, false);
        if (cuoWu != null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", cuoWu);
            return jieGuo;
        }
        AiPeiZhi muBiao;
        if (peiZhi.getId() != null) {
            muBiao = aiPeiZhiRepository.findById(peiZhi.getId()).orElse(null);
            if (muBiao == null) {
                jieGuo.put("chengGong", false);
                jieGuo.put("xinXi", "该配置不存在");
                return jieGuo;
            }
        } else {
            muBiao = new AiPeiZhi();
            muBiao.setChuangJianShiJian(new Date());
        }
        muBiao.setMingCheng(peiZhi.getMingCheng() == null ? "" : peiZhi.getMingCheng().trim());
        muBiao.setFuWuShang(peiZhi.getFuWuShang() == null ? "qita" : peiZhi.getFuWuShang().trim());
        muBiao.setDiZhi(peiZhi.getDiZhi().trim());
        muBiao.setMoXing(peiZhi.getMoXing().trim());
        if (peiZhi.getMiYao() != null && !peiZhi.getMiYao().trim().isEmpty()) {
            muBiao.setMiYao(aesUtil.jiaMi(peiZhi.getMiYao().trim()));
        }
        muBiao.setShiTu(Boolean.TRUE.equals(peiZhi.getShiTu()));
        muBiao.setZhuangTai(peiZhi.getZhuangTai() == null ? 0 : peiZhi.getZhuangTai());
        muBiao.setPaiXu(peiZhi.getPaiXu() == null ? 0 : peiZhi.getPaiXu());
        muBiao.setBeiZhu(peiZhi.getBeiZhu() == null ? "" : peiZhi.getBeiZhu());
        if (muBiao.getChuangJianShiJian() == null) muBiao.setChuangJianShiJian(new Date());
        jieGuo.put("chengGong", true);
        jieGuo.put("peiZhi", zhuanMap(aiPeiZhiRepository.save(muBiao), false));
        return jieGuo;
    }

    /**
     * 批量添加：一次粘贴多行密钥。
     * 每行支持「密钥」或「名称|密钥」或「名称|密钥|模型」三种写法，# 开头为注释行。
     */
    @PostMapping("/piliang")
    public Map<String, Object> piliang(@RequestBody Map<String, Object> qingQiu,
                                       @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        String neiRong = qingQiu.get("neiRong") == null ? "" : String.valueOf(qingQiu.get("neiRong"));
        String diZhi = qingQiu.get("diZhi") == null ? "" : String.valueOf(qingQiu.get("diZhi")).trim();
        String moXing = qingQiu.get("moXing") == null ? "" : String.valueOf(qingQiu.get("moXing")).trim();
        String fuWuShang = qingQiu.get("fuWuShang") == null ? "qita" : String.valueOf(qingQiu.get("fuWuShang")).trim();
        boolean shiTu = Boolean.parseBoolean(String.valueOf(qingQiu.get("shiTu")));

        if (diZhi.isEmpty() || moXing.isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先填写接口地址和模型名称");
            return jieGuo;
        }
        String[] hang = neiRong.split("\\r?\\n");
        int tianJia = 0;
        int paiXuQiShi = 0;
        for (AiPeiZhi p : aiPeiZhiRepository.findAllByOrderByPaiXuAscIdAsc()) {
            paiXuQiShi = Math.max(paiXuQiShi, p.getPaiXu() == null ? 0 : p.getPaiXu());
        }
        int xuHao = 0;
        for (String yuan : hang) {
            String hangWen = yuan == null ? "" : yuan.trim();
            if (hangWen.isEmpty() || hangWen.startsWith("#")) continue;
            String[] buFen = hangWen.split("\\|");
            String mingCheng = "";
            String miYao = "";
            String benMoXing = moXing;
            if (buFen.length == 1) {
                miYao = buFen[0].trim();
            } else if (buFen.length == 2) {
                mingCheng = buFen[0].trim();
                miYao = buFen[1].trim();
            } else {
                mingCheng = buFen[0].trim();
                miYao = buFen[1].trim();
                if (!buFen[2].trim().isEmpty()) benMoXing = buFen[2].trim();
            }
            if (miYao.isEmpty()) continue;
            xuHao++;
            if (mingCheng.isEmpty()) mingCheng = fuWuShang + " 密钥" + (tianJia + paiXuQiShi + xuHao);

            AiPeiZhi peiZhi = new AiPeiZhi();
            peiZhi.setMingCheng(mingCheng);
            peiZhi.setFuWuShang(fuWuShang);
            peiZhi.setDiZhi(diZhi);
            peiZhi.setMoXing(benMoXing);
            peiZhi.setMiYao(aesUtil.jiaMi(miYao));
            peiZhi.setShiTu(shiTu);
            peiZhi.setZhuangTai(0);
            peiZhi.setPaiXu(paiXuQiShi + xuHao);
            peiZhi.setBeiZhu("");
            peiZhi.setChuangJianShiJian(new Date());
            aiPeiZhiRepository.save(peiZhi);
            tianJia++;
        }
        if (tianJia == 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "没有解析到有效的密钥，请每行填写一个");
            return jieGuo;
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("tianJiaShu", tianJia);
        return jieGuo;
    }

    @PostMapping("/zhuangtai")
    public Map<String, Object> qieHuanZhuangTai(@RequestParam Long id,
                                                @RequestParam Integer zhuangTai,
                                                @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        AiPeiZhi peiZhi = aiPeiZhiRepository.findById(id).orElse(null);
        if (peiZhi == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该配置不存在");
            return jieGuo;
        }
        peiZhi.setZhuangTai(zhuangTai == null || zhuangTai == 0 ? 0 : 1);
        aiPeiZhiRepository.save(peiZhi);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @PostMapping("/shanchu")
    public Map<String, Object> shanChu(@RequestParam Long id,
                                       @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        aiPeiZhiRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 连通性测试：用一条极短的消息试一下密钥能不能用 */
    @PostMapping("/ceshi")
    public Map<String, Object> ceShi(@RequestParam Long id,
                                     @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        AiPeiZhi peiZhi = aiPeiZhiRepository.findById(id).orElse(null);
        if (peiZhi == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该配置不存在");
            return jieGuo;
        }
        Map<String, Object> jie = aiFuWu.ceShi(peiZhi);
        jie.put("peiZhi", zhuanMap(aiPeiZhiRepository.findById(id).orElse(peiZhi), false));
        return jie;
    }

    /** 拉取服务商的可用模型列表（OpenAI 兼容 /models）；编辑时密钥留空则用已保存的密钥 */
    @PostMapping("/moban")
    public Map<String, Object> moXingLieBiao(@RequestParam(required = false) Long id,
                                             @RequestParam(required = false) String diZhi,
                                             @RequestParam(required = false) String miYao,
                                             @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (!shiFouGuanLiYuan(guanLiYuanId)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以配置 AI 接口，请重新登录后台");
            return jieGuo;
        }
        String dizhi = diZhi == null ? "" : diZhi.trim();
        String yao = miYao == null ? "" : miYao.trim();
        if (id != null) {
            AiPeiZhi peiZhi = aiPeiZhiRepository.findById(id).orElse(null);
            if (peiZhi != null) {
                if (dizhi.isEmpty() && peiZhi.getDiZhi() != null) dizhi = peiZhi.getDiZhi();
                if (yao.isEmpty() && peiZhi.getMiYao() != null) yao = aesUtil.jieMi(peiZhi.getMiYao());
            }
        }
        jieGuo.putAll(aiFuWu.huoQuMoXingLieBiao(dizhi, yao));
        return jieGuo;
    }

    // ==================== 内部方法 ====================

    private boolean shiFouGuanLiYuan(Long guanLiYuanId) {
        if (guanLiYuanId == null) return false;
        YongHu yh = yongHuRepository.findById(guanLiYuanId).orElse(null);
        return yh != null && Boolean.TRUE.equals(yh.getGuanLiYuan());
    }

    private String xiaoYan(AiPeiZhi peiZhi, boolean piLiang) {
        if (peiZhi.getDiZhi() == null || peiZhi.getDiZhi().trim().isEmpty()) return "请填写接口地址";
        if (peiZhi.getMoXing() == null || peiZhi.getMoXing().trim().isEmpty()) return "请填写模型名称";
        if (!piLiang && (peiZhi.getMiYao() == null || peiZhi.getMiYao().trim().isEmpty())) return "请填写 API 密钥";
        String diZhi = peiZhi.getDiZhi().trim().toLowerCase();
        if (!diZhi.startsWith("http://") && !diZhi.startsWith("https://")) return "接口地址要以 http:// 或 https:// 开头";
        return null;
    }

    /** 转成前台可用的 Map；密钥一律打码，不回传明文 */
    private Map<String, Object> zhuanMap(AiPeiZhi peiZhi, boolean xianShiMiYao) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", peiZhi.getId());
        map.put("mingCheng", peiZhi.getMingCheng());
        map.put("fuWuShang", peiZhi.getFuWuShang());
        map.put("diZhi", peiZhi.getDiZhi());
        map.put("moXing", peiZhi.getMoXing());
        map.put("shiTu", peiZhi.getShiTu());
        map.put("zhuangTai", peiZhi.getZhuangTai());
        map.put("paiXu", peiZhi.getPaiXu());
        map.put("beiZhu", peiZhi.getBeiZhu());
        map.put("zuiJinZhuangTai", peiZhi.getZuiJinZhuangTai());
        map.put("zuiJinCuoWu", peiZhi.getZuiJinCuoWu());
        map.put("chuangJianShiJian", peiZhi.getChuangJianShiJian());
        map.put("zuiJinShiYongShiJian", peiZhi.getZuiJinShiYongShiJian());
        map.put("miYao", xianShiMiYao ? peiZhi.getMiYao() : zheBi(aesUtil.jieMi(peiZhi.getMiYao())));
        return map;
    }

    private String zheBi(String miYao) {
        if (miYao == null || miYao.isEmpty()) return "";
        String yao = miYao.trim();
        if (yao.length() <= 10) return "******";
        return yao.substring(0, 6) + "****" + yao.substring(yao.length() - 4);
    }
}
