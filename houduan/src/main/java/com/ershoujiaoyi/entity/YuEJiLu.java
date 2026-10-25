// 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * 钱包流水：记录每一笔余额变动，便于用户与管理员对账。
 * 类型 leiXing：shouru 卖货收入 / tixian 提现扣款 / bohui 提现驳回退回 / tuihui 订单退款
 */
@Entity
@Table(name = "yu_e_ji_lu")
public class YuEJiLu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long yongHuId;
    private String yongHuMing;

    /** 变动类型：shouru / tixian / bohui / tuihui */
    private String leiXing;

    /** 变动金额，正数为收入，负数为支出 */
    private Double jinE;

    /** 变动后的余额 */
    private Double yuE;

    /** 关联订单（收入与退款时有值） */
    private Long dingDanId;

    /** 关联商品（收入时有值） */
    private Long shangPinId;

    @Column(length = 500)
    private String shuoMing;

    @Temporal(TemporalType.TIMESTAMP)
    private Date shiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getYongHuId() { return yongHuId; }
    public void setYongHuId(Long yongHuId) { this.yongHuId = yongHuId; }
    public String getYongHuMing() { return yongHuMing; }
    public void setYongHuMing(String yongHuMing) { this.yongHuMing = yongHuMing; }
    public String getLeiXing() { return leiXing; }
    public void setLeiXing(String leiXing) { this.leiXing = leiXing; }
    public Double getJinE() { return jinE; }
    public void setJinE(Double jinE) { this.jinE = jinE; }
    public Double getYuE() { return yuE; }
    public void setYuE(Double yuE) { this.yuE = yuE; }
    public Long getDingDanId() { return dingDanId; }
    public void setDingDanId(Long dingDanId) { this.dingDanId = dingDanId; }
    public Long getShangPinId() { return shangPinId; }
    public void setShangPinId(Long shangPinId) { this.shangPinId = shangPinId; }
    public String getShuoMing() { return shuoMing; }
    public void setShuoMing(String shuoMing) { this.shuoMing = shuoMing; }
    public Date getShiJian() { return shiJian; }
    public void setShiJian(Date shiJian) { this.shiJian = shiJian; }
}
