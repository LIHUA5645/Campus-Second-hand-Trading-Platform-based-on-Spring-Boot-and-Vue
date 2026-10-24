// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * AI 接口配置：管理员在后台填写的大模型密钥。
 * 一个平台可以配置多个密钥，调用时按顺序逐个尝试，某个失败自动切换到下一个。
 */
@Entity
@Table(name = "ai_pei_zhi")
public class AiPeiZhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 配置名称，如「DeepSeek 主号」 */
    private String mingCheng;

    /** 服务商：openai / deepseek / qwen / zhipu / moonshot / qita */
    private String fuWuShang;

    /** 接口地址，如 https://api.deepseek.com/v1，程序会自动补 /chat/completions */
    private String diZhi;

    /** 模型名称，如 deepseek-chat、gpt-4o-mini、qwen-vl-max */
    private String moXing;

    /** API 密钥 */
    private String miYao;

    /** 是否支持识图（视觉理解），识图请求优先使用这类配置 */
    private Boolean shiTu;

    /** 状态：0 启用 1 停用 */
    private Integer zhuangTai;

    /** 排序，数字越小越优先 */
    private Integer paiXu;

    private String beiZhu;

    /** 最近连通性：正常 / 异常 */
    private String zuiJinZhuangTai;

    /** 最近一次调用的错误信息 */
    @Column(length = 500)
    private String zuiJinCuoWu;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chuangJianShiJian;

    @Temporal(TemporalType.TIMESTAMP)
    private Date zuiJinShiYongShiJian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMingCheng() { return mingCheng; }
    public void setMingCheng(String mingCheng) { this.mingCheng = mingCheng; }
    public String getFuWuShang() { return fuWuShang; }
    public void setFuWuShang(String fuWuShang) { this.fuWuShang = fuWuShang; }
    public String getDiZhi() { return diZhi; }
    public void setDiZhi(String diZhi) { this.diZhi = diZhi; }
    public String getMoXing() { return moXing; }
    public void setMoXing(String moXing) { this.moXing = moXing; }
    public String getMiYao() { return miYao; }
    public void setMiYao(String miYao) { this.miYao = miYao; }
    public Boolean getShiTu() { return shiTu; }
    public void setShiTu(Boolean shiTu) { this.shiTu = shiTu; }
    public Integer getZhuangTai() { return zhuangTai; }
    public void setZhuangTai(Integer zhuangTai) { this.zhuangTai = zhuangTai; }
    public Integer getPaiXu() { return paiXu; }
    public void setPaiXu(Integer paiXu) { this.paiXu = paiXu; }
    public String getBeiZhu() { return beiZhu; }
    public void setBeiZhu(String beiZhu) { this.beiZhu = beiZhu; }
    public String getZuiJinZhuangTai() { return zuiJinZhuangTai; }
    public void setZuiJinZhuangTai(String zuiJinZhuangTai) { this.zuiJinZhuangTai = zuiJinZhuangTai; }
    public String getZuiJinCuoWu() { return zuiJinCuoWu; }
    public void setZuiJinCuoWu(String zuiJinCuoWu) { this.zuiJinCuoWu = zuiJinCuoWu; }
    public Date getChuangJianShiJian() { return chuangJianShiJian; }
    public void setChuangJianShiJian(Date chuangJianShiJian) { this.chuangJianShiJian = chuangJianShiJian; }
    public Date getZuiJinShiYongShiJian() { return zuiJinShiYongShiJian; }
    public void setZuiJinShiYongShiJian(Date zuiJinShiYongShiJian) { this.zuiJinShiYongShiJian = zuiJinShiYongShiJian; }
}
