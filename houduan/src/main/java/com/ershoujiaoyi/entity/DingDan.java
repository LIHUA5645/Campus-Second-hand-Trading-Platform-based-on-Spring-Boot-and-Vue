// 作者：肖沐樑 QQ：3387432690
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
    public Date getXiaDanShiJian() { return xiaDanShiJian; }
    public void setXiaDanShiJian(Date xiaDanShiJian) { this.xiaDanShiJian = xiaDanShiJian; }
}
