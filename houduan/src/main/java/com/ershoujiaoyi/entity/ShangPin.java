// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shang_pin")
public class ShangPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String biaoTi;
    private String miaoShu;
    private Double jiaGe;
    private String tuPian;
    private Long fenLeiId;
    private Long yongHuId;
    private String yongHuMing;
    // 状态：0在售 1已售 2已下架
    private Integer zhuangTai;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fabuShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBiaoTi() { return biaoTi; }
    public void setBiaoTi(String biaoTi) { this.biaoTi = biaoTi; }
    public String getMiaoShu() { return miaoShu; }
    public void setMiaoShu(String miaoShu) { this.miaoShu = miaoShu; }
    public Double getJiaGe() { return jiaGe; }
    public void setJiaGe(Double jiaGe) { this.jiaGe = jiaGe; }
    public String getTuPian() { return tuPian; }
    public void setTuPian(String tuPian) { this.tuPian = tuPian; }
    public Long getFenLeiId() { return fenLeiId; }
    public void setFenLeiId(Long fenLeiId) { this.fenLeiId = fenLeiId; }
    public Long getYongHuId() { return yongHuId; }
    public void setYongHuId(Long yongHuId) { this.yongHuId = yongHuId; }
    public String getYongHuMing() { return yongHuMing; }
    public void setYongHuMing(String yongHuMing) { this.yongHuMing = yongHuMing; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public Date getFabuShiJian() { return fabuShiJian; }
    public void setFabuShiJian(Date fabuShiJian) { this.fabuShiJian = fabuShiJian; }
}
