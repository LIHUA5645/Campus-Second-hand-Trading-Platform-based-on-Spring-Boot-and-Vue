// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.controller;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.entity.GongGao;
import com.ershoujiaoyi.entity.LunBoTu;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.FenLeiRepository;
import com.ershoujiaoyi.repository.GongGaoRepository;
import com.ershoujiaoyi.repository.LunBoTuRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.ershoujiaoyi.util.Md5;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    private LunBoTuRepository lunBoTuRepository;
    @Autowired
    private GongGaoRepository gongGaoRepository;

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

    /** 数据概览：总数 + 今日新增 + 分类分布 + 订单状态分布 + 成交额 */
    @GetMapping("/tongji")
    public Map<String, Object> tongji() {
        Map<String, Object> jieGuo = new HashMap<>();
        List<YongHu> quanBuYongHu = yongHuRepository.findAll();
        List<ShangPin> quanBuShangPin = shangPinRepository.findAll();
        List<DingDan> quanBuDingDan = dingDanRepository.findAll();
        List<FenLei> quanBuFenLei = fenLeiRepository.findAll();

        jieGuo.put("chengGong", true);
        jieGuo.put("yongHuShu", quanBuYongHu.size());
        jieGuo.put("shangPinShu", quanBuShangPin.size());
        jieGuo.put("zaiShouShu", quanBuShangPin.stream()
                .filter(s -> Integer.valueOf(0).equals(s.getZhuangTai())).count());
        jieGuo.put("dingDanShu", quanBuDingDan.size());

        // 分类商品分布
        List<Map<String, Object>> fenLeiFenBu = new ArrayList<>();
        for (FenLei f : quanBuFenLei) {
            long shu = quanBuShangPin.stream()
                    .filter(s -> f.getId() != null && f.getId().equals(s.getFenLeiId()))
                    .count();
            Map<String, Object> xiang = new HashMap<>();
            xiang.put("mingCheng", f.getMingCheng());
            xiang.put("shu", shu);
            fenLeiFenBu.add(xiang);
        }
        jieGuo.put("fenLeiFenBu", fenLeiFenBu);

        // 订单状态分布
        Map<String, Object> dingDanFenBu = new HashMap<>();
        dingDanFenBu.put("daiFaHuo", quanBuDingDan.stream().filter(d -> Integer.valueOf(0).equals(d.getZhuangTai())).count());
        dingDanFenBu.put("daiShouHuo", quanBuDingDan.stream().filter(d -> Integer.valueOf(1).equals(d.getZhuangTai())).count());
        dingDanFenBu.put("yiWanCheng", quanBuDingDan.stream().filter(d -> Integer.valueOf(2).equals(d.getZhuangTai())).count());
        dingDanFenBu.put("yiQuXiao", quanBuDingDan.stream().filter(d -> Integer.valueOf(3).equals(d.getZhuangTai())).count());
        jieGuo.put("dingDanFenBu", dingDanFenBu);

        // 今日新增
        Calendar lingDian = Calendar.getInstance();
        lingDian.set(Calendar.HOUR_OF_DAY, 0);
        lingDian.set(Calendar.MINUTE, 0);
        lingDian.set(Calendar.SECOND, 0);
        lingDian.set(Calendar.MILLISECOND, 0);
        Date jinTian = lingDian.getTime();
        jieGuo.put("jinRiYongHu", quanBuYongHu.stream()
                .filter(y -> y.getZhuCeShiJian() != null && !y.getZhuCeShiJian().before(jinTian)).count());
        jieGuo.put("jinRiShangPin", quanBuShangPin.stream()
                .filter(s -> s.getFabuShiJian() != null && !s.getFabuShiJian().before(jinTian)).count());

        // 已完成订单成交额
        double chengJiaoE = quanBuDingDan.stream()
                .filter(d -> Integer.valueOf(2).equals(d.getZhuangTai()))
                .mapToDouble(d -> d.getJiaGe() == null ? 0D : d.getJiaGe())
                .sum();
        jieGuo.put("chengJiaoE", chengJiaoE);

        return jieGuo;
    }

    // ==================== 用户管理 ====================

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
        YongHu yh = yongHuRepository.findById(id).orElse(null);
        if (yh != null && "admin".equals(yh.getYongHuMing())) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "内置管理员账号不可删除");
            return jieGuo;
        }
        yongHuRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 设置 / 取消管理员 */
    @PostMapping("/yongHu/guanliyuan")
    public Map<String, Object> sheZhiGuanLiYuan(@RequestParam Long id, @RequestParam Boolean shi) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yh = yongHuRepository.findById(id).orElse(null);
        if (yh == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        if ("admin".equals(yh.getYongHuMing()) && Boolean.FALSE.equals(shi)) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "内置管理员账号不可降级");
            return jieGuo;
        }
        yh.setGuanLiYuan(shi);
        yongHuRepository.save(yh);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 管理员重置（清除）某用户的自定义头像 */
    @PostMapping("/yongHu/chongzhiTouXiang")
    public Map<String, Object> chongZhiTouXiang(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yh = yongHuRepository.findById(id).orElse(null);
        if (yh == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        yh.setTouXiang(null);
        yongHuRepository.save(yh);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 管理员编辑用户资料（昵称/手机/学号/可选重置密码） */
    @PostMapping("/yongHu/bianji")
    public Map<String, Object> bianJiYongHu(@RequestParam Long id,
                                            @RequestParam(required = false) String xingMing,
                                            @RequestParam(required = false) String shouJi,
                                            @RequestParam(required = false) String xueHao,
                                            @RequestParam(required = false) String miMa,
                                            @RequestParam(required = false) String touXiang) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu yh = yongHuRepository.findById(id).orElse(null);
        if (yh == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "用户不存在");
            return jieGuo;
        }
        if (xingMing == null || xingMing.trim().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "昵称不能为空");
            return jieGuo;
        }
        yh.setXingMing(xingMing.trim());
        yh.setShouJi(shouJi == null ? null : shouJi.trim());
        yh.setXueHao(xueHao == null ? null : xueHao.trim());
        if (miMa != null && !miMa.trim().isEmpty()) {
            if (miMa.trim().length() < 6) {
                jieGuo.put("chengGong", false);
                jieGuo.put("xinXi", "新密码至少 6 位");
                return jieGuo;
            }
            yh.setMiMa(Md5.jiaMi(miMa.trim()));
        }
        if (touXiang != null) {
            // 传空串表示恢复默认头像，传地址表示设置头像
            yh.setTouXiang(touXiang.trim().isEmpty() ? null : touXiang.trim());
        }
        yongHuRepository.save(yh);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    // ==================== 商品管理 ====================

    /**
     * 管理员发布商品：可代任意用户上架，未指定发布者时记在当前管理员名下。
     */
    @PostMapping("/shangPin/fabu")
    public Map<String, Object> faBuShangPin(@RequestBody ShangPin shangPin,
                                            @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu guanLiYuan = guanLiYuanId == null ? null : yongHuRepository.findById(guanLiYuanId).orElse(null);
        if (guanLiYuan == null || guanLiYuan.getGuanLiYuan() == null || !guanLiYuan.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以发布商品，请重新登录后台");
            return jieGuo;
        }
        if (shangPin.getBiaoTi() == null || shangPin.getBiaoTi().trim().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "商品标题不能为空");
            return jieGuo;
        }
        if (shangPin.getJiaGe() == null || shangPin.getJiaGe() < 0) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请填写正确的商品价格");
            return jieGuo;
        }
        Long faBuRenId = shangPin.getYongHuId() == null ? guanLiYuan.getId() : shangPin.getYongHuId();
        YongHu faBuRen = yongHuRepository.findById(faBuRenId).orElse(guanLiYuan);
        shangPin.setId(null);
        shangPin.setYongHuId(faBuRen.getId());
        shangPin.setYongHuMing(faBuRen.getXingMing() != null && !faBuRen.getXingMing().isEmpty()
                ? faBuRen.getXingMing() : faBuRen.getYongHuMing());
        shangPin.setZhuangTai(0);
        shangPin.setFabuShiJian(new Date());
        jieGuo.put("chengGong", true);
        jieGuo.put("shangPin", shangPinRepository.save(shangPin));
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

    /**
     * 管理员编辑商品：标题 / 描述 / 价格 / 图片 / 分类，发布者与发布时间保持不变。
     * tuPian 传空字符串表示清空图片，字段不传则保持原值。
     */
    @PostMapping("/shangPin/xiugai")
    public Map<String, Object> bianJiShangPin(@RequestBody ShangPin shangPin,
                                              @RequestHeader(value = "GuanLiYuanId", required = false) Long guanLiYuanId) {
        Map<String, Object> jieGuo = new HashMap<>();
        YongHu guanLiYuan = guanLiYuanId == null ? null : yongHuRepository.findById(guanLiYuanId).orElse(null);
        if (guanLiYuan == null || guanLiYuan.getGuanLiYuan() == null || !guanLiYuan.getGuanLiYuan()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "只有管理员可以编辑商品，请重新登录后台");
            return jieGuo;
        }
        if (shangPin.getId() == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "缺少商品编号");
            return jieGuo;
        }
        ShangPin yuan = shangPinRepository.findById(shangPin.getId()).orElse(null);
        if (yuan == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "商品不存在");
            return jieGuo;
        }
        if (shangPin.getBiaoTi() != null) {
            if (shangPin.getBiaoTi().trim().isEmpty()) {
                jieGuo.put("chengGong", false);
                jieGuo.put("xinXi", "商品标题不能为空");
                return jieGuo;
            }
            yuan.setBiaoTi(shangPin.getBiaoTi().trim());
        }
        if (shangPin.getJiaGe() != null) {
            if (shangPin.getJiaGe() < 0) {
                jieGuo.put("chengGong", false);
                jieGuo.put("xinXi", "请填写正确的商品价格");
                return jieGuo;
            }
            yuan.setJiaGe(shangPin.getJiaGe());
        }
        if (shangPin.getMiaoShu() != null) yuan.setMiaoShu(shangPin.getMiaoShu());
        if (shangPin.getTuPian() != null) yuan.setTuPian(shangPin.getTuPian().trim());
        if (shangPin.getFenLeiId() != null) yuan.setFenLeiId(shangPin.getFenLeiId());
        jieGuo.put("chengGong", true);
        jieGuo.put("shangPin", shangPinRepository.save(yuan));
        return jieGuo;
    }

    @PostMapping("/shangPin/shanchu")
    public Map<String, Object> shanChuShangPin(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        shangPinRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    /** 下架商品 */
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

    /** 恢复上架（已下架 -> 在售） */
    @PostMapping("/shangPin/shangjia")
    public Map<String, Object> shangJiaShangPin(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        ShangPin sp = shangPinRepository.findById(id).orElse(null);
        if (sp == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "商品不存在");
            return jieGuo;
        }
        sp.setZhuangTai(0);
        shangPinRepository.save(sp);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    // ==================== 订单管理 ====================

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

    @PostMapping("/dingDan/shanchu")
    public Map<String, Object> shanChuDingDan(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        dingDanRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    // ==================== 分类管理 ====================

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

    // ==================== 轮播图管理 ====================

    @GetMapping("/lunbo")
    public Map<String, Object> lunbo() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", lunBoTuRepository.findAllByOrderByPaiXuAscIdAsc());
        return jieGuo;
    }

    @PostMapping("/lunbo/baocun")
    public Map<String, Object> baoCunLunBo(@RequestBody LunBoTu lunBoTu) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (lunBoTu.getTuPian() == null || lunBoTu.getTuPian().trim().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请填写轮播图片地址");
            return jieGuo;
        }
        if (lunBoTu.getPaiXu() == null) {
            lunBoTu.setPaiXu(0);
        }
        if (lunBoTu.getZhuangTai() == null) {
            lunBoTu.setZhuangTai(0);
        }
        Date chuangJian = null;
        if (lunBoTu.getId() != null) {
            LunBoTu jiu = lunBoTuRepository.findById(lunBoTu.getId()).orElse(null);
            if (jiu != null) {
                chuangJian = jiu.getChuangJianShiJian();
            }
        }
        lunBoTu.setChuangJianShiJian(chuangJian == null ? new Date() : chuangJian);
        jieGuo.put("chengGong", true);
        jieGuo.put("lunBoTu", lunBoTuRepository.save(lunBoTu));
        return jieGuo;
    }

    @PostMapping("/lunbo/shanchu")
    public Map<String, Object> shanChuLunBo(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        lunBoTuRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @PostMapping("/lunbo/zhuangtai")
    public Map<String, Object> lunBoZhuangTai(@RequestParam Long id, @RequestParam Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        LunBoTu lb = lunBoTuRepository.findById(id).orElse(null);
        if (lb == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "轮播图不存在");
            return jieGuo;
        }
        lb.setZhuangTai(zhuangTai);
        lunBoTuRepository.save(lb);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    // ==================== 公告管理 ====================

    @GetMapping("/gonggao")
    public Map<String, Object> gonggao() {
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("lieBiao", gongGaoRepository.findAllByOrderByChuangJianShiJianDescIdDesc());
        return jieGuo;
    }

    @PostMapping("/gonggao/baocun")
    public Map<String, Object> baoCunGongGao(@RequestBody GongGao gongGao) {
        Map<String, Object> jieGuo = new HashMap<>();
        if (gongGao.getBiaoTi() == null || gongGao.getBiaoTi().trim().isEmpty()) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请填写公告标题");
            return jieGuo;
        }
        if (gongGao.getZhuangTai() == null) {
            gongGao.setZhuangTai(0);
        }
        Date chuangJian = null;
        if (gongGao.getId() != null) {
            GongGao jiu = gongGaoRepository.findById(gongGao.getId()).orElse(null);
            if (jiu != null) {
                chuangJian = jiu.getChuangJianShiJian();
            }
        }
        gongGao.setChuangJianShiJian(chuangJian == null ? new Date() : chuangJian);
        jieGuo.put("chengGong", true);
        jieGuo.put("gongGao", gongGaoRepository.save(gongGao));
        return jieGuo;
    }

    @PostMapping("/gonggao/shanchu")
    public Map<String, Object> shanChuGongGao(@RequestParam Long id) {
        Map<String, Object> jieGuo = new HashMap<>();
        gongGaoRepository.deleteById(id);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }

    @PostMapping("/gonggao/zhuangtai")
    public Map<String, Object> gongGaoZhuangTai(@RequestParam Long id, @RequestParam Integer zhuangTai) {
        Map<String, Object> jieGuo = new HashMap<>();
        GongGao gg = gongGaoRepository.findById(id).orElse(null);
        if (gg == null) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "公告不存在");
            return jieGuo;
        }
        gg.setZhuangTai(zhuangTai);
        gongGaoRepository.save(gg);
        jieGuo.put("chengGong", true);
        return jieGuo;
    }
}
