// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * 支付配置：管理员在后台填写的支付宝（沙箱/正式）参数。
 * 应用私钥与支付宝公钥落库前经 AES 加密，返回后台时打码展示。
 */
@Entity
@Table(name = "zhi_fu_pei_zhi")
public class ZhiFuPeiZhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 配置名称，如「支付宝沙箱」 */
    private String mingCheng;

    /** 支付宝开放平台的应用 APPID */
    private String appId;

    /** 应用私钥（PKCS8，RSA2），加密存储 */
    @Column(length = 2000)
    private String yingYongSiYao;

    /** 支付宝公钥，用于校验回调签名 */
    @Column(length = 2000)
    private String zhiFuBaoGongYao;

    /** 收款账号 seller_id，留空表示使用 APPID 对应账号 */
    private String shouKuanZhangHao;

    /** 是否沙箱环境（开放平台沙箱应用填 true） */
    private Boolean shaXiang = true;

    /** 是否启用：0 停用 1 启用 */
    private Integer zhuangTai;

    /** 支付成功后前端跳转地址，留空则由请求方传入 */
    private String fanHuiDiZhi;

    /** 异步通知地址，需公网可访问，留空则靠主动查询确认支付结果 */
    private String tongZhiDiZhi;

    private String beiZhu;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chuangJianShiJian;

    @Temporal(TemporalType.TIMESTAMP)
    private Date gengXinShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMingCheng() { return mingCheng; }
    public void setMingCheng(String mingCheng) { this.mingCheng = mingCheng; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getYingYongSiYao() { return yingYongSiYao; }
    public void setYingYongSiYao(String yingYongSiYao) { this.yingYongSiYao = yingYongSiYao; }
    public String getZhiFuBaoGongYao() { return zhiFuBaoGongYao; }
    public void setZhiFuBaoGongYao(String zhiFuBaoGongYao) { this.zhiFuBaoGongYao = zhiFuBaoGongYao; }
    public String getShouKuanZhangHao() { return shouKuanZhangHao; }
    public void setShouKuanZhangHao(String shouKuanZhangHao) { this.shouKuanZhangHao = shouKuanZhangHao; }
    public Boolean getShaXiang() { return shaXiang; }
    public void setShaXiang(Boolean shaXiang) { this.shaXiang = shaXiang; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public String getFanHuiDiZhi() { return fanHuiDiZhi; }
    public void setFanHuiDiZhi(String fanHuiDiZhi) { this.fanHuiDiZhi = fanHuiDiZhi; }
    public String getTongZhiDiZhi() { return tongZhiDiZhi; }
    public void setTongZhiDiZhi(String tongZhiDiZhi) { this.tongZhiDiZhi = tongZhiDiZhi; }
    public String getBeiZhu() { return beiZhu; }
    public void setBeiZhu(String beiZhu) { this.beiZhu = beiZhu; }
    public Date getChuangJianShiJian() { return chuangJianShiJian; }
    public void setChuangJianShiJian(Date chuangJianShiJian) { this.chuangJianShiJian = chuangJianShiJian; }
    public Date getGengXinShiJian() { return gengXinShiJian; }
    public void setGengXinShiJian(Date gengXinShiJian) { this.gengXinShiJian = gengXinShiJian; }
}
