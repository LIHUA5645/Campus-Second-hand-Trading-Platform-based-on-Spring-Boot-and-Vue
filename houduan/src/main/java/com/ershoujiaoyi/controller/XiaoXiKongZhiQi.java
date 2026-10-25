// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.XiaoXi;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.XiaoXiRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 站内私信：买家在商品详情页给发布者发消息，双方在消息中心互聊
 */
@RestController
@RequestMapping("/xiaoxi")
public class XiaoXiKongZhiQi {

    @Autowired
    private XiaoXiRepository xiaoXiRepository;
    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;

    /**
     * 发送一条私信
     */
    @PostMapping("/fasong")
    public Map<String, Object> fasong(@RequestBody XiaoXi xiaoXi) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (xiaoXi.getFaSongRenId() == null || xiaoXi.getJieShouRenId() == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先登录后再发消息");
            return jieGuo;
        }
        if (xiaoXi.getFaSongRenId().equals(xiaoXi.getJieShouRenId())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "不能给自己发消息");
            return jieGuo;
        }
        String neiRong = xiaoXi.getNeiRong() == null ? "" : xiaoXi.getNeiRong().trim();
        if (neiRong.isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "消息内容不能为空");
            return jieGuo;
        }
        if (neiRong.length() > 1000) {
            neiRong = neiRong.substring(0, 1000);
        }
        if (xiaoXi.getShangPinId() != null) {
            ShangPin shangPin = shangPinRepository.findById(xiaoXi.getShangPinId()).orElse(null);
            if (shangPin == null) {
                jieGuo.put("chengGong", false);
                jieGuo.put("xinXi", "商品不存在或已被删除");
                return jieGuo;
            }
            xiaoXi.setShangPinBiaoTi(shangPin.getBiaoTi());
            xiaoXi.setShangPinTuPian(shangPin.getTuPian());
        }
        YongHu faSongRen = yongHuRepository.findById(xiaoXi.getFaSongRenId()).orElse(null);
        if (faSongRen == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "发送人不存在");
            return jieGuo;
        }
        YongHu jieShouRen = yongHuRepository.findById(xiaoXi.getJieShouRenId()).orElse(null);
        if (jieShouRen == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "接收人不存在");
            return jieGuo;
        }
        xiaoXi.setFaSongRenMing(xianShiMing(faSongRen));
        xiaoXi.setFaSongRenTouXiang(faSongRen.getTouXiang());
        xiaoXi.setJieShouRenMing(xianShiMing(jieShouRen));
        xiaoXi.setNeiRong(neiRong);
        xiaoXi.setYiDu(false);
        xiaoXi.setFaSongShiJian(new Date());
        XiaoXi baoCun = xiaoXiRepository.save(xiaoXi);
        jieGuo.put("chengGong", true);
        jieGuo.put("xiaoXi", baoCun);
        return jieGuo;
    }

    /**
     * 会话列表：按「对方 + 商品」聚合，取每个会话的最后一条消息
     */
    @GetMapping("/huihua")
    public Map<String, Object> huihua(@RequestParam Long yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        List<XiaoXi> quanBu = xiaoXiRepository
                .findByFaSongRenIdOrJieShouRenIdOrderByFaSongShiJianDesc(yongHuId, yongHuId);
        Map<Long, YongHu> yongHuHuanCun = new HashMap<>();
        Map<String, Map<String, Object>> huiHuaMap = new LinkedHashMap<>();
        for (XiaoXi x : quanBu) {
            Long duiFangId = yongHuId.equals(x.getFaSongRenId()) ? x.getJieShouRenId() : x.getFaSongRenId();
            if (duiFangId == null) {
                continue;
            }
            String jian = duiFangId + "_" + (x.getShangPinId() == null ? "0" : x.getShangPinId());
            Map<String, Object> huiHua = huiHuaMap.get(jian);
            if (huiHua == null) {
                huiHua = new HashMap<>();
                huiHua.put("duiFangId", duiFangId);
                YongHu duiFang = yongHuHuanCun.get(duiFangId);
                if (duiFang == null) {
                    duiFang = yongHuRepository.findById(duiFangId).orElse(null);
                    if (duiFang != null) {
                        yongHuHuanCun.put(duiFangId, duiFang);
                    }
                }
                String duiFangMing = yongHuId.equals(x.getFaSongRenId())
                        ? x.getJieShouRenMing() : x.getFaSongRenMing();
                String duiFangTouXiang = yongHuId.equals(x.getFaSongRenId())
                        ? null : x.getFaSongRenTouXiang();
                huiHua.put("duiFangMing", duiFang != null ? xianShiMing(duiFang) : duiFangMing);
                huiHua.put("duiFangTouXiang", duiFang != null ? duiFang.getTouXiang() : duiFangTouXiang);
                huiHua.put("shangPinId", x.getShangPinId());
                huiHua.put("shangPinBiaoTi", x.getShangPinBiaoTi());
                huiHua.put("shangPinTuPian", x.getShangPinTuPian());
                huiHua.put("shangPinJiaGe", null);
                huiHua.put("shangPinZhuangTai", null);
                // 商品信息以最新数据为准，消息里存的标题图片只作兜底
                if (x.getShangPinId() != null) {
                    ShangPin shangPin = shangPinRepository.findById(x.getShangPinId()).orElse(null);
                    if (shangPin != null) {
                        huiHua.put("shangPinBiaoTi", shangPin.getBiaoTi());
                        huiHua.put("shangPinTuPian", shangPin.getTuPian());
                        huiHua.put("shangPinJiaGe", shangPin.getJiaGe());
                        huiHua.put("shangPinZhuangTai", shangPin.getZhuangTai());
                    }
                }
                huiHua.put("zuiHouNeiRong", x.getNeiRong());
                huiHua.put("zuiHouShiJian", x.getFaSongShiJian());
                huiHua.put("zuiHouShiWo", yongHuId.equals(x.getFaSongRenId()));
                huiHua.put("weiDuShu", 0);
                huiHuaMap.put(jian, huiHua);
            }
            if (Boolean.FALSE.equals(x.getYiDu()) && yongHuId.equals(x.getJieShouRenId())) {
                huiHua.put("weiDuShu", ((Integer) huiHua.get("weiDuShu")) + 1);
            }
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", huiHuaMap.values());
        return jieGuo;
    }

    /**
     * 某个会话的全部聊天记录，打开即标记为已读
     */
    @GetMapping("/liebiao")
    public Map<String, Object> liebiao(@RequestParam Long yongHuId,
                                       @RequestParam Long duiFangId,
                                       @RequestParam Long shangPinId) {
        Map<String, Object> jieGuo = new HashMap<>();
        List<XiaoXi> lieBiao = xiaoXiRepository.zhaoHuiHua(shangPinId, yongHuId, duiFangId);
        xiaoXiRepository.biaoJiYiDu(yongHuId, duiFangId, shangPinId);
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    /**
     * 我的未读消息总数，供顶栏小红点使用
     */
    @GetMapping("/weidu")
    public Map<String, Object> weidu(@RequestParam Long yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("weiDuShu", xiaoXiRepository.countByJieShouRenIdAndYiDuFalse(yongHuId));
        return jieGuo;
    }

    // 展示名优先用真实姓名，没有则用登录名
    private String xianShiMing(YongHu yongHu) {
        return yongHu.getXingMing() != null && !yongHu.getXingMing().isEmpty()
                ? yongHu.getXingMing() : yongHu.getYongHuMing();
    }
}
