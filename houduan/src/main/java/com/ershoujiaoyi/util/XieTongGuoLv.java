// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.util;

import com.ershoujiaoyi.entity.DingDan;
import com.ershoujiaoyi.entity.ShangPin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基于物品的协同过滤（Item-Based Collaborative Filtering）。
 *
 * 打分矩阵：
 * 1. 订单行为 —— 已完成 1.0、待收货 0.8、待发货 0.6，已取消视为负反馈不计入；
 * 2. 发布行为 —— 0.5 分，属于隐式兴趣，主要用于新用户冷启动。
 *
 * 物品相似度 = 行为余弦相似度 + 同类目加成 + 同发布者加成，最后截断到 [0, 1]，
 * 既保留「买过这个的人还买过那个」的协同信号，又在没有足够订单时不至于推不出东西。
 */
public class XieTongGuoLv {

    /** 行为余弦相似度权重 */
    private static final double QUAN_XING_WEI = 1.0;
    /** 同类目加成 */
    private static final double QUAN_LEI_MU = 0.25;
    /** 同发布者加成 */
    private static final double QUAN_FA_BU_ZHE = 0.12;

    /** 用户 ->（商品 -> 兴趣分） */
    private final Map<Long, Map<Long, Double>> yongHuXingQu = new HashMap<>();
    /** 商品 ->（用户 -> 兴趣分） */
    private final Map<Long, Map<Long, Double>> shangPinYongHu = new HashMap<>();
    /** 商品被下单次数（热度） */
    private final Map<Long, Integer> shangPinReDu = new HashMap<>();
    /** 商品 -> 分类 */
    private final Map<Long, Long> shangPinFenLei = new HashMap<>();
    /** 商品 -> 发布者 */
    private final Map<Long, Long> shangPinFaBuZhe = new HashMap<>();

    public XieTongGuoLv(List<DingDan> dingDanLie, List<ShangPin> shangPinLie) {
        if (shangPinLie != null) {
            for (ShangPin sp : shangPinLie) {
                if (sp.getId() == null) continue;
                shangPinFenLei.put(sp.getId(), sp.getFenLeiId());
                shangPinFaBuZhe.put(sp.getId(), sp.getYongHuId());
                if (sp.getYongHuId() != null) {
                    xieRu(sp.getYongHuId(), sp.getId(), 0.5);
                }
            }
        }
        if (dingDanLie != null) {
            for (DingDan dd : dingDanLie) {
                if (dd.getShangPinId() == null || dd.getMaiJiaId() == null) continue;
                if (Integer.valueOf(3).equals(dd.getZhuangTai())) continue;
                double fen = Integer.valueOf(2).equals(dd.getZhuangTai()) ? 1.0
                        : Integer.valueOf(1).equals(dd.getZhuangTai()) ? 0.8 : 0.6;
                xieRu(dd.getMaiJiaId(), dd.getShangPinId(), fen);
                shangPinReDu.merge(dd.getShangPinId(), 1, Integer::sum);
            }
        }
    }

    /** 写入一条 用户-商品 兴趣分，同一对取较高分 */
    private void xieRu(Long yongHuId, Long shangPinId, double fen) {
        Map<Long, Double> yiYou = yongHuXingQu.computeIfAbsent(yongHuId, k -> new HashMap<>());
        yiYou.merge(shangPinId, fen, Math::max);
        shangPinYongHu.computeIfAbsent(shangPinId, k -> new HashMap<>()).merge(yongHuId, fen, Math::max);
    }

    public Map<Long, Double> yongHuXingQu(Long yongHuId) {
        return yongHuId == null ? new HashMap<>() : yongHuXingQu.getOrDefault(yongHuId, new HashMap<>());
    }

    public int reDu(Long shangPinId) {
        return shangPinReDu.getOrDefault(shangPinId, 0);
    }

    /** 纯行为余弦相似度 */
    private double xingWeiXiangSi(Long shangPinA, Long shangPinB) {
        Map<Long, Double> a = shangPinYongHu.get(shangPinA);
        Map<Long, Double> b = shangPinYongHu.get(shangPinB);
        if (a == null || b == null) return 0D;
        double dianJi = 0D, moA = 0D, moB = 0D;
        for (Map.Entry<Long, Double> e : a.entrySet()) {
            moA += e.getValue() * e.getValue();
            Double vb = b.get(e.getKey());
            if (vb != null) dianJi += e.getValue() * vb;
        }
        for (Double v : b.values()) moB += v * v;
        if (moA <= 0 || moB <= 0) return 0D;
        return dianJi / (Math.sqrt(moA) * Math.sqrt(moB));
    }

    /** 综合相似度：行为 + 同类目 + 同发布者 */
    public double xiangSiDu(Long shangPinA, Long shangPinB) {
        if (shangPinA == null || shangPinB == null || shangPinA.equals(shangPinB)) return 0D;
        double fen = QUAN_XING_WEI * xingWeiXiangSi(shangPinA, shangPinB);
        Long leiA = shangPinFenLei.get(shangPinA);
        Long leiB = shangPinFenLei.get(shangPinB);
        if (leiA != null && leiA.equals(leiB)) fen += QUAN_LEI_MU;
        Long zhuA = shangPinFaBuZhe.get(shangPinA);
        Long zhuB = shangPinFaBuZhe.get(shangPinB);
        if (zhuA != null && zhuA.equals(zhuB)) fen += QUAN_FA_BU_ZHE;
        return Math.min(1D, fen);
    }

    /**
     * 给目标用户打分并排序。
     *
     * @param yongHuId 目标用户
     * @param houXuan  候选商品 id（一般为在售且非本人发布）
     * @param shu      需要的条数
     */
    public List<Long> tuiJian(Long yongHuId, Collection<Long> houXuan, int shu) {
        Map<Long, Double> deFen = new LinkedHashMap<>();
        if (houXuan == null || houXuan.isEmpty()) return new ArrayList<>();
        Map<Long, Double> benRen = yongHuXingQu(yongHuId);
        if (benRen.isEmpty()) return new ArrayList<>();
        Set<Long> yiJiSuan = new HashSet<>();
        for (Long muBiao : houXuan) {
            if (muBiao == null || benRen.containsKey(muBiao)) continue;
            double fen = 0D;
            for (Map.Entry<Long, Double> e : benRen.entrySet()) {
                if (muBiao.equals(e.getKey()) || !yiJiSuan.add(e.getKey())) continue;
                double xiangSi = xiangSiDu(e.getKey(), muBiao);
                if (xiangSi > 0) fen += xiangSi * e.getValue();
            }
            if (fen > 0) deFen.put(muBiao, fen);
        }
        return paiXuQuQian(deFen, shu);
    }

    /** 与目标商品最相似的商品（详情页「看了又看」） */
    public List<Long> xiangGuan(Long shangPinId, Collection<Long> houXuan, int shu) {
        Map<Long, Double> deFen = new LinkedHashMap<>();
        if (shangPinId == null || houXuan == null) return new ArrayList<>();
        for (Long muBiao : houXuan) {
            if (muBiao == null || muBiao.equals(shangPinId)) continue;
            double xiangSi = xiangSiDu(shangPinId, muBiao);
            if (xiangSi > 0) deFen.put(muBiao, xiangSi);
        }
        return paiXuQuQian(deFen, shu);
    }

    private List<Long> paiXuQuQian(Map<Long, Double> deFen, int shu) {
        List<Long> jieGuo = new ArrayList<>(deFen.keySet());
        jieGuo.sort(Comparator.comparingDouble((Long id) -> -deFen.getOrDefault(id, 0D))
                .thenComparingLong(id -> -reDu(id)));
        return jieGuo.size() > shu ? jieGuo.subList(0, shu) : jieGuo;
    }
}
