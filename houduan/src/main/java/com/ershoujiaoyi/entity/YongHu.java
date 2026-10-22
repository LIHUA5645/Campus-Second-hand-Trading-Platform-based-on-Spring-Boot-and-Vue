// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "yong_hu")
public class YongHu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String yongHuMing;

    @Column(nullable = false)
    private String miMa;

    private String xingMing;
    private String shouJi;
    private String xueHao;
    private String touXiang;
    private Boolean guanLiYuan = false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date zhuCeShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getYongHuMing() { return yongHuMing; }
    public void setYongHuMing(String yongHuMing) { this.yongHuMing = yongHuMing; }
    public String getMiMa() { return miMa; }
    public void setMiMa(String miMa) { this.miMa = miMa; }
    public String getXingMing() { return xingMing; }
    public void setXingMing(String xingMing) { this.xingMing = xingMing; }
    public String getShouJi() { return shouJi; }
    public void setShouJi(String shouJi) { this.shouJi = shouJi; }
    public String getXueHao() { return xueHao; }
    public void setXueHao(String xueHao) { this.xueHao = xueHao; }
    public String getTouXiang() { return touXiang; }
    public void setTouXiang(String touXiang) { this.touXiang = touXiang; }
    public Boolean getGuanLiYuan() { return guanLiYuan; }
    public void setGuanLiYuan(Boolean guanLiYuan) { this.guanLiYuan = guanLiYuan; }
    public Date getZhuCeShiJian() { return zhuCeShiJian; }
    public void setZhuCeShiJian(Date zhuCeShiJian) { this.zhuCeShiJian = zhuCeShiJian; }
}
