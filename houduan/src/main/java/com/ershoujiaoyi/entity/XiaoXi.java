// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 站内私信（买家与商品发布者之间的留言）
 * 一条记录表示一句话，按「双方 + 商品」聚合成一个会话
 */
@Entity
@Table(name = "xiao_xi")
public class XiaoXi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 发送人
    private Long faSongRenId;
    private String faSongRenMing;
    private String faSongRenTouXiang;

    // 接收人
    private Long jieShouRenId;
    private String jieShouRenMing;

    // 会话所关联的商品，便于双方知道在聊哪件闲置
    private Long shangPinId;
    private String shangPinBiaoTi;
    private String shangPinTuPian;

    @Column(length = 1000)
    private String neiRong;

    // 接收人是否已读
    private Boolean yiDu = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date faSongShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFaSongRenId() { return faSongRenId; }
    public void setFaSongRenId(Long faSongRenId) { this.faSongRenId = faSongRenId; }
    public String getFaSongRenMing() { return faSongRenMing; }
    public void setFaSongRenMing(String faSongRenMing) { this.faSongRenMing = faSongRenMing; }
    public String getFaSongRenTouXiang() { return faSongRenTouXiang; }
    public void setFaSongRenTouXiang(String faSongRenTouXiang) { this.faSongRenTouXiang = faSongRenTouXiang; }
    public Long getJieShouRenId() { return jieShouRenId; }
    public void setJieShouRenId(Long jieShouRenId) { this.jieShouRenId = jieShouRenId; }
    public String getJieShouRenMing() { return jieShouRenMing; }
    public void setJieShouRenMing(String jieShouRenMing) { this.jieShouRenMing = jieShouRenMing; }
    public Long getShangPinId() { return shangPinId; }
    public void setShangPinId(Long shangPinId) { this.shangPinId = shangPinId; }
    public String getShangPinBiaoTi() { return shangPinBiaoTi; }
    public void setShangPinBiaoTi(String shangPinBiaoTi) { this.shangPinBiaoTi = shangPinBiaoTi; }
    public String getShangPinTuPian() { return shangPinTuPian; }
    public void setShangPinTuPian(String shangPinTuPian) { this.shangPinTuPian = shangPinTuPian; }
    public String getNeiRong() { return neiRong; }
    public void setNeiRong(String neiRong) { this.neiRong = neiRong; }
    public Boolean getYiDu() { return yiDu; }
    public void setYiDu(Boolean yiDu) { this.yiDu = yiDu; }
    public Date getFaSongShiJian() { return faSongShiJian; }
    public void setFaSongShiJian(Date faSongShiJian) { this.faSongShiJian = faSongShiJian; }
}
