// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.repository.GongGaoRepository;
import com.ershoujiaoyi.repository.LunBoTuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点公开接口：首页轮播图与公告，无需登录。
 */
@RestController
@RequestMapping("/zhandian")
public class ZhanDianKongZhiQi {

    @Autowired
    private LunBoTuRepository lunBoTuRepository;

    @Autowired
    private GongGaoRepository gongGaoRepository;

    /** 首页轮播图（仅启用，按排序） */
    @GetMapping("/lunbo")
    public Map<String, Object> lunbo() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lunBoTuRepository.findByZhuangTaiOrderByPaiXuAscIdAsc(0));
        return jieGuo;
    }

    /** 首页公告（最新 5 条显示中的） */
    @GetMapping("/gonggao")
    public Map<String, Object> gonggao() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", gongGaoRepository.findTop5ByZhuangTaiOrderByChuangJianShiJianDesc(0));
        return jieGuo;
    }
}
