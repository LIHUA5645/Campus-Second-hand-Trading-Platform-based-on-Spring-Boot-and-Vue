// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.service;

import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.repository.DingDanRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.ershoujiaoyi.util.XieTongGuoLv;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 推荐服务：用「基于物品的协同过滤」算出候选，再用热度 + 最新补足。
 */
@Service
public class TuiJianFuWu {

    private final ShangPinRepository shangPinRepository;
    private final DingDanRepository dingDanRepository;

    public TuiJianFuWu(ShangPinRepository shangPinRepository, DingDanRepository dingDanRepository) {
        this.shangPinRepository = shangPinRepository;
        this.dingDanRepository = dingDanRepository;
    }

    /** 猜你喜欢：协同过滤结果在前，不足时用热门 + 最新兜底 */
    public List<ShangPin> tuiJian(Long yongHuId, int shu) {
        if (shu <= 0) return new ArrayList<>();
        List<ShangPin> zaiShou = shangPinRepository.findAll().stream()
                .filter(s -> Integer.valueOf(0).equals(s.getZhuangTai()))
                .collect(Collectors.toList());
        if (zaiShou.isEmpty()) return new ArrayList<>();

        Map<Long, ShangPin> suoYin = zaiShou.stream()
                .collect(Collectors.toMap(ShangPin::getId, Function.identity(), (a, b) -> a));
        List<Long> houXuan = zaiShou.stream()
                .filter(s -> yongHuId == null || !yongHuId.equals(s.getYongHuId()))
                .map(ShangPin::getId)
                .collect(Collectors.toList());

        XieTongGuoLv moXing = new XieTongGuoLv(dingDanRepository.findAll(), shangPinRepository.findAll());
        List<Long> jieGuo = new ArrayList<>(moXing.tuiJian(yongHuId, houXuan, shu));

        if (jieGuo.size() < shu) {
            List<ShangPin> houBei = zaiShou.stream()
                    .filter(s -> yongHuId == null || !yongHuId.equals(s.getYongHuId()))
                    .sorted(Comparator.comparingLong((ShangPin s) -> -moXing.reDu(s.getId()))
                            .thenComparing((ShangPin s) -> s.getFabuShiJian() == null ? 0L : s.getFabuShiJian().getTime(), Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            for (ShangPin sp : houBei) {
                if (jieGuo.size() >= shu) break;
                if (!jieGuo.contains(sp.getId())) jieGuo.add(sp.getId());
            }
        }
        Set<Long> quChong = new LinkedHashSet<>(jieGuo);
        return quChong.stream().map(suoYin::get).filter(java.util.Objects::nonNull).collect(Collectors.toList());
    }

    /** 看了又看：同款附近的好物，不足时用同类目商品兜底 */
    public List<ShangPin> xiangGuan(Long shangPinId, int shu) {
        if (shangPinId == null || shu <= 0) return new ArrayList<>();
        List<ShangPin> zaiShou = shangPinRepository.findAll().stream()
                .filter(s -> Integer.valueOf(0).equals(s.getZhuangTai()))
                .collect(Collectors.toList());
        Map<Long, ShangPin> suoYin = zaiShou.stream()
                .collect(Collectors.toMap(ShangPin::getId, Function.identity(), (a, b) -> a));
        ShangPin dangQian = suoYin.get(shangPinId);
        if (dangQian == null) return new ArrayList<>();

        List<Long> houXuan = zaiShou.stream().map(ShangPin::getId).collect(Collectors.toList());
        XieTongGuoLv moXing = new XieTongGuoLv(dingDanRepository.findAll(), shangPinRepository.findAll());
        List<Long> jieGuo = new ArrayList<>(moXing.xiangGuan(shangPinId, houXuan, shu));

        if (jieGuo.size() < shu) {
            List<ShangPin> tongLei = zaiShou.stream()
                    .filter(s -> !shangPinId.equals(s.getId()) && dangQian.getFenLeiId() != null
                            && dangQian.getFenLeiId().equals(s.getFenLeiId()))
                    .sorted(Comparator.comparing((ShangPin s) -> s.getFabuShiJian() == null ? 0L : s.getFabuShiJian().getTime()).reversed())
                    .collect(Collectors.toList());
            for (ShangPin sp : tongLei) {
                if (jieGuo.size() >= shu) break;
                if (!jieGuo.contains(sp.getId())) jieGuo.add(sp.getId());
            }
        }
        Set<Long> quChong = new LinkedHashSet<>(jieGuo);
        return quChong.stream().map(suoYin::get).filter(java.util.Objects::nonNull).collect(Collectors.toList());
    }
}
