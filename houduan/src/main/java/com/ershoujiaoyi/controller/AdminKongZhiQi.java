// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.FenLeiRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.util.Md5;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminKongZhiQi {

    @Autowired
    private YongHuRepository yongHuRepository;
    @Autowired
    private ShangPinRepository shangPinRepository;
    @Autowired
    private DingDanRepository dingDanRepository;
    @Autowired
    private FenLeiRepository fenLeiRepository;

    @PostMapping("/denglu")
    public Map<String, Object> denglu(@RequestParam String yongHuMing, @RequestParam String miMa) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yh = yongHuRepository.findByYongHuMing(yongHuMing);
        if (yh == null || !Md5.jiaMi(miMa).equals(yh.getMiMa())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户名或密码错误");
            return jieGuo;
        }
        if (yh.getGuanLiYuan() == null || !yh.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "该账号不是管理员");
            return jieGuo;
        }
        yh.setMiMa(null);
        jieGuo.put("chengGong", true);
        jieGuo.put("guanLiYuan", yh);
        return jieGuo;
    }

    @GetMapping("/tongji")
    public Map<String, Object> tongji() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("yongHuShu", yongHuRepository.count());
        jieGuo.put("shangPinShu", shangPinRepository.count());
        jieGuo.put("zaiShouShu", shangPinRepository.countByZhuangTai(0));
        jieGuo.put("dingDanShu", dingDanRepository.count());
        return jieGuo;
    }

    @GetMapping("/yongHu")
    public Map<String, Object> yongHu(@RequestParam(defaultValue = "0") int yeMa,
                                     @RequestParam(defaultValue = "10") int meiYe,
                                     @RequestParam(required = false) String guanJianCi) {
        Map<String, Object> jieGuo = new HashMap<>();
        Specification<YongHu> tiaoJian = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (guanJianCi != null && !guanJianCi.isEmpty()) {
                list.add(cb.or(
                        cb.like(root.get("yongHuMing"), "%" + guanJianCi + "%"),
                        cb.like(root.get("xingMing"), "%" + guanJianCi + "%")
                ));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        List<YongHu> quanBu = yongHuRepository.findAll(tiaoJian);
        int zongShu = quanBu.size();
        int qiShi = yeMa * meiYe;
        int jieShu = Math.min(qiShi + meiYe, zongShu);
        List<YongHu> dangYe = qiShi < zongShu ? quanBu.subList(qiShi, jieShu) : new ArrayList<>();
        dangYe.forEach(y -> y.setMiMa(null));
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", dangYe);
        jieGuo.put("zongShu", zongShu);
        return jieGuo;
    }

    @PostMapping("/yongHu/shanchu")
    public Map<String, Object> shanChuYongHu(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        yongHuRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @GetMapping("/shangPin")
    public Map<String, Object> shangPin(@RequestParam(defaultValue = "0") int yeMa,
                                       @RequestParam(defaultValue = "10") int meiYe,
                                       @RequestParam(required = false) String guanJianCi) {
        Map<String, Object> jieGuo = new HashMap<>();
        Specification<ShangPin> tiaoJian = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (guanJianCi != null && !guanJianCi.isEmpty()) {
                list.add(cb.like(root.get("biaoTi"), "%" + guanJianCi + "%"));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        List<ShangPin> quanBu = shangPinRepository.findAll(tiaoJian);
        int zongShu = quanBu.size();
        int qiShi = yeMa * meiYe;
        int jieShu = Math.min(qiShi + meiYe, zongShu);
        List<ShangPin> dangYe = qiShi < zongShu ? quanBu.subList(qiShi, jieShu) : new ArrayList<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", dangYe);
        jieGuo.put("zongShu", zongShu);
        return jieGuo;
    }

    @PostMapping("/shangPin/shanchu")
    public Map<String, Object> shanChuShangPin(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        shangPinRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @PostMapping("/shangPin/xiajia")
    public Map<String, Object> xiaJiaShangPin(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        ShangPin sp = shangPinRepository.findById(id).orElse(null);
        if (sp == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "商品不存在");
            return jieGuo;
        }
        sp.setZhuangTai(2);
        shangPinRepository.save(sp);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @GetMapping("/dingDan")
    public Map<String, Object> dingDan(@RequestParam(defaultValue = "0") int yeMa,
                                      @RequestParam(defaultValue = "10") int meiYe,
                                      @RequestParam(required = false) Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        Specification<DingDan> tiaoJian = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (zhuangTai != null) {
                list.add(cb.equal(root.get("zhuangTai"), zhuangTai));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        List<DingDan> quanBu = dingDanRepository.findAll(tiaoJian);
        int zongShu = quanBu.size();
        int qiShi = yeMa * meiYe;
        int jieShu = Math.min(qiShi + meiYe, zongShu);
        List<DingDan> dangYe = qiShi < zongShu ? quanBu.subList(qiShi, jieShu) : new ArrayList<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", dangYe);
        jieGuo.put("zongShu", zongShu);
        return jieGuo;
    }

    @PutMapping("/dingDan/zhuangtai")
    public Map<String, Object> dingDanZhuangTai(@RequestParam Long id, @RequestParam Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        DingDan dd = dingDanRepository.findById(id).orElse(null);
        if (dd == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "订单不存在");
            return jieGuo;
        }
        dd.setZhuangTai(zhuangTai);
        dingDanRepository.save(dd);
        if (zhuangTai == 3 && dd.getShangPinId() != null) {
            shangPinRepository.findById(dd.getShangPinId()).ifPresent(s -> {
                s.setZhuangTai(0);
                shangPinRepository.save(s);
            });
        }
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @GetMapping("/fenlei")
    public Map<String, Object> fenlei() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", fenLeiRepository.findAll());
        return jieGuo;
    }

    @PostMapping("/fenlei/tianjia")
    public Map<String, Object> tianJiaFenLei(@RequestBody FenLei fenLei) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (fenLei.getMingCheng() == null || fenLei.getMingCheng().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "分类名称不能为空");
            return jieGuo;
        }
        jieGuo.put("chengGong", true);
        jieGuo.put("fenLei", fenLeiRepository.save(fenLei));
        return jieGuo;
    }

    @PostMapping("/fenlei/shanchu")
    public Map<String, Object> shanChuFenLei(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        fenLeiRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }
}
