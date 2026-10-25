// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ding_dan")
public class DingDan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shangPinId;
    private String shangPinBiaoTi;
    private Double jiaGe;
    private String tuPian;
    private Long maiJiaId;
    private String maiJiaMing;
    private String maiJiaShouJi;
    private String diZhi;
    private String liuYan;
    // 状态：0待发货 1待收货 2已完成 3已取消
    private Integer zhuangTai;

    // 支付：0 未付款 1 已付款（历史订单为 null，按原状态展示）
    private Integer zhiFuZhuangTai;
    // 支付方式：alipay 支付宝 / huodao 货到付款（线下当面交易）
    private String zhiFuFangShi;
    // 支付宝交易号
    private String jiaoYiHao;
    @Temporal(TemporalType.TIMESTAMP)
    private Date zhiFuShiJian;

    @Temporal(TemporalType.TIMESTAMP)
    private Date xiaDanShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getShangPinId() { return shangPinId; }
    public void setShangPinId(Long shangPinId) { this.shangPinId = shangPinId; }
    public String getShangPinBiaoTi() { return shangPinBiaoTi; }
    public void setShangPinBiaoTi(String shangPinBiaoTi) { this.shangPinBiaoTi = shangPinBiaoTi; }
    public Double getJiaGe() { return jiaGe; }
    public void setJiaGe(Double jiaGe) { this.jiaGe = jiaGe; }
    public String getTuPian() { return tuPian; }
    public void setTuPian(String tuPian) { this.tuPian = tuPian; }
    public Long getMaiJiaId() { return maiJiaId; }
    public void setMaiJiaId(Long maiJiaId) { this.maiJiaId = maiJiaId; }
    public String getMaiJiaMing() { return maiJiaMing; }
    public void setMaiJiaMing(String maiJiaMing) { this.maiJiaMing = maiJiaMing; }
    public String getMaiJiaShouJi() { return maiJiaShouJi; }
    public void setMaiJiaShouJi(String maiJiaShouJi) { this.maiJiaShouJi = maiJiaShouJi; }
    public String getDiZhi() { return diZhi; }
    public void setDiZhi(String diZhi) { this.diZhi = diZhi; }
    public String getLiuYan() { return liuYan; }
    public void setLiuYan(String liuYan) { this.liuYan = liuYan; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public Integer getZhiFuZhuangTai() { return zhiFuZhuangTai; }
    public void setZhiFuZhuangTai(Integer zhiFuZhuangTai) { this.zhiFuZhuangTai = zhiFuZhuangTai; }
    public String getZhiFuFangShi() { return zhiFuFangShi; }
    public void setZhiFuFangShi(String zhiFuFangShi) { this.zhiFuFangShi = zhiFuFangShi; }
    public String getJiaoYiHao() { return jiaoYiHao; }
    public void setJiaoYiHao(String jiaoYiHao) { this.jiaoYiHao = jiaoYiHao; }
    public Date getZhiFuShiJian() { return zhiFuShiJian; }
    public void setZhiFuShiJian(Date zhiFuShiJian) { this.zhiFuShiJian = zhiFuShiJian; }
    public Date getXiaDanShiJian() { return xiaDanShiJian; }
    public void setXiaDanShiJian(Date xiaDanShiJian) { this.xiaDanShiJian = xiaDanShiJian; }
}
