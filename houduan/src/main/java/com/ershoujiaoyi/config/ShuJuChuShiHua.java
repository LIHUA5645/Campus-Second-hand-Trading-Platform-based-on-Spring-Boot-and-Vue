// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.config;

import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.FenLeiRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.util.Md5;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShuJuChuShiHua implements CommandLineRunner {

    private final FenLeiRepository fenLeiRepository;
    private final YongHuRepository yongHuRepository;

    public ShuJuChuShiHua(FenLeiRepository fenLeiRepository, YongHuRepository yongHuRepository) {
        this.fenLeiRepository = fenLeiRepository;
        this.yongHuRepository = yongHuRepository;
    }

    @Override
    public void run(String... args) {
        if (fenLeiRepository.count() == 0) {
            String[] mingCheng = {"教材书籍", "数码电子", "生活用品", "服饰鞋包", "运动器材", "寝室好物", "其他"};
            for (String mc : mingCheng) {
                FenLei fenLei = new FenLei();
                fenLei.setMingCheng(mc);
                fenLeiRepository.save(fenLei);
            }
        }
        if (yongHuRepository.findByYongHuMing("admin") == null) {
            YongHu admin = new YongHu();
            admin.setYongHuMing("admin");
            admin.setMiMa(Md5.jiaMi("admin123"));
            admin.setXingMing("系统管理员");
            admin.setGuanLiYuan(true);
            admin.setZhuCeShiJian(new Date());
            yongHuRepository.save(admin);
        }
    }
}
