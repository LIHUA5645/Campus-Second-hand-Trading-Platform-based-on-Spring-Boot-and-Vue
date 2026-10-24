// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.XiTongPeiZhi;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.XiTongPeiZhiRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置接口：目前承载 AI 助手形象（头像 / 名称 / 介绍）。
 * 整张表只保留一行（id=1）。
 */
@RestController
public class PeiZhiKongZhiQi {

    @Autowired
    private XiTongPeiZhiRepository xiTongPeiZhiRepository;
    @Autowired
    private YongHuRepository yongHuRepository;

    private static final String MO_REN_MING = "小淘";
    private static final String MO_REN_JIE_SHAO = "你的校园闲置导购小助手，说出需求或发图，帮你挑最合适的闲置～";

    // 首页装修默认文案
    private static final String MR_HUI_BIAO = "校园闲置 · 安心流转";
    private static final String MR_BIAO_TI_1 = "让校园里的";
    private static final String MR_GAO_LIANG = "闲置";
    private static final String MR_BIAO_TI_2 = "流动起来";
    private static final String MR_MIAO_SHU = "教材、数码、好物低价流转，学长学姐的宝贝等你来淘。";
    private static final String MR_TJ1 = "在售好物";
    private static final String MR_TJ2 = "商品分类";
    private static final String MR_TJ3_ZHI = "24h";
    private static final String MR_TJ3_BIAO = "极速交易";

    /** 前台获取 AI 形象配置（公开） */
    @GetMapping("/peizhi/ai")
    public Map<String, Object> aiPeiZhi() {
        Map<String, Object> jieGuo = new HashMap<>();
        XiTongPeiZhi xing = huoQuHuoChuangJian();
        jieGuo.put("chengGong", true);
        jieGuo.put("aiTouXiang", xing.getAiTouXiang() == null ? "" : xing.getAiTouXiang());
        jieGuo.put("aiMingCheng", xing.getAiMingCheng() == null ? MO_REN_MING : xing.getAiMingCheng());
        jieGuo.put("aiJieShao", xing.getAiJieShao() == null ? MO_REN_JIE_SHAO : xing.getAiJieShao());
        return jieGuo;
    }

    /** 管理员保存 AI 形象（头像地址由前端先上传图片后带回） */
    @PostMapping("/admin/peizhi/ai")
    public Map<String, Object> baoCunAiPeiZhi(@RequestBody Map<String, Object> qingQiu,
                                              @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu guanLiYuan = guanLiYuanId == null ? null : yongHuRepository.findById(guanLiYuanId).orElse(null);
        if (guanLiYuan == null || guanLiYuan.getGuanLiYuan() == null || !guanLiYuan.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以修改 AI 形象，请重新登录后台");
            return jieGuo;
        }
        XiTongPeiZhi xing = huoQuHuoChuangJian();
        if (qingQiu.get("aiTouXiang") != null) {
            String tu = String.valueOf(qingQiu.get("aiTouXiang")).trim();
            xing.setAiTouXiang(tu.isEmpty() ? null : tu);
        }
        if (qingQiu.get("aiMingCheng") != null) {
            String ming = String.valueOf(qingQiu.get("aiMingCheng")).trim();
            xing.setAiMingCheng(ming.isEmpty() ? MO_REN_MING : ming);
        }
        if (qingQiu.get("aiJieShao") != null) {
            String jie = String.valueOf(qingQiu.get("aiJieShao")).trim();
            xing.setAiJieShao(jie.isEmpty() ? MO_REN_JIE_SHAO : jie);
        }
        xiTongPeiZhiRepository.save(xing);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 前台获取首页装修文案（公开） */
    @GetMapping("/peizhi/shouye")
    public Map<String, Object> shouYeZhuangXiu() {
        Map<String, Object> jieGuo = new HashMap<>();
        XiTongPeiZhi xing = huoQuHuoChuangJian();
        jieGuo.put("chengGong", true);
        jieGuo.put("huiBiao", kongZeRen(xing.getZxHuiBiao(), MR_HUI_BIAO));
        jieGuo.put("biaoTi1", kongZeRen(xing.getZxBiaoTi1(), MR_BIAO_TI_1));
        jieGuo.put("gaoLiang", kongZeRen(xing.getZxGaoLiang(), MR_GAO_LIANG));
        jieGuo.put("biaoTi2", kongZeRen(xing.getZxBiaoTi2(), MR_BIAO_TI_2));
        jieGuo.put("miaoShu", kongZeRen(xing.getZxMiaoShu(), MR_MIAO_SHU));
        jieGuo.put("tongJi1BiaoQian", kongZeRen(xing.getZxTongJi1BiaoQian(), MR_TJ1));
        jieGuo.put("tongJi2BiaoQian", kongZeRen(xing.getZxTongJi2BiaoQian(), MR_TJ2));
        jieGuo.put("tongJi3Zhi", kongZeRen(xing.getZxTongJi3Zhi(), MR_TJ3_ZHI));
        jieGuo.put("tongJi3BiaoQian", kongZeRen(xing.getZxTongJi3BiaoQian(), MR_TJ3_BIAO));
        return jieGuo;
    }

    /** 管理员保存首页装修文案 */
    @PostMapping("/admin/peizhi/shouye")
    public Map<String, Object> baoCunShouYeZhuangXiu(@RequestBody Map<String, Object> qingQiu,
                                                     @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu guanLiYuan = guanLiYuanId == null ? null : yongHuRepository.findById(guanLiYuanId).orElse(null);
        if (guanLiYuan == null || guanLiYuan.getGuanLiYuan() == null || !guanLiYuan.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以修改首页装修，请重新登录后台");
            return jieGuo;
        }
        XiTongPeiZhi xing = huoQuHuoChuangJian();
        xing.setZxHuiBiao(quKong(qingQiu.get("huiBiao"), MR_HUI_BIAO));
        xing.setZxBiaoTi1(quKong(qingQiu.get("biaoTi1"), MR_BIAO_TI_1));
        xing.setZxGaoLiang(quKong(qingQiu.get("gaoLiang"), MR_GAO_LIANG));
        xing.setZxBiaoTi2(quKong(qingQiu.get("biaoTi2"), MR_BIAO_TI_2));
        xing.setZxMiaoShu(quKong(qingQiu.get("miaoShu"), MR_MIAO_SHU));
        xing.setZxTongJi1BiaoQian(quKong(qingQiu.get("tongJi1BiaoQian"), MR_TJ1));
        xing.setZxTongJi2BiaoQian(quKong(qingQiu.get("tongJi2BiaoQian"), MR_TJ2));
        xing.setZxTongJi3Zhi(quKong(qingQiu.get("tongJi3Zhi"), MR_TJ3_ZHI));
        xing.setZxTongJi3BiaoQian(quKong(qingQiu.get("tongJi3BiaoQian"), MR_TJ3_BIAO));
        xiTongPeiZhiRepository.save(xing);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    private String kongZeRen(String zhi, String moRen) {
        return (zhi == null || zhi.trim().isEmpty()) ? moRen : zhi;
    }

    private String quKong(Object zhi, String moRen) {
        if (zhi == null) return moRen;
        String s = String.valueOf(zhi).trim();
        return s.isEmpty() ? moRen : s;
    }

    private XiTongPeiZhi huoQuHuoChuangJian() {
        // 整张表只保留一行配置，取第一条；没有就新建（不自设主键，避免与 IDENTITY 冲突）
        java.util.List<XiTongPeiZhi> all = xiTongPeiZhiRepository.findAll();
        if (!all.isEmpty()) {
            return all.get(0);
        }
        XiTongPeiZhi xing = new XiTongPeiZhi();
        xing.setAiMingCheng(MO_REN_MING);
        xing.setAiJieShao(MO_REN_JIE_SHAO);
        return xiTongPeiZhiRepository.save(xing);
    }
}
