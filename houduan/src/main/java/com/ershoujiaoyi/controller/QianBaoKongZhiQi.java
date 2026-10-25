// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.TiXian;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.entity.YuEJiLu;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.TiXianRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.repository.YuEJiLuRepository;
import com.ershoujiaoyi.service.QianBaoFuWu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的钱包：买家付款先进平台账户，订单确认收货后记入卖家余额，卖家可申请提现。
 */
@RestController
@RequestMapping("/qianbao")
public class QianBaoKongZhiQi {

    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;
    @Autowired
    private DingDanRepository dingDanRepository;
    @Autowired
    private YuEJiLuRepository yuEJiLuRepository;
    @Autowired
    private TiXianRepository tiXianRepository;
    @Autowired
    private QianBaoFuWu qianBaoFuWu;

    /** 钱包首页：余额、待到账、累计收入、流水与提现记录 */
    @GetMapping("")
    public Map<String, Object> qianBao(@RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        Long id = zhuanChang(yongHuId);
        if (id == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先登录");
            return jieGuo;
        }
        YongHu yongHu = yongHuRepository.findById(id).orElse(null);
        if (yongHu == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("yuE", yongHu.getYuE() == null ? 0 : yongHu.getYuE());
        jieGuo.put("leiJiShouRu", yongHu.getLeiJiShouRu() == null ? 0 : yongHu.getLeiJiShouRu());
        jieGuo.put("leiJiTiXian", yongHu.getLeiJiTiXian() == null ? 0 : yongHu.getLeiJiTiXian());
        jieGuo.put("daiDaoZhang", jiSuanDaiDaoZhang(id));
        jieGuo.put("jiLu", yuEJiLuRepository.findByYongHuIdOrderByShiJianDescIdDesc(id));
        jieGuo.put("tiXianJiLu", tiXianRepository.findByYongHuIdOrderByShenQingShiJianDescIdDesc(id));
        return jieGuo;
    }

    /** 只看流水（分页场景留的轻量接口） */
    @GetMapping("/jilu")
    public Map<String, Object> jiLu(@RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        Long id = zhuanChang(yongHuId);
        if (id == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先登录");
            return jieGuo;
        }
        List<YuEJiLu> lieBiao = yuEJiLuRepository.findByYongHuIdOrderByShiJianDescIdDesc(id);
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    /** 我的提现记录 */
    @GetMapping("/tixian")
    public Map<String, Object> woDeTiXian(@RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        Long id = zhuanChang(yongHuId);
        if (id == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先登录");
            return jieGuo;
        }
        List<TiXian> lieBiao = tiXianRepository.findByYongHuIdOrderByShenQingShiJianDescIdDesc(id);
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    /** 提交提现申请 */
    @PostMapping("/tixian")
    public Map<String, Object> shenQing(@RequestBody Map<String, Object> qingQiu,
                                        @RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        Long id = zhuanChang(yongHuId);
        if (id == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请先登录");
            return jieGuo;
        }
        YongHu yongHu = yongHuRepository.findById(id).orElse(null);
        if (yongHu == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        Double jinE = zhuanDouble(qingQiu.get("jinE"));
        String fangShi = qingQiu.get("fangShi") == null ? null : String.valueOf(qingQiu.get("fangShi"));
        String zhangHao = qingQiu.get("zhangHao") == null ? null : String.valueOf(qingQiu.get("zhangHao"));
        String xingMing = qingQiu.get("xingMing") == null ? null : String.valueOf(qingQiu.get("xingMing"));
        String shouJi = qingQiu.get("shouJi") == null ? null : String.valueOf(qingQiu.get("shouJi"));
        return qianBaoFuWu.shenQingTiXian(yongHu, jinE, fangShi, zhangHao, xingMing, shouJi);
    }

    /** 待到账：买家已付款但还没确认收货的订单金额之和 */
    private double jiSuanDaiDaoZhang(Long yongHuId) {
        List<ShangPin> woDe = shangPinRepository.findByYongHuId(yongHuId);
        double zong = 0;
        if (woDe == null) {
            return 0;
        }
        List<DingDan> dingDanLie = new ArrayList<>();
        for (ShangPin sp : woDe) {
            dingDanLie.addAll(dingDanRepository.findByShangPinId(sp.getId()));
        }
        for (DingDan d : dingDanLie) {
            boolean yiFu = d.getZhiFuZhuangTai() != null && d.getZhiFuZhuangTai() == 1;
            boolean weiWanCheng = d.getZhuangTai() != null && (d.getZhuangTai() == 0 || d.getZhuangTai() == 1);
            if (yiFu && weiWanCheng && d.getJiaGe() != null) {
                zong += d.getJiaGe();
            }
        }
        return zong;
    }

    private Long zhuanChang(Object zhi) {
        if (zhi == null) return null;
        try {
            return Long.valueOf(String.valueOf(zhi).trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Double zhuanDouble(Object zhi) {
        if (zhi == null) return null;
        try {
            return Double.valueOf(String.valueOf(zhi).trim());
        } catch (Exception e) {
            return null;
        }
    }
}
