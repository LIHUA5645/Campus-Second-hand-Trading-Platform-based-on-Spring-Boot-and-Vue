// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 站内公告：后台发布/编辑/隐藏，首页展示最新几条。
 */
@Entity
@Table(name = "gong_gao")
public class GongGao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 公告标题 */
    private String biaoTi;

    /** 公告内容 */
    @Column(length = 2000)
    private String neiRong;

    /** 状态：0 显示，1 隐藏 */
    private Integer zhuangTai;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chuangJianShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBiaoTi() { return biaoTi; }
    public void setBiaoTi(String biaoTi) { this.biaoTi = biaoTi; }
    public String getNeiRong() { return neiRong; }
    public void setNeiRong(String neiRong) { this.neiRong = neiRong; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public Date getChuangJianShiJian() { return chuangJianShiJian; }
    public void setChuangJianShiJian(Date chuangJianShiJian) { this.chuangJianShiJian = chuangJianShiJian; }
}
