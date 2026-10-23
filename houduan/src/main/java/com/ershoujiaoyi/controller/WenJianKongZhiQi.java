// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.YongHuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * 后台图片上传：从本地选择图片 → 存到 upload/lunbo → 返回 /uploads/lunbo/xxx 地址。
 */
@RestController
@RequestMapping("/upload")
public class WenJianKongZhiQi {

    @Autowired
    private YongHuRepository yongHuRepository;

    @Value("${ershou.upload-dir:upload}")
    private String shangChuanMuLu;

    private static final List<String> YUN_XU = Arrays.asList("jpg", "jpeg", "png", "webp", "gif", "bmp");
    private static final long ZUI_DA = 8L * 1024 * 1024;

    @PostMapping("/tuxiang")
    public Map<String, Object> shangChuanTuXiang(@RequestParam("file") MultipartFile wenJian,
                                                 @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu guanLiYuan = guanLiYuanId == null ? null : yongHuRepository.findById(guanLiYuanId).orElse(null);
        if (guanLiYuan == null || guanLiYuan.getGuanLiYuan() == null || !guanLiYuan.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以上传图片，请重新登录后台");
            return jieGuo;
        }
        if (wenJian == null || wenJian.isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请选择一张图片");
            return jieGuo;
        }
        if (wenJian.getSize() > ZUI_DA) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "图片不能超过 8MB");
            return jieGuo;
        }
        String yuanMing = wenJian.getOriginalFilename() == null ? "" : wenJian.getOriginalFilename();
        int dian = yuanMing.lastIndexOf('.');
        String kuoZhan = dian >= 0 ? yuanMing.substring(dian + 1).toLowerCase(Locale.ROOT) : "";
        if (!YUN_XU.contains(kuoZhan)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "仅支持 jpg / png / webp / gif 图片");
            return jieGuo;
        }
        File muLu = new File(shangChuanMuLu, "lunbo");
        if (!muLu.exists() && !muLu.mkdirs()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "创建上传目录失败：" + muLu.getAbsolutePath());
            return jieGuo;
        }
        String xinMing = UUID.randomUUID().toString().replace("-", "") + "." + kuoZhan;
        File muBiao = new File(muLu, xinMing);
        try {
            wenJian.transferTo(muBiao.getAbsoluteFile());
        } catch (IOException e) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "保存图片失败：" + e.getMessage());
            return jieGuo;
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("url", "/uploads/lunbo/" + xinMing);
        return jieGuo;
    }
}
