// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.service;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.TiXian;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.entity.YuEJiLu;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.TiXianRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.repository.YuEJiLuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钱包：买家付款先进平台（管理员收款账号），订单确认收货后把钱记到卖家余额，
 * 卖家再申请提现，由管理员线下打款。
 */
@Service
public class QianBaoFuWu {

    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;
    @Autowired
    private YuEJiLuRepository yuEJiLuRepository;
    @Autowired
    private TiXianRepository tiXianRepository;

    /**
     * 订单完成后给卖家入账，同一订单只入账一次
     * @return null 表示无需入账（找不到卖家或已入账），否则返回入账金额
     */
    @Transactional
    public Double ruZhang(DingDan dingDan) {
        if (dingDan == null || dingDan.getShangPinId() == null) {
            return null;
        }
        List<YuEJiLu> yiYou = yuEJiLuRepository.findByDingDanIdAndLeiXing(dingDan.getId(), "shouru");
        if (yiYou != null && !yiYou.isEmpty()) {
            return null;
        }
        ShangPin shangPin = shangPinRepository.findById(dingDan.getShangPinId()).orElse(null);
        Long maiJiaId = shangPin == null ? null : shangPin.getYongHuId();
        if (maiJiaId == null) {
            return null;
        }
        YongHu maiJia = yongHuRepository.findById(maiJiaId).orElse(null);
        if (maiJia == null) {
            return null;
        }
        double jinE = dingDan.getJiaGe() == null ? 0 : dingDan.getJiaGe();
        double xinYuE = (maiJia.getYuE() == null ? 0 : maiJia.getYuE()) + jinE;
        maiJia.setYuE(xinYuE);
        maiJia.setLeiJiShouRu((maiJia.getLeiJiShouRu() == null ? 0 : maiJia.getLeiJiShouRu()) + jinE);
        yongHuRepository.save(maiJia);

        YuEJiLu jiLu = new YuEJiLu();
        jiLu.setYongHuId(maiJia.getId());
        jiLu.setYongHuMing(maiJia.getXingMing() == null ? maiJia.getYongHuMing() : maiJia.getXingMing());
        jiLu.setLeiXing("shouru");
        jiLu.setJinE(jinE);
        jiLu.setYuE(xinYuE);
        jiLu.setDingDanId(dingDan.getId());
        jiLu.setShangPinId(dingDan.getShangPinId());
        jiLu.setShuoMing("卖出《" + (dingDan.getShangPinBiaoTi() == null ? "闲置" : dingDan.getShangPinBiaoTi()) + "》收入");
        jiLu.setShiJian(new Date());
        yuEJiLuRepository.save(jiLu);
        return jinE;
    }

    /**
     * 提交提现申请：立即扣减余额（避免重复申请），驳回时原路退回
     */
    @Transactional
    public Map<String, Object> shenQingTiXian(YongHu yongHu, Double jinE, String fangShi,
                                              String zhangHao, String xingMing, String shouJi) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (jinE == null || jinE <= 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "提现金额必须大于 0");
            return jieGuo;
        }
        double yuE = yongHu.getYuE() == null ? 0 : yongHu.getYuE();
        if (jinE > yuE) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "余额不足，当前可提现 ￥" + String.format("%.2f", yuE));
            return jieGuo;
        }
        if (fangShi == null || fangShi.isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请选择收款方式");
            return jieGuo;
        }
        if (zhangHao == null || zhangHao.trim().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请填写收款账号");
            return jieGuo;
        }
        double xinYuE = yuE - jinE;
        yongHu.setYuE(xinYuE);
        yongHuRepository.save(yongHu);

        TiXian tiXian = new TiXian();
        tiXian.setYongHuId(yongHu.getId());
        tiXian.setYongHuMing(yongHu.getXingMing() == null ? yongHu.getYongHuMing() : yongHu.getXingMing());
        tiXian.setJinE(jinE);
        tiXian.setFangShi(fangShi);
        tiXian.setZhangHao(zhangHao.trim());
        tiXian.setXingMing(xingMing);
        tiXian.setShouJi(shouJi);
        tiXian.setZhuangTai(0);
        tiXian.setShenQingShiJian(new Date());
        tiXianRepository.save(tiXian);

        xieJiLu(yongHu, "tixian", -jinE, xinYuE, "申请提现，等待管理员打款");
        jieGuo.put("chengGong", true);
        jieGuo.put("xinXi", "提现申请已提交，管理员审核后会尽快打款");
        jieGuo.put("tiXian", tiXian);
        return jieGuo;
    }

    /**
     * 管理员驳回提现：金额退回余额
     */
    @Transactional
    public Map<String, Object> boHui(TiXian tiXian, String beiZhu) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (tiXian == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "提现记录不存在");
            return jieGuo;
        }
        if (tiXian.getZhuangTai() != null && tiXian.getZhuangTai() != 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该申请已处理，不能重复操作");
            return jieGuo;
        }
        YongHu yongHu = tiXian.getYongHuId() == null ? null
                : yongHuRepository.findById(tiXian.getYongHuId()).orElse(null);
        if (yongHu != null) {
            double jinE = tiXian.getJinE() == null ? 0 : tiXian.getJinE();
            double xinYuE = (yongHu.getYuE() == null ? 0 : yongHu.getYuE()) + jinE;
            yongHu.setYuE(xinYuE);
            yongHuRepository.save(yongHu);
            xieJiLu(yongHu, "bohui", jinE, xinYuE, "提现申请被驳回，金额已退回余额");
        }
        tiXian.setZhuangTai(2);
        tiXian.setBeiZhu(beiZhu);
        tiXian.setChuLiShiJian(new Date());
        tiXianRepository.save(tiXian);
        jieGuo.put("chengGong", true);
        jieGuo.put("xinXi", "已驳回，金额已退回该用户余额");
        return jieGuo;
    }

    /**
     * 管理员确认已打款
     */
    @Transactional
    public Map<String, Object> tongGuo(TiXian tiXian, String beiZhu) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (tiXian == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "提现记录不存在");
            return jieGuo;
        }
        if (tiXian.getZhuangTai() != null && tiXian.getZhuangTai() != 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该申请已处理，不能重复操作");
            return jieGuo;
        }
        tiXian.setZhuangTai(1);
        tiXian.setBeiZhu(beiZhu);
        tiXian.setChuLiShiJian(new Date());
        tiXianRepository.save(tiXian);
        if (tiXian.getYongHuId() != null) {
            yongHuRepository.findById(tiXian.getYongHuId()).ifPresent(yongHu -> {
                double jinE = tiXian.getJinE() == null ? 0 : tiXian.getJinE();
                yongHu.setLeiJiTiXian((yongHu.getLeiJiTiXian() == null ? 0 : yongHu.getLeiJiTiXian()) + jinE);
                yongHuRepository.save(yongHu);
            });
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("xinXi", "已标记为打款完成");
        return jieGuo;
    }

    private void xieJiLu(YongHu yongHu, String leiXing, double jinE, double xinYuE, String shuoMing) {
        YuEJiLu jiLu = new YuEJiLu();
        jiLu.setYongHuId(yongHu.getId());
        jiLu.setYongHuMing(yongHu.getXingMing() == null ? yongHu.getYongHuMing() : yongHu.getXingMing());
        jiLu.setLeiXing(leiXing);
        jiLu.setJinE(jinE);
        jiLu.setYuE(xinYuE);
        jiLu.setShuoMing(shuoMing);
        jiLu.setShiJian(new Date());
        yuEJiLuRepository.save(jiLu);
    }
}
