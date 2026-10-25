// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 轮播图广告：后台可增删改、排序、启停，首页按排序展示启用的轮播项。
 */
@Entity
@Table(name = "lun_bo_tu")
public class LunBoTu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 主标题 */
    private String biaoTi;

    /** 副标题 / 描述 */
    private String fuBiaoTi;

    /** 图片地址（可为站内路径 /images/xxx.jpg 或外链） */
    private String tuPian;

    /** 点击跳转地址，可空 */
    private String lianJie;

    /** 排序值，越小越靠前 */
    private Integer paiXu;

    /** 状态：0 启用，1 停用 */
    private Integer zhuangTai;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chuangJianShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBiaoTi() { return biaoTi; }
    public void setBiaoTi(String biaoTi) { this.biaoTi = biaoTi; }
    public String getFuBiaoTi() { return fuBiaoTi; }
    public void setFuBiaoTi(String fuBiaoTi) { this.fuBiaoTi = fuBiaoTi; }
    public String getTuPian() { return tuPian; }
    public void setTuPian(String tuPian) { this.tuPian = tuPian; }
    public String getLianJie() { return lianJie; }
    public void setLianJie(String lianJie) { this.lianJie = lianJie; }
    public Integer getPaiXu() { return paiXu; }
    public void setPaiXu(Integer paiXu) { this.paiXu = paiXu; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public Date getChuangJianShiJian() { return chuangJianShiJian; }
    public void setChuangJianShiJian(Date chuangJianShiJian) { this.chuangJianShiJian = chuangJianShiJian; }
}
