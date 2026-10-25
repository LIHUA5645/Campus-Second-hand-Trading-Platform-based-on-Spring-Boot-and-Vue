// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * 提现申请：卖家把钱包余额提出来，管理员在后台审核并线下打款。
 * 状态 zhuangTai：0 待审核 / 1 已打款 / 2 已驳回
 */
@Entity
@Table(name = "ti_xian")
public class TiXian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long yongHuId;
    private String yongHuMing;

    /** 提现金额 */
    private Double jinE;

    /** 收款方式：alipay 支付宝 / weixin 微信 / yinhang 银行卡 */
    private String fangShi;

    /** 收款账号（支付宝账号、微信号或银行卡号） */
    private String zhangHao;

    /** 收款人真实姓名 */
    private String xingMing;

    /** 联系电话，便于管理员核对 */
    private String shouJi;

    /** 0 待审核 / 1 已打款 / 2 已驳回 */
    private Integer zhuangTai = 0;

    /** 管理员备注（驳回原因或打款说明） */
    @Column(length = 500)
    private String beiZhu;

    /** 处理人（管理员名） */
    private String chuLiRen;

    @Temporal(TemporalType.TIMESTAMP)
    private Date shenQingShiJian;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chuLiShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getYongHuId() { return yongHuId; }
    public void setYongHuId(Long yongHuId) { this.yongHuId = yongHuId; }
    public String getYongHuMing() { return yongHuMing; }
    public void setYongHuMing(String yongHuMing) { this.yongHuMing = yongHuMing; }
    public Double getJinE() { return jinE; }
    public void setJinE(Double jinE) { this.jinE = jinE; }
    public String getFangShi() { return fangShi; }
    public void setFangShi(String fangShi) { this.fangShi = fangShi; }
    public String getZhangHao() { return zhangHao; }
    public void setZhangHao(String zhangHao) { this.zhangHao = zhangHao; }
    public String getXingMing() { return xingMing; }
    public void setXingMing(String xingMing) { this.xingMing = xingMing; }
    public String getShouJi() { return shouJi; }
    public void setShouJi(String shouJi) { this.shouJi = shouJi; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public String getBeiZhu() { return beiZhu; }
    public void setBeiZhu(String beiZhu) { this.beiZhu = beiZhu; }
    public String getChuLiRen() { return chuLiRen; }
    public void setChuLiRen(String chuLiRen) { this.chuLiRen = chuLiRen; }
    public Date getShenQingShiJian() { return shenQingShiJian; }
    public void setShenQingShiJian(Date shenQingShiJian) { this.shenQingShiJian = shenQingShiJian; }
    public Date getChuLiShiJian() { return chuLiShiJian; }
    public void setChuLiShiJian(Date chuLiShiJian) { this.chuLiShiJian = chuLiShiJian; }
}
