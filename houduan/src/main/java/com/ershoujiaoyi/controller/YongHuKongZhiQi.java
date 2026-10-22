// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.util.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/yonghu")
public class YongHuKongZhiQi {

    @Autowired
    private YongHuRepository yongHuRepository;

    @PostMapping("/zhuce")
    public Map<String, Object> zhuce(@RequestBody YongHu yongHu) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (yongHu.getYongHuMing() == null || yongHu.getYongHuMing().isEmpty()
                || yongHu.getMiMa() == null || yongHu.getMiMa().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户名和密码不能为空");
            return jieGuo;
        }
        if (yongHuRepository.findByYongHuMing(yongHu.getYongHuMing()) != null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该用户名已被注册");
            return jieGuo;
        }
        yongHu.setMiMa(Md5.jiaMi(yongHu.getMiMa()));
        yongHu.setZhuCeShiJian(new Date());
        YongHu baoCun = yongHuRepository.save(yongHu);
        baoCun.setMiMa(null);
        jieGuo.put("chengGong", true);
        jieGuo.put("yongHu", baoCun);
        return jieGuo;
    }

    @PostMapping("/denglu")
    public Map<String, Object> denglu(@RequestParam String yongHuMing, @RequestParam String miMa) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yongHu = yongHuRepository.findByYongHuMing(yongHuMing);
        if (yongHu == null || !Md5.jiaMi(miMa).equals(yongHu.getMiMa())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户名或密码错误");
            return jieGuo;
        }
        yongHu.setMiMa(null);
        jieGuo.put("chengGong", true);
        jieGuo.put("yongHu", yongHu);
        return jieGuo;
    }

    @GetMapping("/geren")
    public Map<String, Object> geren(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yongHu = yongHuRepository.findById(id).orElse(null);
        if (yongHu != null) {
            yongHu.setMiMa(null);
        }
        jieGuo.put("chengGong", yongHu != null);
        jieGuo.put("yongHu", yongHu);
        return jieGuo;
    }

    @PutMapping("/xiugai")
    public Map<String, Object> xiugai(@RequestBody YongHu yongHu) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yuan = yongHuRepository.findById(yongHu.getId()).orElse(null);
        if (yuan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        if (yongHu.getXingMing() != null) yuan.setXingMing(yongHu.getXingMing());
        if (yongHu.getShouJi() != null) yuan.setShouJi(yongHu.getShouJi());
        if (yongHu.getXueHao() != null) yuan.setXueHao(yongHu.getXueHao());
        if (yongHu.getTouXiang() != null) yuan.setTouXiang(yongHu.getTouXiang());
        if (yongHu.getMiMa() != null && !yongHu.getMiMa().isEmpty()) yuan.setMiMa(Md5.jiaMi(yongHu.getMiMa()));
        YongHu baoCun = yongHuRepository.save(yuan);
        baoCun.setMiMa(null);
        jieGuo.put("chengGong", true);
        jieGuo.put("yongHu", baoCun);
        return jieGuo;
    }
}
