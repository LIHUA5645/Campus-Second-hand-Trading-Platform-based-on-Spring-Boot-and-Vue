// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.TiXian;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.TiXianRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.service.QianBaoFuWu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台提现管理：审核卖家的提现申请，线下打款后标记为已打款。
 * 受 GuanLiYuanLanJieQi 保护，必须携带管理员请求头。
 */
@RestController
@RequestMapping("/admin/tixian")
public class AdminTiXianKongZhiQi {

    @Autowired
    private TiXianRepository tiXianRepository;
    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private QianBaoFuWu qianBaoFuWu;

    /** 提现列表，zhuangTai 为空表示全部 */
    @GetMapping("")
    public Map<String, Object> lieBiao(@RequestParam(required = false) Integer zhuangTai,
                                       @RequestParam(required = false) String guanJianCi) {
        Map<String, Object> jieGuo = new HashMap<>();
        List<TiXian> lieBiao;
        if (zhuangTai != null) {
            lieBiao = tiXianRepository.findByZhuangTaiOrderByShenQingShiJianDescIdDesc(zhuangTai);
        } else {
            lieBiao = tiXianRepository.findAllByOrderByShenQingShiJianDescIdDesc();
        }
        if (guanJianCi != null && !guanJianCi.trim().isEmpty()) {
            String kw = guanJianCi.trim();
            lieBiao = lieBiao.stream()
                    .filter(t -> (t.getYongHuMing() != null && t.getYongHuMing().contains(kw))
                            || (t.getZhangHao() != null && t.getZhangHao().contains(kw))
                            || (t.getXingMing() != null && t.getXingMing().contains(kw)))
                    .toList();
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lieBiao);
        jieGuo.put("daiShenHe", tiXianRepository.countByZhuangTai(0));
        return jieGuo;
    }

    /** 处理提现：jieGuo = tongguo 已打款 / bohui 驳回 */
    @PostMapping("/chuli")
    public Map<String, Object> chuLi(@RequestParam Long id,
                                     @RequestParam String jieGuo,
                                     @RequestParam(required = false) String beiZhu,
                                     @RequestHeader(value = "GuanLiYuanId", required = false) String guanLiYuanId) {
        Map<String, Object> fanHui = new HashMap<>();
        TiXian tiXian = tiXianRepository.findById(id).orElse(null);
        if (tiXian == null) {
            fanHui.put("chengGong", false);
            fanHui.put("xinXi", "提现记录不存在");
            return fanHui;
        }
        String chuLiRen = "管理员";
        if (guanLiYuanId != null) {
            try {
                YongHu guanLiYuan = yongHuRepository.findById(Long.valueOf(guanLiYuanId.trim())).orElse(null);
                if (guanLiYuan != null) {
                    chuLiRen = guanLiYuan.getXingMing() == null
                            ? guanLiYuan.getYongHuMing() : guanLiYuan.getXingMing();
                }
            } catch (Exception ignored) {
            }
        }
        tiXian.setChuLiRen(chuLiRen);
        tiXian.setChuLiShiJian(new Date());
        if ("bohui".equals(jieGuo)) {
            return qianBaoFuWu.boHui(tiXian, beiZhu);
        }
        return qianBaoFuWu.tongGuo(tiXian, beiZhu);
    }

    /** 删除提现记录（只用于清理误提交的申请，驳回类记录建议保留） */
    @PostMapping("/shanchu")
    public Map<String, Object> shanChu(@RequestParam Long id) {
        Map<String, Object> fanHui = new HashMap<>();
        TiXian tiXian = tiXianRepository.findById(id).orElse(null);
        if (tiXian == null) {
            fanHui.put("chengGong", false);
            fanHui.put("xinXi", "提现记录不存在");
            return fanHui;
        }
        if (tiXian.getZhuangTai() != null && tiXian.getZhuangTai() == 0) {
            fanHui.put("chengGong", false);
            fanHui.put("xinXi", "待审核的申请请先驳回再删除，避免用户余额对不上");
            return fanHui;
        }
        tiXianRepository.deleteById(id);
        fanHui.put("chengGong", true);
        return fanHui;
    }
}
