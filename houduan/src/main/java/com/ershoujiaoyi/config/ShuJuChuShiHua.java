// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.config;

import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.entity.GongGao;
import com.ershoujiaoyi.entity.LunBoTu;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.FenLeiRepository;
import com.ershoujiaoyi.repository.GongGaoRepository;
import com.ershoujiaoyi.repository.LunBoTuRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.util.Md5;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShuJuChuShiHua implements CommandLineRunner {

    private final FenLeiRepository fenLeiRepository;
    private final YongHuRepository yongHuRepository;
    private final LunBoTuRepository lunBoTuRepository;
    private final GongGaoRepository gongGaoRepository;

    public ShuJuChuShiHua(FenLeiRepository fenLeiRepository,
                          YongHuRepository yongHuRepository,
                          LunBoTuRepository lunBoTuRepository,
                          GongGaoRepository gongGaoRepository) {
        this.fenLeiRepository = fenLeiRepository;
        this.yongHuRepository = yongHuRepository;
        this.lunBoTuRepository = lunBoTuRepository;
        this.gongGaoRepository = gongGaoRepository;
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
        // 默认轮播图（图片位于 qianduan/public/images/lunbo/）
        if (lunBoTuRepository.count() == 0) {
            chuangJianLunBo("/images/lunbo/banner1.jpg", "校园闲置 · 安心流转",
                    "教材 / 数码 / 好物，低价淘到学长学姐的宝贝", 1);
            chuangJianLunBo("/images/lunbo/banner2.jpg", "毕业季清仓特惠",
                    "带走需要的，留下回忆，闲置好物统统低价转", 2);
            chuangJianLunBo("/images/lunbo/banner3.jpg", "数码好物直降",
                    "手机 / 平板 / 耳机，成色好、价格香", 3);
        }
        // 默认公告
        if (gongGaoRepository.count() == 0) {
            chuangJianGongGao("平台使用须知",
                    "发布闲置请如实描述成色与瑕疵，交易建议在校内当面验货；如遇纠纷可联系管理员协助处理。");
            chuangJianGongGao("开学季活动",
                    "开学季发布闲置免手续费，欢迎同学们把用不上的好物分享给需要的人～");
        }
    }

    private void chuangJianLunBo(String tuPian, String biaoTi, String fuBiaoTi, int paiXu) {
        LunBoTu lb = new LunBoTu();
        lb.setTuPian(tuPian);
        lb.setBiaoTi(biaoTi);
        lb.setFuBiaoTi(fuBiaoTi);
        lb.setLianJie("/");
        lb.setPaiXu(paiXu);
        lb.setZhuangTai(0);
        lb.setChuangJianShiJian(new Date());
        lunBoTuRepository.save(lb);
    }

    private void chuangJianGongGao(String biaoTi, String neiRong) {
        GongGao gg = new GongGao();
        gg.setBiaoTi(biaoTi);
        gg.setNeiRong(neiRong);
        gg.setZhuangTai(0);
        gg.setChuangJianShiJian(new Date());
        gongGaoRepository.save(gg);
    }
}
