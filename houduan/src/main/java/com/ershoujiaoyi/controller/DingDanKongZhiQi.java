// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dingdan")
public class DingDanKongZhiQi {

    @Autowired
    private DingDanRepository dingDanRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;
    @Autowired
    private YongHuRepository yongHuRepository;

    @PostMapping("/xiadan")
    public Map<String, Object> xiadan(@RequestBody DingDan dingDan) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (dingDan.getShangPinId() == null || dingDan.getMaiJiaId() == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "下单信息不完整");
            return jieGuo;
        }
        ShangPin shangPin = shangPinRepository.findById(dingDan.getShangPinId()).orElse(null);
        if (shangPin == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "商品不存在");
            return jieGuo;
        }
        if (shangPin.getZhuangTai() != 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该商品已售出或已下架");
            return jieGuo;
        }
        if (shangPin.getYongHuId() != null && shangPin.getYongHuId().equals(dingDan.getMaiJiaId())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "不能购买自己发布的商品");
            return jieGuo;
        }
        dingDan.setShangPinBiaoTi(shangPin.getBiaoTi());
        dingDan.setJiaGe(shangPin.getJiaGe());
        dingDan.setTuPian(shangPin.getTuPian());
        dingDan.setZhuangTai(0);
        dingDan.setXiaDanShiJian(new Date());
        if (dingDan.getMaiJiaMing() == null && dingDan.getMaiJiaId() != null) {
            YongHu maiJia = yongHuRepository.findById(dingDan.getMaiJiaId()).orElse(null);
            if (maiJia != null) {
                dingDan.setMaiJiaMing(maiJia.getXingMing() != null ? maiJia.getXingMing() : maiJia.getYongHuMing());
                dingDan.setMaiJiaShouJi(maiJia.getShouJi());
            }
        }
        DingDan baoCun = dingDanRepository.save(dingDan);
        shangPin.setZhuangTai(1);
        shangPinRepository.save(shangPin);
        jieGuo.put("chengGong", true);
        jieGuo.put("dingDan", baoCun);
        return jieGuo;
    }

    @GetMapping("/wode")
    public Map<String, Object> wode(@RequestParam Long yongHuId,
                                    @RequestParam(defaultValue = "maiJia") String jiaoSe) {
        Map<String, Object> jieGuo = new HashMap<>();
        List<DingDan> lieBiao;
        if ("maiJia".equals(jiaoSe)) {
            lieBiao = dingDanRepository.findByMaiJiaId(yongHuId);
        } else {
            lieBiao = new ArrayList<>();
            List<ShangPin> woDeShangPin = shangPinRepository.findByYongHuId(yongHuId);
            for (ShangPin sp : woDeShangPin) {
                lieBiao.addAll(dingDanRepository.findByShangPinId(sp.getId()));
            }
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    @PutMapping("/zhuangtai")
    public Map<String, Object> zhuangtai(@RequestParam Long id, @RequestParam Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        DingDan dingDan = dingDanRepository.findById(id).orElse(null);
        if (dingDan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        dingDan.setZhuangTai(zhuangTai);
        dingDanRepository.save(dingDan);
        if (zhuangTai == 3 && dingDan.getShangPinId() != null) {
            shangPinRepository.findById(dingDan.getShangPinId()).ifPresent(s -> {
                s.setZhuangTai(0);
                shangPinRepository.save(s);
            });
        }
        jieGuo.put("chengGong", true);
        return jieGuo;
    }
}
