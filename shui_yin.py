# -*- coding: utf-8 -*-
# 作者：添砖 Java  QQ：2047419378
# 完成时间：2026，10，22
# 用途：零宽字符隐形水印工具。
#       把作者信息编码成肉眼不可见的 Unicode 零宽字符（U+200B / U+200C / U+200D），
#       随机分散嵌入到源代码标识符内部。对外人看来是正常代码，
#       但被复制出去运行时会因为零宽字符改变标识符 / 成为非法字符而报错，
#       既能证明归属（隐形签名），又能让“外流代码”无法正常跑起来。
#
# 编码规则：
#   水印头尾标记 MARK = U+200D U+200D U+200D（三个零宽连字符，正常代码不会出现）
#   数据位  0 -> U+200B（零宽空格）
#           1 -> U+200C（零宽非连字符）
#   解码时扫描全文零宽字符，取第一个 MARK 到最后一个 MARK 之间的位流还原。
#
# 命令行：
#   python shui_yin.py jia   <目录或文件>   给代码打隐形水印（不可运行版）
#   python shui_yin.py ti    <目录或文件>   提取并显示水印中的作者信息
#   python shui_yin.py qing  <目录或文件>   清除所有零宽字符，还原成可运行代码
#   目录模式会递归处理常见源码后缀（.java .js .vue .ts .xml .yml .yaml .html .md .json）

import os
import re
import sys
import random

# 零宽字符定义
ZWSP = "\u200b"   # 位 0
ZWNJ = "\u200c"   # 位 1
ZWJ = "\u200d"    # 标记 / 分隔
MARK = ZWJ * 3    # 水印起止标记

# 默认水印载荷：作者信息（软著版权作者）
MO_REN_ZAI_HEI = "添砖 Java QQ：2047419378"

# 需要处理的源码后缀
YUAN_MING_HOU_ZHUI = (".java", ".js", ".vue", ".ts", ".tsx",
                      ".xml", ".yml", ".yaml", ".html", ".htm",
                      ".md", ".json", ".css", ".py")

# 标识符正则（Java / JS / Vue 通用）
BIAO_SHI_FU = re.compile(r"[A-Za-z_$][A-Za-z0-9_$]*")


def jia_mi(xin_xi):
    """把字符串编码成零宽字符水印（含头尾标记）。"""
    shu_ju = xin_xi.encode("utf-8")
    wei = []
    for zi_jie in shu_ju:
        for i in range(7, -1, -1):
            wei.append(0 if ((zi_jie >> i) & 1) == 0 else 1)
    wei_chuan = "".join(ZWSP if b == 0 else ZWNJ for b in wei)
    return MARK + wei_chuan + MARK


def ti_qu_wei(wei_shui_yin):
    """从可能含零宽字符的文本里提取并解码作者信息，失败返回 None。"""
    # 收集所有零宽字符，保持原始顺序
    shou_ji = [c for c in wei_shui_yin if c in (ZWSP, ZWNJ, ZWJ)]
    if len(shou_ji) < len(MARK) * 2:
        return None
    s = "".join(shou_ji)
    shou = s.find(MARK)
    wei = s.rfind(MARK)
    if shou == -1 or wei == -1 or wei <= shou:
        return None
    zhong_jian = s[shou + len(MARK):wei]
    wei_lie = []
    for c in zhong_jian:
        if c == ZWSP:
            wei_lie.append(0)
        elif c == ZWNJ:
            wei_lie.append(1)
        else:
            # 标记碎片等忽略
            continue
    if len(wei_lie) % 8 != 0:
        return None
    zi_jie = bytearray()
    for i in range(0, len(wei_lie), 8):
        zhi = 0
        for b in wei_lie[i:i + 8]:
            zhi = (zhi << 1) | b
        zi_jie.append(zhi)
    try:
        return zi_jie.decode("utf-8")
    except Exception:
        return None


def qing_li(wei_shui_yin):
    """清除所有零宽字符，还原成可运行代码。"""
    return "".join(c for c in wei_shui_yin if c not in (ZWSP, ZWNJ, ZWJ))


def _fen_duan(s, k):
    """把字符串恰好均分成 k 段（前 rem 段多 1 个字符），保证段数恒为 k。"""
    if k <= 1:
        return [s]
    n = len(s)
    ji_chu = n // k
    yu = n % k
    jie_guo = []
    i = 0
    for j in range(k):
        chang = ji_chu + (1 if j < yu else 0)
        jie_guo.append(s[i:i + chang])
        i += chang
    return jie_guo


def _zai_zi_fu_chuan(hang, wei_zhi):
    """判断 hang 中 wei_zhi 位置是否处于字符串（' " `）或单行注释（//）内。
    处于其中的零宽字符会被当成普通字符而不报错，故水印必须避开这些位置。"""
    in_s = in_d = in_b = in_c = False
    i = 0
    n = len(hang)
    while i < wei_zhi and i < n:
        c = hang[i]
        nx = hang[i + 1] if i + 1 < n else ""
        if in_c:
            i += 1
            continue
        if in_s or in_d or in_b:
            if c == "\\":
                i += 2
                continue
            if (in_s and c == "'") or (in_d and c == '"') or (in_b and c == "`"):
                in_s = in_d = in_b = False
            i += 1
            continue
        if c == "'":
            in_s = True
        elif c == '"':
            in_d = True
        elif c == "`":
            in_b = True
        elif c == "/" and nx == "/":
            in_c = True
        i += 1
    return in_s or in_d or in_b or in_c


def _an_quan_biao_shi_fu(hang):
    """返回该行中处于字符串/注释之外的标识符匹配列表。"""
    return [m for m in BIAO_SHI_FU.finditer(hang)
            if not _zai_zi_fu_chuan(hang, m.start())]


def _cha_ru_biao_shi_fu(hang, fen_duan, rng):
    """在某一行里挑一个标识符把水印片段插到它内部。
    优先挑【非字符串/注释内】的标识符（插进去必报 SyntaxError / 编译错误）；
    若该行没有安全标识符（如 JSON 里只有字符串值），则回退到任意标识符，
    仍能携带水印证明归属（JSON/ZIP 等不会因此解析失败）。"""
    an_quan = _an_quan_biao_shi_fu(hang)
    chi = an_quan if an_quan else list(BIAO_SHI_FU.finditer(hang))
    if not chi:
        return hang, False
    m = rng.choice(chi)
    # 插在标识符首字符之后到末尾之间
    wei_zhi = rng.randint(m.start() + 1, m.end())
    return hang[:wei_zhi] + fen_duan + hang[wei_zhi:], True


def qian_ru(dai_ma, xin_xi, zhong_zi=None):
    """把作者信息作为隐形水印随机分散嵌入代码各行，返回带水印的代码。
    随机挑选若干行嵌入，但【保持行号升序】，保证全文零宽字符的全局顺序
    与编码顺序一致，从而可被 ti_qu_wei 完整还原；片段插在真实代码标识符内部，
    改变标识符 / 成为非法字符，外流代码将无法正常编译运行。
    关键：只插在字符串/注释之外的标识符上，确保一定会被解析器报错。"""
    wm = jia_mi(xin_xi)
    tou = MARK
    wei = MARK
    zhong = wm[len(MARK):-len(MARK)]
    hang_lie = dai_ma.split("\n")
    # 候选行：优先“存在非字符串/注释内标识符且像真实代码”的行（保证报错）；
    # 若整篇都没有（如纯 JSON），则回退到任意含标识符的行（仍能携带水印）。
    hou_xuan = [i for i, h in enumerate(hang_lie)
                if _an_quan_biao_shi_fu(h) and any(t in h for t in ";={}()")]
    if not hou_xuan:
        hou_xuan = [i for i, h in enumerate(hang_lie) if _an_quan_biao_shi_fu(h)]
    if not hou_xuan:
        hou_xuan = [i for i, h in enumerate(hang_lie) if BIAO_SHI_FU.search(h)]
    if not hou_xuan:
        return dai_ma
    rng = random.Random(zhong_zi)
    # 随机挑选行，但保持行号升序（关键：保证全局顺序可还原）
    shu_liang = min(len(hou_xuan), 40)
    tiao_xuan = rng.sample(hou_xuan, shu_liang)
    tiao_xuan.sort()
    k = len(tiao_xuan)
    duan = _fen_duan(zhong, k)
    # 构造每段（首段带前置 MARK，末段带后置 MARK；k==1 时两端都带）
    fen_duan_lie = []
    for idx, p in enumerate(duan):
        if k == 1:
            fen_duan_lie.append(tou + p + wei)
        elif idx == 0:
            fen_duan_lie.append(tou + p)
        elif idx == k - 1:
            fen_duan_lie.append(p + wei)
        else:
            fen_duan_lie.append(p)
    # 按行号升序塞进挑选出的行（顺序固定，可还原；每行必含安全标识符，不会丢段）
    for i, hang_idx in enumerate(tiao_xuan):
        hang_lie[hang_idx], _ = _cha_ru_biao_shi_fu(hang_lie[hang_idx], fen_duan_lie[i], rng)
    return "\n".join(hang_lie)


def _shi_wen_jian_lu_jing(lu_jing):
    if os.path.isfile(lu_jing):
        return lu_jing.endswith(YUAN_MING_HOU_ZHUI)
    return False


def _bian_li(mu_lu):
    lie_biao = []
    for gen, _, wen_jian in os.walk(mu_lu):
        for f in wen_jian:
            lp = os.path.join(gen, f)
            if _shi_wen_jian_lu_jing(lp):
                lie_biao.append(lp)
    return lie_biao


def _chu_li(mu_biao, mo_shi, xin_xi=MO_REN_ZAI_HEI, zhong_zi=None):
    wen_jian_lie = []
    if os.path.isfile(mu_biao):
        wen_jian_lie = [mu_biao]
    else:
        wen_jian_lie = _bian_li(mu_biao)
    for lp in wen_jian_lie:
        with open(lp, "r", encoding="utf-8") as f:
            yuan = f.read()
        if mo_shi == "jia":
            xin = qian_ru(yuan, xin_xi, zhong_zi=(zhong_zi or lp))
            with open(lp, "w", encoding="utf-8") as f:
                f.write(xin)
            print("已打水印：" + lp)
        elif mo_shi == "ti":
            jie = ti_qu_wei(yuan)
            print(lp + " => " + (jie if jie else "（未发现水印）"))
        elif mo_shi == "qing":
            xin = qing_li(yuan)
            with open(lp, "w", encoding="utf-8") as f:
                f.write(xin)
            print("已清除水印：" + lp)


def zhu():
    if len(sys.argv) < 3:
        print("用法：python shui_yin.py [jia|ti|qing] <目录或文件> [水印信息]")
        return
    mo_shi = sys.argv[1]
    mu_biao = sys.argv[2]
    xin_xi = sys.argv[3] if len(sys.argv) > 3 else MO_REN_ZAI_HEI
    _chu_li(mu_biao, mo_shi, xin_xi)


if __name__ == "__main__":
    zhu()
