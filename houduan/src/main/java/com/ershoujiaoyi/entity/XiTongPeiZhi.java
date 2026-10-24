// 作者：肖沐樑 QQ：3387432690  完成时间：2026，10，24
package com.ershoujiaoyi.entity;

import jakarta.persistence.*;

/**
 * 系统配置：目前用来保存 AI 助手的形象（头像、名称、一句话介绍）。
 * 整张表只保留一行（id=1），后台可随时更换头像。
 */
@Entity
@Table(name = "xi_tong_peizhi")
public class XiTongPeiZhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** AI 头像图片地址 */
    private String aiTouXiang;

    /** AI 名称，默认「小淘」 */
    private String aiMingCheng;

    /** AI 一句话介绍 */
    private String aiJieShao;

    /** ===== 首页装修（管理员可改，null 时前端用默认文案） ===== */
    /** 首页顶部徽标文字，如「校园闲置 · 安心流转」 */
    private String zxHuiBiao;
    /** 首页主标题第一行（普通文字） */
    private String zxBiaoTi1;
    /** 首页主标题高亮词 */
    private String zxGaoLiang;
    /** 首页主标题第二行 */
    private String zxBiaoTi2;
    /** 首页主标题下的一句描述 */
    private String zxMiaoShu;
    /** 统计一标签（数值为在售商品数，自动统计） */
    private String zxTongJi1BiaoQian;
    /** 统计二标签（数值为商品分类数，自动统计） */
    private String zxTongJi2BiaoQian;
    /** 统计三数值（如 24h） */
    private String zxTongJi3Zhi;
    /** 统计三标签 */
    private String zxTongJi3BiaoQian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAiTouXiang() { return aiTouXiang; }
    public void setAiTouXiang(String aiTouXiang) { this.aiTouXiang = aiTouXiang; }
    public String getAiMingCheng() { return aiMingCheng; }
    public void setAiMingCheng(String aiMingCheng) { this.aiMingCheng = aiMingCheng; }
    public String getAiJieShao() { return aiJieShao; }
    public void setAiJieShao(String aiJieShao) { this.aiJieShao = aiJieShao; }
    public String getZxHuiBiao() { return zxHuiBiao; }
    public void setZxHuiBiao(String zxHuiBiao) { this.zxHuiBiao = zxHuiBiao; }
    public String getZxBiaoTi1() { return zxBiaoTi1; }
    public void setZxBiaoTi1(String zxBiaoTi1) { this.zxBiaoTi1 = zxBiaoTi1; }
    public String getZxGaoLiang() { return zxGaoLiang; }
    public void setZxGaoLiang(String zxGaoLiang) { this.zxGaoLiang = zxGaoLiang; }
    public String getZxBiaoTi2() { return zxBiaoTi2; }
    public void setZxBiaoTi2(String zxBiaoTi2) { this.zxBiaoTi2 = zxBiaoTi2; }
    public String getZxMiaoShu() { return zxMiaoShu; }
    public void setZxMiaoShu(String zxMiaoShu) { this.zxMiaoShu = zxMiaoShu; }
    public String getZxTongJi1BiaoQian() { return zxTongJi1BiaoQian; }
    public void setZxTongJi1BiaoQian(String zxTongJi1BiaoQian) { this.zxTongJi1BiaoQian = zxTongJi1BiaoQian; }
    public String getZxTongJi2BiaoQian() { return zxTongJi2BiaoQian; }
    public void setZxTongJi2BiaoQian(String zxTongJi2BiaoQian) { this.zxTongJi2BiaoQian = zxTongJi2BiaoQian; }
    public String getZxTongJi3Zhi() { return zxTongJi3Zhi; }
    public void setZxTongJi3Zhi(String zxTongJi3Zhi) { this.zxTongJi3Zhi = zxTongJi3Zhi; }
    public String getZxTongJi3BiaoQian() { return zxTongJi3BiaoQian; }
    public void setZxTongJi3BiaoQian(String zxTongJi3BiaoQian) { this.zxTongJi3BiaoQian = zxTongJi3BiaoQian; }
}
