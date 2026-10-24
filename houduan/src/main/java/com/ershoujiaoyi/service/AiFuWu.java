// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.service;

import com.ershoujiaoyi.entity.AiPeiZhi;
import com.ershoujiaoyi.entity.FenLei;
import com.ershoujiaoyi.entity.ShangPin;
import com.ershoujiaoyi.repository.AiPeiZhiRepository;
import com.ershoujiaoyi.repository.FenLeiRepository;
import com.ershoujiaoyi.repository.ShangPinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 智能导购服务：
 * 1. 把在售商品清单交给大模型，让模型按用户需求挑选商品（识图时把图片一起发给视觉模型）。
 * 2. 管理员可配置多个密钥，按顺序逐个尝试，某个密钥报错自动切换下一个。
 * 3. 全部失败（或未配置密钥）时，自动降级为本地关键词 + 价格匹配，保证用户始终有结果。
 */
@Service
public class AiFuWu {

    /** 一次最多把多少件在售商品交给大模型，防止提示词过长 */
    private static final int SHANG_PIN_SHANG_XIAN = 150;
    private static final Duration CHAO_SHI = Duration.ofSeconds(90);

    private static final Pattern SHU_ZI = Pattern.compile("(\\d+(?:\\.\\d+)?)");
    private static final Pattern MA_KUAI = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```");

    private final AiPeiZhiRepository aiPeiZhiRepository;
    private final ShangPinRepository shangPinRepository;
    private final FenLeiRepository fenLeiRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    public AiFuWu(AiPeiZhiRepository aiPeiZhiRepository,
                  ShangPinRepository shangPinRepository,
                  FenLeiRepository fenLeiRepository) {
        this.aiPeiZhiRepository = aiPeiZhiRepository;
        this.shangPinRepository = shangPinRepository;
        this.fenLeiRepository = fenLeiRepository;
    }

    // ==================== 对外：智能选品 ====================

    /**
     * @param wenTi 用户问题，如「预算 300 的二手平板」
     * @param tuPian 图片（data:image/...;base64,xxx 或可访问的图片地址），识图用，可为空
     * @param shu 需要推荐的数量
     */
    public Map<String, Object> xuanShangPin(String wenTi, String tuPian, Integer shu) {
        int tiaoShu = shu == null || shu <= 0 ? 6 : Math.min(shu, 12);
        Map<String, Object> jieGuo = new HashMap<>();
        String wen = wenTi == null ? "" : wenTi.trim();
        boolean youTu = tuPian != null && !tuPian.trim().isEmpty();

        List<ShangPin> zaiShou = huoQuZaiShou();
        if (zaiShou.isEmpty()) {
            jieGuo.put("chengGong", true);
            jieGuo.put("huiDa", "平台上暂时还没有在售的闲置，等同学发布后再来问我吧～");
            jieGuo.put("lieBiao", new ArrayList<>());
            jieGuo.put("moShi", "无商品");
            return jieGuo;
        }
        if (wen.isEmpty() && !youTu) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "请告诉我你想找什么，或者上传一张图片让我识图找同款");
            return jieGuo;
        }

        List<AiPeiZhi> keYong = xuanKeYongPeiZhi(youTu);
        StringBuilder cuoWu = new StringBuilder();
        for (AiPeiZhi peiZhi : keYong) {
            try {
                String yuanWen = diaoYongMoXing(peiZhi, wen, youTu ? tuPian : null, zaiShou, tiaoShu);
                Map<String, Object> jieXi = jieXiHuiDa(yuanWen, zaiShou, tiaoShu);
                jiLuChengGong(peiZhi);
                jieGuo.put("chengGong", true);
                jieGuo.put("huiDa", jieXi.get("shuoMing"));
                jieGuo.put("lieBiao", jieXi.get("lieBiao"));
                jieGuo.put("moShi", youTu ? "AI 识图选品" : "AI 智能选品");
                jieGuo.put("moXing", peiZhi.getMoXing());
                return jieGuo;
            } catch (Exception e) {
                jiLuCuoWu(peiZhi, e.getMessage());
                if (cuoWu.length() > 0) cuoWu.append("；");
                cuoWu.append(miaoShuCuoWu(e.getMessage()));
            }
        }

        Map<String, Object> benDi = benDiPiPei(wen, zaiShou, tiaoShu, youTu);
        jieGuo.put("chengGong", true);
        jieGuo.put("huiDa", benDi.get("shuoMing"));
        jieGuo.put("lieBiao", benDi.get("lieBiao"));
        jieGuo.put("moShi", youTu ? "识图降级 · 本地匹配" : "本地匹配");
        jieGuo.put("jiangJi", true);
        if (cuoWu.length() > 0) jieGuo.put("yuanYin", cuoWu.toString());
        return jieGuo;
    }

    /** 测试某个密钥是否可用 */
    public Map<String, Object> ceShi(AiPeiZhi peiZhi) {
        Map<String, Object> jieGuo = new HashMap<>();
        long kaiShi = System.currentTimeMillis();
        try {
            String neiRong = diaoYongMoXing(peiZhi, "回复两个字：正常", null, new ArrayList<>(), 1);
            jiLuChengGong(peiZhi);
            String jian = neiRong == null ? "" : neiRong.trim().replace("\n", " ");
            if (jian.length() > 40) jian = jian.substring(0, 40) + "…";
            jieGuo.put("chengGong", true);
            jieGuo.put("xinXi", "连接成功，模型回复：" + (jian.isEmpty() ? "（空）" : jian));
            jieGuo.put("haoShi", System.currentTimeMillis() - kaiShi);
        } catch (Exception e) {
            jiLuCuoWu(peiZhi, e.getMessage());
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", miaoShuCuoWu(e.getMessage()));
            jieGuo.put("haoShi", System.currentTimeMillis() - kaiShi);
        }
        return jieGuo;
    }

    /** 前台查询：是否已有可用密钥 */
    public Map<String, Object> zhuangTai() {
        List<AiPeiZhi> quanBu = aiPeiZhiRepository.findAllByOrderByPaiXuAscIdAsc();
        long qiYong = quanBu.stream().filter(this::qiYong).count();
        long shiTu = quanBu.stream().filter(p -> qiYong(p) && Boolean.TRUE.equals(p.getShiTu())).count();
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("zongShu", quanBu.size());
        jieGuo.put("qiYongShu", qiYong);
        jieGuo.put("shiTuShu", shiTu);
        jieGuo.put("keYong", qiYong > 0);
        jieGuo.put("shiTuKeYong", shiTu > 0);
        return jieGuo;
    }

    // ==================== 调用大模型 ====================

    private String diaoYongMoXing(AiPeiZhi peiZhi, String wenTi, String tuPian,
                                  List<ShangPin> zaiShou, int tiaoShu) throws Exception {
        if (peiZhi.getDiZhi() == null || peiZhi.getDiZhi().trim().isEmpty()) {
            throw new IllegalStateException("接口地址为空");
        }
        if (peiZhi.getMiYao() == null || peiZhi.getMiYao().trim().isEmpty()) {
            throw new IllegalStateException("API 密钥为空");
        }
        if (peiZhi.getMoXing() == null || peiZhi.getMoXing().trim().isEmpty()) {
            throw new IllegalStateException("模型名称为空");
        }

        ObjectNode qingQiu = mapper.createObjectNode();
        qingQiu.put("model", peiZhi.getMoXing().trim());
        qingQiu.put("temperature", 0.3);
        qingQiu.put("max_tokens", 900);

        ArrayNode xiaoXi = mapper.createArrayNode();
        xiaoXi.add(xiaoXi("system", xiTongTiShi()));
        if (tuPian != null && !tuPian.trim().isEmpty()) {
            ObjectNode yongHu = mapper.createObjectNode();
            yongHu.put("role", "user");
            ArrayNode neiRong = mapper.createArrayNode();
            neiRong.add(leiXing("text", yongHuTiShi(wenTi, zaiShou, tiaoShu, true)));
            ObjectNode tu = mapper.createObjectNode();
            tu.put("type", "image_url");
            ObjectNode tuDiZhi = mapper.createObjectNode();
            tuDiZhi.put("url", tuPian.trim());
            tu.set("image_url", tuDiZhi);
            neiRong.add(tu);
            yongHu.set("content", neiRong);
            xiaoXi.add(yongHu);
        } else {
            xiaoXi.add(xiaoXi("user", yongHuTiShi(wenTi, zaiShou, tiaoShu, false)));
        }
        qingQiu.set("messages", xiaoXi);

        HttpRequest qing = HttpRequest.newBuilder()
                .uri(URI.create(zuHeDiZhi(peiZhi.getDiZhi())))
                .timeout(CHAO_SHI)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "Bearer " + peiZhi.getMiYao().trim())
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(qingQiu), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> xiangYing = httpClient.send(qing, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        int zhuangTaiMa = xiangYing.statusCode();
        String ti = xiangYing.body() == null ? "" : xiangYing.body();
        if (zhuangTaiMa < 200 || zhuangTaiMa >= 300) {
            throw new IllegalStateException("HTTP " + zhuangTaiMa + " " + jieQu(ti, 160));
        }
        JsonNode gen = mapper.readTree(ti);
        JsonNode xuanZe = gen.path("choices");
        if (xuanZe.isArray() && xuanZe.size() > 0) {
            JsonNode wen = xuanZe.get(0).path("message").path("content");
            if (!wen.isMissingNode() && !wen.isNull()) {
                String neiRong = wen.asText();
                if (neiRong != null && !neiRong.trim().isEmpty()) return neiRong;
            }
        }
        JsonNode cuo = gen.path("error").path("message");
        if (!cuo.isMissingNode()) throw new IllegalStateException("接口返回错误：" + cuo.asText());
        throw new IllegalStateException("接口返回内容为空：" + jieQu(ti, 160));
    }

    private String zuHeDiZhi(String diZhi) {
        String d = diZhi.trim();
        while (d.endsWith("/")) d = d.substring(0, d.length() - 1);
        if (d.endsWith("/chat/completions")) return d;
        return d + "/chat/completions";
    }

    private ObjectNode xiaoXi(String jueSe, String neiRong) {
        ObjectNode node = mapper.createObjectNode();
        node.put("role", jueSe);
        node.put("content", neiRong);
        return node;
    }

    private ObjectNode leiXing(String lei, String neiRong) {
        ObjectNode node = mapper.createObjectNode();
        node.put("type", lei);
        node.put("text", neiRong);
        return node;
    }

    private String xiTongTiShi() {
        return "你是校园二手交易平台的智能导购助手「小淘」，只为本校同学服务。\n"
                + "严格规则：\n"
                + "1. 只能从给定的在售商品列表中挑选，绝对不能编造不存在的商品。\n"
                + "2. 只输出一个 JSON 对象，不要输出任何多余文字、解释或 Markdown 代码块标记。\n"
                + "3. 输出格式：{\"shuoMing\":\"一句话答复用户，语气亲切简洁\",\"tuiJian\":[{\"id\":商品ID数字,\"liYou\":\"20字以内推荐理由\"}]}\n"
                + "4. tuiJian 按匹配度从高到低排列，id 必须是列表里真实存在的数字。\n"
                + "5. 如果没有合适的商品，tuiJian 返回空数组，并在 shuoMing 里说明原因并给出选购建议。";
    }

    private String yongHuTiShi(String wenTi, List<ShangPin> zaiShou, int tiaoShu, boolean youTu) {
        StringBuilder sb = new StringBuilder();
        if (youTu) {
            sb.append("请先仔细看这张图片，识别图片里的物品（品类、品牌型号、成色、大致用途），"
                    + "然后从下面的在售商品里找出同类、同款或能满足同样需求的闲置。\n");
        }
        sb.append("用户需求：").append(wenTi == null || wenTi.trim().isEmpty()
                ? (youTu ? "（用户只上传了图片，请根据图片内容理解需求）" : "（未填写）") : wenTi.trim()).append("\n");
        sb.append("最多推荐 ").append(tiaoShu).append(" 件。\n");
        sb.append("在售商品列表（JSON 数组，字段：id / 标题 / 价格 / 分类 / 描述）：\n");
        sb.append(shangPinQingDan(zaiShou));
        return sb.toString();
    }

    private String shangPinQingDan(List<ShangPin> zaiShou) {
        Map<Long, String> fenLeiMing = fenLeiMingBiao();
        ArrayNode lie = mapper.createArrayNode();
        int shu = Math.min(zaiShou.size(), SHANG_PIN_SHANG_XIAN);
        for (int i = 0; i < shu; i++) {
            ShangPin sp = zaiShou.get(i);
            ObjectNode xiang = mapper.createObjectNode();
            xiang.put("id", sp.getId() == null ? 0L : sp.getId());
            xiang.put("biaoTi", sp.getBiaoTi() == null ? "" : sp.getBiaoTi());
            xiang.put("jiaGe", sp.getJiaGe() == null ? 0D : sp.getJiaGe());
            xiang.put("fenLei", sp.getFenLeiId() == null ? "" : fenLeiMing.getOrDefault(sp.getFenLeiId(), ""));
            String miao = sp.getMiaoShu() == null ? "" : sp.getMiaoShu().replace("\n", " ").trim();
            xiang.put("miaoShu", miao.length() > 80 ? miao.substring(0, 80) : miao);
            lie.add(xiang);
        }
        return lie.toString();
    }

    // ==================== 解析模型返回 ====================

    private Map<String, Object> jieXiHuiDa(String yuanWen, List<ShangPin> zaiShou, int tiaoShu) {
        Map<String, Object> jieGuo = new HashMap<>();
        String shuoMing = "";
        List<Map<String, Object>> lieBiao = new ArrayList<>();
        String jing = tiQuJson(yuanWen);
        try {
            JsonNode gen = mapper.readTree(jing);
            if (gen.path("shuoMing").isTextual()) shuoMing = gen.path("shuoMing").asText();
            JsonNode tuiJian = gen.path("tuiJian");
            if (tuiJian.isArray()) {
                Map<Long, ShangPin> suoYin = new HashMap<>();
                for (ShangPin sp : zaiShou) if (sp.getId() != null) suoYin.put(sp.getId(), sp);
                Map<Long, String> fenLeiMing = fenLeiMingBiao();
                for (JsonNode xiang : tuiJian) {
                    if (lieBiao.size() >= tiaoShu) break;
                    long id = xiang.path("id").asLong(-1L);
                    ShangPin sp = id < 0 ? null : suoYin.get(id);
                    if (sp == null) continue;
                    Map<String, Object> tiao = shangPinTiao(sp, fenLeiMing);
                    tiao.put("liYou", xiang.path("liYou").asText(""));
                    lieBiao.add(tiao);
                }
            }
        } catch (Exception e) {
            shuoMing = jieQu(yuanWen, 200);
        }
        if (shuoMing == null || shuoMing.trim().isEmpty()) shuoMing = "我帮你挑了几件，看看合不合适～";
        jieGuo.put("shuoMing", shuoMing.trim());
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    private String tiQuJson(String yuanWen) {
        if (yuanWen == null) return "{}";
        String wen = yuanWen.trim();
        Matcher ma = MA_KUAI.matcher(wen);
        if (ma.find()) wen = ma.group(1).trim();
        int qi = wen.indexOf('{');
        int zhi = wen.lastIndexOf('}');
        if (qi >= 0 && zhi > qi) return wen.substring(qi, zhi + 1);
        return wen;
    }

    // ==================== 本地兜底匹配 ====================

    private Map<String, Object> benDiPiPei(String wenTi, List<ShangPin> zaiShou, int tiaoShu, boolean youTu) {
        Map<String, Object> jieGuo = new HashMap<>();
        Map<Long, String> fenLeiMing = fenLeiMingBiao();
        String wen = wenTi == null ? "" : wenTi.toLowerCase(java.util.Locale.ROOT);
        Set<String> ci = fenCi(wen);
        Double zuiGao = zuiGaoJia(wen);
        Double zuiDi = zuiDiJia(wen);

        List<Object[]> pai = new ArrayList<>();
        for (ShangPin sp : zaiShou) {
            if (zuiGao != null && sp.getJiaGe() != null && sp.getJiaGe() > zuiGao) continue;
            if (zuiDi != null && sp.getJiaGe() != null && sp.getJiaGe() < zuiDi) continue;
            String biaoTi = sp.getBiaoTi() == null ? "" : sp.getBiaoTi();
            String miaoShu = sp.getMiaoShu() == null ? "" : sp.getMiaoShu();
            String fenLei = sp.getFenLeiId() == null ? "" : fenLeiMing.getOrDefault(sp.getFenLeiId(), "");
            int fen = 0;
            for (String c : ci) {
                if (biaoTi.toLowerCase(java.util.Locale.ROOT).contains(c)) fen += 6;
                else if (fenLei.contains(c)) fen += 4;
                else if (miaoShu.toLowerCase(java.util.Locale.ROOT).contains(c)) fen += 2;
            }
            if (fen > 0) pai.add(new Object[]{fen, sp});
        }
        pai.sort((a, b) -> (Integer) b[0] - (Integer) a[0]);

        List<Map<String, Object>> lieBiao = new ArrayList<>();
        for (Object[] hang : pai) {
            if (lieBiao.size() >= tiaoShu) break;
            ShangPin sp = (ShangPin) hang[1];
            Map<String, Object> tiao = shangPinTiao(sp, fenLeiMing);
            tiao.put("liYou", "关键词匹配度 " + hang[0] + " 分");
            lieBiao.add(tiao);
        }

        String shuoMing;
        if (lieBiao.isEmpty()) {
            shuoMing = youTu
                    ? "没有识别到完全一致的闲置，下面这些是平台上比较新的好物，先看看～"
                    : "暂时没有找到完全匹配的闲置，下面这些是平台上比较新的好物，先看看～";
            for (ShangPin sp : zaiShou) {
                if (lieBiao.size() >= tiaoShu) break;
                lieBiao.add(shangPinTiao(sp, fenLeiMing));
            }
        } else {
            StringBuilder jia = new StringBuilder();
            if (zuiGao != null) jia.append("预算 ").append(zuiGao).append(" 以内，");
            if (zuiDi != null) jia.append("价格 ").append(zuiDi).append(" 以上，");
            shuoMing = jia.append("我按关键词帮你筛了 ").append(lieBiao.size()).append(" 件，看看合不合适～").toString();
        }
        jieGuo.put("shuoMing", shuoMing);
        jieGuo.put("lieBiao", lieBiao);
        return jieGuo;
    }

    /** 中文按 2 字切分，英文数字按词切分 */
    private Set<String> fenCi(String wen) {
        Set<String> ci = new HashSet<>();
        if (wen == null) return ci;
        StringBuilder han = new StringBuilder();
        StringBuilder ying = new StringBuilder();
        for (int i = 0; i < wen.length(); i++) {
            char ch = wen.charAt(i);
            if (ch >= 0x4e00 && ch <= 0x9fa5) {
                if (ying.length() > 0) { ci.add(ying.toString()); ying.setLength(0); }
                han.append(ch);
                if (han.length() == 2) { ci.add(han.toString()); han.delete(0, 1); }
            } else {
                if (han.length() > 0) han.setLength(0);
                if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) ying.append(ch);
                else if (ying.length() > 0) { ci.add(ying.toString()); ying.setLength(0); }
            }
        }
        if (ying.length() > 0) ci.add(ying.toString());
        return ci;
    }

    private Double zuiGaoJia(String wen) {
        if (wen == null) return null;
        if (!wen.contains("以内") && !wen.contains("以下") && !wen.contains("不超过")
                && !wen.contains("低于") && !wen.contains("预算") && !wen.contains("之内")) return null;
        Matcher m = SHU_ZI.matcher(wen);
        while (m.find()) {
            try {
                double v = Double.parseDouble(m.group(1));
                if (v > 0) return v;
            } catch (Exception e) { /* 忽略 */ }
        }
        return null;
    }

    private Double zuiDiJia(String wen) {
        if (wen == null) return null;
        if (!wen.contains("以上") && !wen.contains("超过") && !wen.contains("高于")) return null;
        Matcher m = SHU_ZI.matcher(wen);
        while (m.find()) {
            try {
                double v = Double.parseDouble(m.group(1));
                if (v > 0) return v;
            } catch (Exception e) { /* 忽略 */ }
        }
        return null;
    }

    // ==================== 公共小工具 ====================

    private List<ShangPin> huoQuZaiShou() {
        List<ShangPin> zaiShou = new ArrayList<>();
        for (ShangPin sp : shangPinRepository.findAll()) {
            if (Integer.valueOf(0).equals(sp.getZhuangTai())) zaiShou.add(sp);
        }
        zaiShou.sort((a, b) -> {
            long ta = a.getFabuShiJian() == null ? 0L : a.getFabuShiJian().getTime();
            long tb = b.getFabuShiJian() == null ? 0L : b.getFabuShiJian().getTime();
            return Long.compare(tb, ta);
        });
        return zaiShou;
    }

    private List<AiPeiZhi> xuanKeYongPeiZhi(boolean youTu) {
        List<AiPeiZhi> quanBu = aiPeiZhiRepository.findAllByOrderByPaiXuAscIdAsc();
        List<AiPeiZhi> jieGuo = new ArrayList<>();
        if (youTu) {
            for (AiPeiZhi p : quanBu) if (qiYong(p) && Boolean.TRUE.equals(p.getShiTu())) jieGuo.add(p);
        }
        if (jieGuo.isEmpty()) {
            for (AiPeiZhi p : quanBu) if (qiYong(p)) jieGuo.add(p);
        }
        return jieGuo;
    }

    private boolean qiYong(AiPeiZhi peiZhi) {
        return peiZhi != null && Integer.valueOf(0).equals(peiZhi.getZhuangTai());
    }

    private Map<Long, String> fenLeiMingBiao() {
        Map<Long, String> biao = new HashMap<>();
        for (FenLei f : fenLeiRepository.findAll()) {
            if (f.getId() != null) biao.put(f.getId(), f.getMingCheng() == null ? "" : f.getMingCheng());
        }
        return biao;
    }

    private Map<String, Object> shangPinTiao(ShangPin sp, Map<Long, String> fenLeiMing) {
        Map<String, Object> tiao = new HashMap<>();
        tiao.put("id", sp.getId());
        tiao.put("biaoTi", sp.getBiaoTi());
        tiao.put("jiaGe", sp.getJiaGe());
        tiao.put("tuPian", sp.getTuPian());
        tiao.put("miaoShu", sp.getMiaoShu());
        tiao.put("fenLeiMing", sp.getFenLeiId() == null ? "" : fenLeiMing.getOrDefault(sp.getFenLeiId(), ""));
        tiao.put("yongHuMing", sp.getYongHuMing());
        return tiao;
    }

    private void jiLuChengGong(AiPeiZhi peiZhi) {
        try {
            peiZhi.setZuiJinZhuangTai("正常");
            peiZhi.setZuiJinCuoWu("");
            peiZhi.setZuiJinShiYongShiJian(new Date());
            aiPeiZhiRepository.save(peiZhi);
        } catch (Exception e) { /* 状态记录失败不影响主流程 */ }
    }

    private void jiLuCuoWu(AiPeiZhi peiZhi, String xinXi) {
        try {
            peiZhi.setZuiJinZhuangTai("异常");
            peiZhi.setZuiJinCuoWu(jieQu(xinXi, 300));
            aiPeiZhiRepository.save(peiZhi);
        } catch (Exception e) { /* 状态记录失败不影响主流程 */ }
    }

    private String miaoShuCuoWu(String xinXi) {
        if (xinXi == null || xinXi.isEmpty()) return "未知错误";
        String w = xinXi.trim();
        if (w.contains("401") || w.contains("invalid api key") || w.contains("Incorrect API key")) return "密钥无效（401）";
        if (w.contains("402") || w.contains("insufficient") || w.contains("balance")) return "余额不足（402）";
        if (w.contains("429")) return "请求过于频繁（429）";
        if (w.contains("timeout") || w.contains("Timed out")) return "请求超时";
        return jieQu(w, 120);
    }

    private String jieQu(String wen, int chang) {
        String w = wen == null ? "" : wen.replace("\n", " ").trim();
        return w.length() <= chang ? w : w.substring(0, chang) + "…";
    }
}
