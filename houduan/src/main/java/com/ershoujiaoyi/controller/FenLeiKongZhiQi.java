// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.repository.FenLeiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fenlei")
public class FenLeiKongZhiQi {

    @Autowired
    private FenLeiRepository fenLeiRepository;

    @GetMapping("/liebiao")
    public Map<String, Object> liebiao() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", fenLeiRepository.findAll());
        return jieGuo;
    }

    @PostMapping("/tianjia")
    public Map<String, Object> tianjia(@RequestBody FenLei fenLei) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (fenLei.getMingCheng() == null || fenLei.getMingCheng().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "分类名称不能为空");
            return jieGuo;
        }
        FenLei baoCun = fenLeiRepository.save(fenLei);
        jieGuo.put("chengGong", true);
        jieGuo.put("fenLei", baoCun);
        return jieGuo;
    }
}
