// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.util;

import com.ershoujiaoyi.entity.ZhiFuPeiZhi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * 支付宝对接工具（不依赖官方 SDK，用 JDK 自带的 RSA2 签名实现）。
 * 支持：PC 网页支付表单（alipay.trade.page.pay）、交易查询（alipay.trade.query）、异步通知验签。
 * 沙箱与正式环境只是网关地址不同，由 ZhiFuPeiZhi.shaXiang 决定。
 */
@Component
public class ZhiFuGongJu {

    private static final String SHA_XIANG_WANG_GUAN = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String ZHENG_SHI_WANG_GUAN = "https://openapi.alipay.com/gateway.do";
    private static final ObjectMapper JSON = new ObjectMapper();

    public String wangGuan(ZhiFuPeiZhi peiZhi) {
        boolean shaXiang = peiZhi.getShaXiang() == null || peiZhi.getShaXiang();
        return shaXiang ? SHA_XIANG_WANG_GUAN : ZHENG_SHI_WANG_GUAN;
    }

    /**
     * 生成 PC 网页支付的自动提交表单，前端把这段 HTML 塞进页面即可跳转支付宝
     */
    public String shengChengZhiFuBiaoDan(ZhiFuPeiZhi peiZhi,
                                         String dingDanHao,
                                         String biaoTi,
                                         double jinE,
                                         String fanHuiDiZhi,
                                         String tongZhiDiZhi) throws Exception {
        Map<String, String> gongGong = gongGongCanShu(peiZhi, "alipay.trade.page.pay");
        if (tongZhiDiZhi != null && !tongZhiDiZhi.isEmpty()) {
            gongGong.put("notify_url", tongZhiDiZhi);
        }
        if (fanHuiDiZhi != null && !fanHuiDiZhi.isEmpty()) {
            gongGong.put("return_url", fanHuiDiZhi);
        }
        Map<String, Object> yeWu = new LinkedHashMap<>();
        yeWu.put("out_trade_no", dingDanHao);
        yeWu.put("total_amount", String.format(Locale.US, "%.2f", jinE));
        yeWu.put("subject", biaoTi);
        yeWu.put("product_code", "FAST_INSTANT_TRADE_PAY");
        if (peiZhi.getShouKuanZhangHao() != null && !peiZhi.getShouKuanZhangHao().trim().isEmpty()) {
            yeWu.put("seller_id", peiZhi.getShouKuanZhangHao().trim());
        }
        gongGong.put("biz_content", JSON.writeValueAsString(yeWu));
        String qianMing = qianMing(gongGong, peiZhi.getYingYongSiYao());

        StringBuilder html = new StringBuilder();
        html.append("<form id=\"zhiFuBaoTiJiao\" name=\"zhiFuBaoTiJiao\" action=\"")
                .append(wangGuan(peiZhi)).append("?charset=utf-8\" method=\"POST\">");
        for (Map.Entry<String, String> ruKou : gongGong.entrySet()) {
            html.append("<input type=\"hidden\" name=\"").append(ruKou.getKey())
                    .append("\" value=\"").append(zhuanYiHtml(ruKou.getValue())).append("\"/>");
        }
        html.append("<input type=\"hidden\" name=\"sign\" value=\"").append(zhuanYiHtml(qianMing)).append("\"/>");
        html.append("</form><script>document.forms['zhiFuBaoTiJiao'].submit();</script>");
        return html.toString();
    }

    /**
     * 主动查询交易结果：本地没有公网回调时，靠它确认用户是否真的付款
     * 返回 { chengGong, yiZhiFu, zhuangTai, jiaoYiHao, xinXi }
     */
    public Map<String, Object> chaXun(ZhiFuPeiZhi peiZhi, String dingDanHao) throws Exception {
        Map<String, String> gongGong = gongGongCanShu(peiZhi, "alipay.trade.query");
        Map<String, Object> yeWu = new LinkedHashMap<>();
        yeWu.put("out_trade_no", dingDanHao);
        gongGong.put("biz_content", JSON.writeValueAsString(yeWu));
        gongGong.put("sign", qianMing(gongGong, peiZhi.getYingYongSiYao()));

        String xiangYing = postBiaoDan(wangGuan(peiZhi) + "?charset=utf-8", gongGong);
        Map<String, Object> jieGuo = new HashMap<>();
        jieGuo.put("chengGong", true);
        jieGuo.put("yiZhiFu", false);
        try {
            Map<String, Object> gen = JSON.readValue(xiangYing, Map.class);
            Object xiang = gen.get("alipay_trade_query_response");
            if (xiang instanceof Map) {
                Map<String, Object> ben = (Map<String, Object>) xiang;
                String daiMa = String.valueOf(ben.get("code"));
                jieGuo.put("daiMa", daiMa);
                jieGuo.put("zhuangTai", String.valueOf(ben.get("trade_status")));
                jieGuo.put("jiaoYiHao", String.valueOf(ben.get("trade_no")));
                jieGuo.put("xinXi", String.valueOf(ben.get("sub_msg") == null ? ben.get("msg") : ben.get("sub_msg")));
                boolean yiZhiFu = "10000".equals(daiMa)
                        && ("TRADE_SUCCESS".equals(ben.get("trade_status")) || "TRADE_FINISHED".equals(ben.get("trade_status")));
                jieGuo.put("yiZhiFu", yiZhiFu);
            } else {
                jieGuo.put("xinXi", "支付宝返回异常：" + xiangYing);
            }
        } catch (Exception e) {
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "解析支付查询结果失败：" + e.getMessage());
        }
        return jieGuo;
    }

    /**
     * 校验支付宝异步通知的签名
     */
    public boolean yanQian(Map<String, String> canShu, String zhiFuBaoGongYao) {
        try {
            String qianMing = canShu.get("sign");
            if (qianMing == null || qianMing.isEmpty()) return false;
            Map<String, String> daiYan = new HashMap<>(canShu);
            daiYan.remove("sign");
            daiYan.remove("sign_type");
            Signature yan = Signature.getInstance("SHA256withRSA");
            yan.initVerify(zaiRuGongYao(zhiFuBaoGongYao));
            yan.update(paiXuPinJie(daiYan).getBytes(StandardCharsets.UTF_8));
            return yan.verify(Base64.getDecoder().decode(qianMing));
        } catch (Exception e) {
            return false;
        }
    }

    /** 检查配置是否完整（用于友好提示） */
    public String jianChaPeiZhi(ZhiFuPeiZhi peiZhi) {
        if (peiZhi == null) return "尚未配置支付方式";
        if (peiZhi.getAppId() == null || peiZhi.getAppId().trim().isEmpty()) return "缺少支付宝 APPID";
        if (peiZhi.getYingYongSiYao() == null || peiZhi.getYingYongSiYao().trim().isEmpty()) return "缺少应用私钥";
        if (peiZhi.getZhiFuBaoGongYao() == null || peiZhi.getZhiFuBaoGongYao().trim().isEmpty()) return "缺少支付宝公钥";
        return null;
    }

    // ==================== 内部方法 ====================

    private Map<String, String> gongGongCanShu(ZhiFuPeiZhi peiZhi, String fangFa) {
        Map<String, String> canShu = new HashMap<>();
        canShu.put("app_id", peiZhi.getAppId());
        canShu.put("method", fangFa);
        canShu.put("format", "JSON");
        canShu.put("charset", "utf-8");
        canShu.put("sign_type", "RSA2");
        canShu.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        canShu.put("version", "1.0");
        return canShu;
    }

    /** RSA2 签名：参数按 key 排序后 key=value& 拼接，再做 SHA256withRSA */
    private String qianMing(Map<String, String> canShu, String siYaoBase64) throws Exception {
        Signature qian = Signature.getInstance("SHA256withRSA");
        qian.initSign(zaiRuSiYao(siYaoBase64));
        qian.update(paiXuPinJie(canShu).getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(qian.sign());
    }

    private String paiXuPinJie(Map<String, String> canShu) {
        List<String> jian = new ArrayList<>(canShu.keySet());
        Collections.sort(jian);
        StringBuilder neiRong = new StringBuilder();
        for (int i = 0; i < jian.size(); i++) {
            String zhi = canShu.get(jian.get(i));
            if (zhi == null || zhi.isEmpty()) continue;
            if (neiRong.length() > 0) neiRong.append("&");
            neiRong.append(jian.get(i)).append("=").append(zhi);
        }
        return neiRong.toString();
    }

    private PrivateKey zaiRuSiYao(String base64) throws Exception {
        byte[] ziJie = Base64.getDecoder().decode(quChuHuanHang(base64));
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(ziJie));
    }

    private PublicKey zaiRuGongYao(String base64) throws Exception {
        byte[] ziJie = Base64.getDecoder().decode(quChuHuanHang(base64));
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(ziJie));
    }

    private String quChuHuanHang(String zhi) {
        return zhi == null ? "" : zhi.replaceAll("\\s", "");
    }

    private String zhuanYiHtml(String zhi) {
        if (zhi == null) return "";
        return zhi.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String postBiaoDan(String diZhi, Map<String, String> canShu) throws Exception {
        StringBuilder biaoDan = new StringBuilder();
        for (Map.Entry<String, String> ruKou : canShu.entrySet()) {
            if (biaoDan.length() > 0) biaoDan.append("&");
            biaoDan.append(URLEncoder.encode(ruKou.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(ruKou.getValue(), StandardCharsets.UTF_8));
        }
        HttpURLConnection lianJie = (HttpURLConnection) new URL(diZhi).openConnection();
        lianJie.setRequestMethod("POST");
        lianJie.setDoOutput(true);
        lianJie.setConnectTimeout(15000);
        lianJie.setReadTimeout(30000);
        lianJie.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        try (OutputStream shuChu = lianJie.getOutputStream()) {
            shuChu.write(biaoDan.toString().getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder fanHui = new StringBuilder();
        try (Scanner saoMiao = new Scanner(lianJie.getInputStream(), StandardCharsets.UTF_8)) {
            while (saoMiao.hasNext()) fanHui.append(saoMiao.nextLine());
        }
        return fanHui.toString();
    }
}
