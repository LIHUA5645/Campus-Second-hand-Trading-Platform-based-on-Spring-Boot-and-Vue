// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.entity.ZhiFuPeiZhi;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.repository.ZhiFuPeiZhiRepository;
import com.ershoujiaoyi.util.AESUtil;
import com.ershoujiaoyi.util.ZhiFuGongJu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付：支付宝（沙箱/正式）PC 网页支付 + 线下货到付款。
 * 流程：下单 → 收银台选择支付方式 → 支付宝页面付款 → 同步返回后主动查询确认 → 订单进入「待发货」。
 */
@RestController
@RequestMapping("/zhifu")
public class ZhiFuKongZhiQi {

    /** 支付宝商户订单号前缀，避免与订单号冲突 */
    private static final String DING_DAN_HAO_QIAN_ZHUI = "SP";

    @Autowired
    private DingDanRepository dingDanRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;
    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private ZhiFuPeiZhiRepository zhiFuPeiZhiRepository;
    @Autowired
    private ZhiFuGongJu zhiFuGongJu;
    @Autowired
    private AESUtil aesUtil;

    /**
     * 发起支付宝支付：返回一段会自动跳转到支付宝收银台的 HTML 表单
     */
    @PostMapping("/xiadan")
    public Map<String, Object> xiadan(@RequestBody Map<String, Object> qingQiu,
                                      @RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        Long dingDanId = zhuanChang(qingQiu.get("dingDanId"));
        String fanHuiDiZhi = qingQiu.get("fanHuiDiZhi") == null ? null : String.valueOf(qingQiu.get("fanHuiDiZhi"));
        DingDan dingDan = dingDanId == null ? null : dingDanRepository.findById(dingDanId).orElse(null);
        if (dingDan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        if (dingDan.getZhuangTai() != null && dingDan.getZhuangTai() == 3) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单已取消，无法支付");
            return jieGuo;
        }
        if (dingDan.getZhiFuZhuangTai() != null && dingDan.getZhiFuZhuangTai() == 1) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该订单已支付");
            return jieGuo;
        }
        Long dangQian = zhuanChang((Object) yongHuId);
        if (dangQian == null || dingDan.getMaiJiaId() == null || !dangQian.equals(dingDan.getMaiJiaId())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有买家本人可以支付该订单");
            return jieGuo;
        }
        ZhiFuPeiZhi peiZhi = huoQuPeiZhi();
        String wenTi = zhiFuGongJu.jianChaPeiZhi(peiZhi);
        if (wenTi != null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", wenTi + "，请联系管理员在后台「支付配置」中填写");
            return jieGuo;
        }
        String fanHui = (fanHuiDiZhi != null && !fanHuiDiZhi.isEmpty())
                ? fanHuiDiZhi : peiZhi.getFanHuiDiZhi();
        String biaoTi = dingDan.getShangPinBiaoTi() == null ? "校园二手商品" : dingDan.getShangPinBiaoTi();
        if (biaoTi.length() > 60) biaoTi = biaoTi.substring(0, 60);
        double jinE = dingDan.getJiaGe() == null ? 0 : dingDan.getJiaGe();
        try {
            String html = zhiFuGongJu.shengChengZhiFuBiaoDan(
                    peiZhi, DING_DAN_HAO_QIAN_ZHUI + dingDan.getId(), biaoTi, jinE,
                    fanHui, peiZhi.getTongZhiDiZhi());
            dingDan.setZhiFuFangShi("alipay");
            dingDanRepository.save(dingDan);
            jieGuo.put("chengGong", true);
            jieGuo.put("html", html);
            jieGuo.put("dingDanHao", DING_DAN_HAO_QIAN_ZHUI + dingDan.getId());
            return jieGuo;
        } catch (Exception e) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "生成支付表单失败：" + e.getMessage()
                    + "（请检查后台配置的 APPID、应用私钥与支付宝公钥是否正确）");
            return jieGuo;
        }
    }

    /**
     * 主动查询支付结果：付款后同步返回页面调用，确认真实到账再推进订单
     */
    @GetMapping("/chaxun")
    public Map<String, Object> chaXun(@RequestParam Long dingDanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        DingDan dingDan = dingDanRepository.findById(dingDanId).orElse(null);
        if (dingDan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        if (dingDan.getZhiFuZhuangTai() != null && dingDan.getZhiFuZhuangTai() == 1) {
            jieGuo.put("chengGong", true);
            jieGuo.put("yiZhiFu", true);
            jieGuo.put("xinXi", "订单已支付");
            jieGuo.put("dingDan", dingDan);
            return jieGuo;
        }
        // 货到付款是线下交易，没有支付宝流水可查，直接按订单自身的付款状态返回
        if ("huodao".equals(dingDan.getZhiFuFangShi())) {
            jieGuo.put("chengGong", true);
            jieGuo.put("yiZhiFu", Integer.valueOf(1).equals(dingDan.getZhiFuZhuangTai()));
            jieGuo.put("xinXi", "货到付款订单，等待卖家确认收款");
            jieGuo.put("dingDan", dingDan);
            return jieGuo;
        }
        ZhiFuPeiZhi peiZhi = huoQuPeiZhi();
        String wenTi = zhiFuGongJu.jianChaPeiZhi(peiZhi);
        if (wenTi != null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", wenTi);
            return jieGuo;
        }
        try {
            Map<String, Object> jieGuoChaXun = zhiFuGongJu.chaXun(peiZhi, DING_DAN_HAO_QIAN_ZHUI + dingDan.getId());
            if (Boolean.TRUE.equals(jieGuoChaXun.get("yiZhiFu"))) {
                dingDan.setZhiFuZhuangTai(1);
                dingDan.setZhiFuFangShi("alipay");
                dingDan.setJiaoYiHao(jieGuoChaXun.get("jiaoYiHao") == null ? null
                        : String.valueOf(jieGuoChaXun.get("jiaoYiHao")));
                dingDan.setZhiFuShiJian(new Date());
                dingDanRepository.save(dingDan);
            }
            jieGuo.put("chengGong", true);
            jieGuo.put("yiZhiFu", jieGuoChaXun.get("yiZhiFu"));
            jieGuo.put("xinXi", jieGuoChaXun.get("xinXi"));
            jieGuo.put("dingDan", dingDan);
            return jieGuo;
        } catch (Exception e) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "查询支付结果失败：" + e.getMessage());
            return jieGuo;
        }
    }

    /**
     * 支付宝异步通知：部署到公网后由支付宝回调，验签通过后更新订单
     */
    @PostMapping("/huidiao")
    public String huidiao(@RequestParam Map<String, String> canShu) {
        ZhiFuPeiZhi peiZhi = huoQuPeiZhi();
        if (peiZhi == null || peiZhi.getZhiFuBaoGongYao() == null) {
            return "fail";
        }
        if (!zhiFuGongJu.yanQian(new HashMap<>(canShu), peiZhi.getZhiFuBaoGongYao())) {
            return "fail";
        }
        String zhuangTai = canShu.get("trade_status");
        if (!"TRADE_SUCCESS".equals(zhuangTai) && !"TRADE_FINISHED".equals(zhuangTai)) {
            return "success";
        }
        String waiBuHao = canShu.get("out_trade_no");
        if (waiBuHao == null || !waiBuHao.startsWith(DING_DAN_HAO_QIAN_ZHUI)) {
            return "success";
        }
        Long dingDanId = zhuanChang((Object) waiBuHao.substring(DING_DAN_HAO_QIAN_ZHUI.length()));
        if (dingDanId == null) {
            return "success";
        }
        dingDanRepository.findById(dingDanId).ifPresent(dingDan -> {
            dingDan.setZhiFuZhuangTai(1);
            dingDan.setZhiFuFangShi("alipay");
            dingDan.setJiaoYiHao(canShu.get("trade_no"));
            dingDan.setZhiFuShiJian(new Date());
            dingDanRepository.save(dingDan);
        });
        return "success";
    }

    /**
     * 前台判断支付是否可用（不返回任何密钥等敏感信息）
     */
    @GetMapping("/peizhi")
    public Map<String, Object> peiZhi() {
        Map<String, Object> jieGuo = new HashMap<>();
        ZhiFuPeiZhi peiZhi = huoQuPeiZhi();
        String wenTi = zhiFuGongJu.jianChaPeiZhi(peiZhi);
        jieGuo.put("chengGong", true);
        jieGuo.put("yiPeiZhi", wenTi == null);
        jieGuo.put("yuanYin", wenTi);
        jieGuo.put("shaXiang", peiZhi != null && (peiZhi.getShaXiang() == null || peiZhi.getShaXiang()));
        jieGuo.put("mingCheng", peiZhi == null ? null : peiZhi.getMingCheng());
        return jieGuo;
    }

    /** 线下货到付款：只有买家本人可以选择，标记为当面交易，收货后由卖家确认收款 */
    @PostMapping("/huodao")
    public Map<String, Object> huodao(@RequestParam Long dingDanId,
                                      @RequestHeader(value = "YongHuId", required = false) String yongHuId) {
        Map<String, Object> jieGuo = new HashMap<>();
        DingDan dingDan = dingDanRepository.findById(dingDanId).orElse(null);
        if (dingDan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        Long dangQian = zhuanChang((Object) yongHuId);
        if (dangQian == null || dingDan.getMaiJiaId() == null || !dangQian.equals(dingDan.getMaiJiaId())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有买家本人可以选择货到付款");
            return jieGuo;
        }
        dingDan.setZhiFuFangShi("huodao");
        dingDan.setZhiFuZhuangTai(0);
        dingDanRepository.save(dingDan);
        jieGuo.put("chengGong", true);
        jieGuo.put("dingDan", dingDan);
        return jieGuo;
    }

    /** 卖家确认已收到线下付款：仅商品发布者本人或管理员可操作 */
    @PostMapping("/shoukuan")
    public Map<String, Object> shoukuan(@RequestParam Long dingDanId,
                                        @RequestHeader(value = "YongHuId", required = false) String yongHuId,
                                        @RequestHeader(value = "GuanLiYuanId", required = false) String guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        DingDan dingDan = dingDanRepository.findById(dingDanId).orElse(null);
        if (dingDan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        Long dangQian = zhuanChang((Object) yongHuId);
        if (dangQian == null) {
            dangQian = zhuanChang((Object) guanLiYuanId);
        }
        boolean shiGuanLiYuan = false;
        if (dangQian != null) {
            YongHu yongHu = yongHuRepository.findById(dangQian).orElse(null);
            shiGuanLiYuan = yongHu != null && Boolean.TRUE.equals(yongHu.getGuanLiYuan());
        }
        ShangPin shangPin = dingDan.getShangPinId() == null ? null
                : shangPinRepository.findById(dingDan.getShangPinId()).orElse(null);
        boolean shiMaiJia = shangPin != null && shangPin.getYongHuId() != null && dangQian != null
                && shangPin.getYongHuId().equals(dangQian);
        if (!shiMaiJia && !shiGuanLiYuan) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有卖家本人可以确认收款");
            return jieGuo;
        }
        dingDan.setZhiFuZhuangTai(1);
        dingDan.setZhiFuShiJian(new Date());
        if (dingDan.getZhiFuFangShi() == null) {
            dingDan.setZhiFuFangShi("huodao");
        }
        dingDanRepository.save(dingDan);
        jieGuo.put("chengGong", true);
        jieGuo.put("dingDan", dingDan);
        return jieGuo;
    }

    /** 取当前启用的支付配置，并把加密字段解密成明文副本 */
    private ZhiFuPeiZhi huoQuPeiZhi() {
        List<ZhiFuPeiZhi> lieBiao = zhiFuPeiZhiRepository.findByZhuangTaiOrderByIdAsc(1);
        if (lieBiao == null || lieBiao.isEmpty()) {
            return null;
        }
        ZhiFuPeiZhi yuan = lieBiao.get(0);
        ZhiFuPeiZhi fuBen = new ZhiFuPeiZhi();
        fuBen.setId(yuan.getId());
        fuBen.setMingCheng(yuan.getMingCheng());
        fuBen.setAppId(yuan.getAppId());
        fuBen.setYingYongSiYao(aesUtil.jieMi(yuan.getYingYongSiYao()));
        fuBen.setZhiFuBaoGongYao(aesUtil.jieMi(yuan.getZhiFuBaoGongYao()));
        fuBen.setShouKuanZhangHao(yuan.getShouKuanZhangHao());
        fuBen.setShaXiang(yuan.getShaXiang());
        fuBen.setZhuangTai(yuan.getZhuangTai());
        fuBen.setFanHuiDiZhi(yuan.getFanHuiDiZhi());
        fuBen.setTongZhiDiZhi(yuan.getTongZhiDiZhi());
        return fuBen;
    }

    private Long zhuanChang(Object zhi) {
        if (zhi == null) return null;
        try {
            return Long.valueOf(String.valueOf(zhi).trim());
        } catch (Exception e) {
            return null;
        }
    }
}
