<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div class="shouYe">
    <section class="yingXiong">
      <div class="heroDots"></div>
      <div class="rongQi yingXiongNei">
        <div class="yingXiongZuo">
          <span class="heroBadge">校园闲置 · 安心流转</span>
          <h1>让校园里的<span class="zhuTiWenZiBai">闲置</span><br />流动起来</h1>
          <p>教材、数码、好物低价流转，学长学姐的宝贝等你来淘。</p>
          <div class="yingXiongSouSuo">
            <el-input v-model="guanJianCi" size="large" placeholder="想淘点什么？" @keyup.enter="chaXun">
              <template #prefix><el-icon><Search /></el-icon></template>
              <template #append><el-button type="primary" size="large" @click="chaXun">搜一搜</el-button></template>
            </el-input>
          </div>
          <div class="shuJu">
            <div><b>{{ zongShu || '—' }}</b><span>在售好物</span></div>
            <div><b>{{ fenLeiLieBiao.length || '—' }}</b><span>商品分类</span></div>
            <div><b>24h</b><span>极速交易</span></div>
          </div>
        </div>
        <div class="yingXiongYou">
          <el-carousel ref="lunBoRef" v-if="lunBoLie.length" height="304px" :interval="4200" indicator-position="none" arrow="hover" class="lunBo" @change="(i) => dangQianLunBo = i">
            <el-carousel-item v-for="(lb, idx) in lunBoLie" :key="lb.id">
              <div class="lunBoXiang" @click="tiaoZhuan(lb.lianJie)">
                <img :src="lb.tuPian" :alt="lb.biaoTi || '轮播图'" />
                <div class="lunBoTian"></div>
                <div class="lunBoZheZhao"></div>
                <span class="lunBoSu">{{ String(idx + 1).padStart(2, '0') }} / {{ String(lunBoLie.length).padStart(2, '0') }}</span>
                <div class="lunBoKaPian" v-if="lb.biaoTi || lb.fuBiaoTi">
                  <strong v-if="lb.biaoTi">{{ lb.biaoTi }}</strong>
                  <span v-if="lb.fuBiaoTi" class="lunBoFu">{{ lb.fuBiaoTi }}</span>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
          <div v-if="lunBoLie.length" class="lunBoZhiShiQi">
            <span v-for="(lb, i) in lunBoLie" :key="lb.id" :class="['tiao', dangQianLunBo === i ? 'xuan' : '']" @click="tiaoLunBo(i)"></span>
          </div>
          <template v-else>
            <div class="huan1"></div>
            <div class="huan2"></div>
            <div class="huan3"></div>
            <div class="heroCard">
              <el-icon class="yingXiongTuBiao"><ShoppingBag /></el-icon>
            </div>
            <span class="fuBiao fuBiao1">教材 9 成新</span>
            <span class="fuBiao fuBiao2">数码好价</span>
            <span class="fuBiao fuBiao3">宿舍好物</span>
          </template>
        </div>
      </div>
    </section>

    <div class="rongQi">
      <div class="gongGaoTiao" v-if="gongGaoLie.length">
        <el-icon class="gongGaoTuBiao"><Bell /></el-icon>
        <span class="gongGaoBiaoQian">公告</span>
        <el-carousel height="26px" direction="vertical" :interval="4500" indicator-position="none" class="gongGaoLunBo">
          <el-carousel-item v-for="gg in gongGaoLie" :key="gg.id">
            <span class="gongGaoWenZi"><b>{{ gg.biaoTi }}</b><em>{{ gg.neiRong }}</em></span>
          </el-carousel-item>
        </el-carousel>
      </div>

      <section class="fenLeiQu">
        <div class="quTou">
          <h2 class="kuaiBiaoTi"><el-icon><Menu /></el-icon> 全部分类</h2>
          <span class="quFuZhu">挑一个类目，快速找到心仪好物</span>
        </div>
        <div class="fenLeiWangGe">
          <div :class="['fenLeiKaPian', xuanZhongFenLei === null ? 'xuanZhong' : '']" @click="xuanFenLei(null)">
            <el-icon class="fenLeiTuBiao"><Grid /></el-icon><span>全部</span>
          </div>
          <div v-for="f in fenLeiLieBiao" :key="f.id" :class="['fenLeiKaPian', xuanZhongFenLei === f.id ? 'xuanZhong' : '']" @click="xuanFenLei(f.id)">
            <el-icon class="fenLeiTuBiao"><Goods /></el-icon><span>{{ f.mingCheng }}</span>
          </div>
        </div>
      </section>

      <section class="shangPinQu">
        <div class="shangPinTou">
          <div class="shangPinTouWen">
            <h2 class="kuaiBiaoTi"><el-icon><Goods /></el-icon> 好物市场</h2>
            <p class="shangPinFuZhu">每天都有同学发布新的闲置，先到先得～</p>
          </div>
          <el-button v-if="dangQianYongHu" type="primary" round @click="luYou.push('/fabu')"><el-icon><Plus /></el-icon> 发布闲置</el-button>
        </div>

        <div v-if="shangPinLieBiao.length === 0" class="kongZhuangTai">
          <el-empty description="暂无在售商品，快来发布第一个吧～" />
        </div>
        <div v-else class="wangGe">
          <div v-for="sp in shangPinLieBiao" :key="sp.id" class="shangPinKaPian" @click="luYou.push('/shangpin/' + sp.id)">
            <div class="shangPinTuPian">
              <img v-if="sp.tuPian" :src="sp.tuPian" alt="商品图" />
              <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
              <span class="jiaGeBiaoQian">￥{{ sp.jiaGe }}</span>
            </div>
            <div class="shangPinXinXi">
              <div class="shangPinBiaoTi">{{ sp.biaoTi }}</div>
              <div class="shangPinDiBu">
                <span class="faBuZhe"><el-icon><User /></el-icon> {{ sp.yongHuMing || '匿名' }}</span>
                <span class="jiaGe">￥{{ sp.jiaGe }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="fenYe" v-if="zongShu > meiYe">
          <el-pagination layout="prev, pager, next" :total="zongShu" :page-size="meiYe" :current-page="yeMa + 1" @current-change="fanYe" />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'

const luYou = useRouter()
const luYouXin = useRoute()
const dangQianYongHu = ref(null)
const lunBoLie = ref([])
const gongGaoLie = ref([])
const lunBoRef = ref(null)
const dangQianLunBo = ref(0)
const tiaoLunBo = (i) => { if (lunBoRef.value) lunBoRef.value.setActiveItem(i) }
const guanJianCi = ref('')
const fenLeiLieBiao = ref([])
const xuanZhongFenLei = ref(null)
const shangPinLieBiao = ref([])
const zongShu = ref(0)
const yeMa = ref(0)
const meiYe = 12

const huoQuYongHu = () => {
  const y = localStorage.getItem('yongHu')
  dangQianYongHu.value = y ? JSON.parse(y) : null
}

const jiaZaiFenLei = async () => {
  const res = await jieKou.fenLeiLieBiao()
  if (res.data.chengGong) fenLeiLieBiao.value = res.data.lieBiao
}

const jiaZaiZhanDian = async () => {
  try {
    const [lb, gg] = await Promise.all([jieKou.zhanDianLunBo(), jieKou.zhanDianGongGao()])
    if (lb.data.chengGong) lunBoLie.value = lb.data.lieBiao || []
    if (gg.data.chengGong) gongGaoLie.value = gg.data.lieBiao || []
  } catch (e) {
    // 首页装饰数据加载失败不影响主流程
  }
}

const tiaoZhuan = (lianJie) => {
  if (!lianJie) return
  if (/^https?:/i.test(lianJie)) window.open(lianJie, '_blank')
  else luYou.push(lianJie)
}

const jiaZaiShangPin = async () => {
  const res = await jieKou.liebiao({
    guanJianCi: guanJianCi.value,
    fenLeiId: xuanZhongFenLei.value,
    yeMa: yeMa.value,
    meiYe: meiYe
  })
  if (res.data.chengGong) {
    shangPinLieBiao.value = res.data.lieBiao
    zongShu.value = res.data.zongShu
  }
}

const chaXun = () => { yeMa.value = 0; jiaZaiShangPin() }
const xuanFenLei = (id) => { xuanZhongFenLei.value = id; yeMa.value = 0; jiaZaiShangPin() }
const fanYe = (p) => { yeMa.value = p - 1; jiaZaiShangPin() }

watch(() => luYouXin.query.kw, (kw) => { guanJianCi.value = kw || ''; yeMa.value = 0; jiaZaiShangPin() })

onMounted(() => {
  huoQuYongHu()
  jiaZaiFenLei()
  jiaZaiZhanDian()
  guanJianCi.value = luYouXin.query.kw || ''
  jiaZaiShangPin()
})
</script>

<style scoped>
.shouYe { padding-bottom: 40px; }

/* ===== 首页滚动公告 ===== */
.gongGaoTiao {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
  padding: 8px 18px;
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: 999px;
  box-shadow: var(--banYing);
}
.gongGaoTuBiao { color: var(--zhuSe); font-size: 16px; }
.gongGaoBiaoQian {
  flex-shrink: 0;
  font-size: 12.5px;
  font-weight: 700;
  color: var(--zhuSe);
  padding-right: 10px;
  border-right: 1px solid var(--bianKuang);
}
.gongGaoLunBo { flex: 1; height: 26px; }
.gongGaoLunBo :deep(.el-carousel__container) { height: 26px; }
.gongGaoWenZi {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 26px;
  font-size: 13px;
  color: var(--wenZiEr);
  overflow: hidden;
}
.gongGaoWenZi b { flex-shrink: 0; color: var(--wenZi); }
.gongGaoWenZi em {
  font-style: normal;
  color: var(--wenZiSan);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* ===== Hero：深墨夜色 + 暖光晕 ===== */
.yingXiong {
  position: relative;
  overflow: hidden;
  color: #fff;
  border-radius: 0 0 28px 28px;
  background:
    radial-gradient(760px 400px at 90% 6%, rgba(255, 168, 76, .22) 0%, rgba(255, 168, 76, 0) 62%),
    radial-gradient(700px 440px at 2% 98%, rgba(20, 184, 166, .20) 0%, rgba(20, 184, 166, 0) 60%),
    linear-gradient(135deg, #0b1117 0%, #131b24 48%, #0c1520 100%);
}
.heroDots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, .5) 1px, transparent 1px);
  background-size: 22px 22px;
  opacity: .12;
  pointer-events: none;
}
.yingXiongNei {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 64px 24px 60px;
  gap: 40px;
}
.yingXiongZuo { max-width: 560px; }
.heroBadge {
  display: inline-block;
  padding: 5px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, .10);
  border: 1px solid rgba(255, 255, 255, .20);
  font-size: 13px;
  letter-spacing: .5px;
  margin-bottom: 18px;
}
.yingXiongZuo h1 {
  font-size: 44px;
  line-height: 1.22;
  margin: 0 0 16px;
  font-weight: 800;
  letter-spacing: .5px;
}
.yingXiongZuo p { font-size: 16px; color: rgba(255, 255, 255, .78); margin: 0 0 26px; }
.zhuTiWenZiBai { color: #fff; text-shadow: 0 2px 18px rgba(255, 196, 120, .35); }

.yingXiongSouSuo { max-width: 480px; }
.yingXiongSouSuo :deep(.el-input__wrapper) {
  border-radius: 999px;
  padding-left: 18px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, .35);
}
.yingXiongSouSuo :deep(.el-input-group__append) {
  background: transparent;
  box-shadow: none;
  padding: 0;
  border: none;
}
.yingXiongSouSuo :deep(.el-input-group__append) .el-button {
  border-radius: 999px;
  margin-left: 6px;
  height: 40px;
  padding: 0 22px;
}

.shuJu { display: flex; gap: 40px; margin-top: 32px; }
.shuJu div { display: flex; flex-direction: column; }
.shuJu b { font-size: 28px; font-weight: 800; }
.shuJu span { font-size: 13px; color: rgba(255, 255, 255, .62); margin-top: 3px; }

/* Hero 右侧插画 / 轮播图（16:9，与素材原比例一致，不裁切） */
.yingXiongYou {
  position: relative;
  width: 540px;
  height: 304px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.lunBo {
  width: 100%;
  height: 304px;
  border-radius: 20px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, .16);
  box-shadow: 0 30px 64px rgba(0, 0, 0, .55);
  background: #fff;
}
.lunBo :deep(.el-carousel__container) { height: 304px; }
.lunBo :deep(.el-carousel__arrow) {
  width: 36px;
  height: 36px;
  background: rgba(10, 14, 20, .42);
  border-radius: 50%;
  backdrop-filter: blur(4px);
}
.lunBo :deep(.el-carousel__arrow:hover) { background: rgba(10, 14, 20, .70); }
.lunBo :deep(.el-carousel__arrow) .el-icon { font-size: 16px; }

.lunBoXiang { position: relative; width: 100%; height: 100%; cursor: pointer; overflow: hidden; background: #fff; }
.lunBoXiang img { width: 100%; height: 100%; object-fit: cover; object-position: center; display: block; transition: transform .6s ease; }
.lunBoXiang:hover img { transform: scale(1.03); }

/* 叠加层默认透明，鼠标悬停才出现，避免与图片自带文案打架 */
.lunBoTian {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, .18) 0%, rgba(0, 0, 0, 0) 18%);
  opacity: 0;
  transition: opacity .35s ease;
  pointer-events: none;
}
.lunBoZheZhao {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, .52) 100%);
  opacity: 0;
  transition: opacity .35s ease;
  pointer-events: none;
}
.lunBoXiang:hover .lunBoTian,
.lunBoXiang:hover .lunBoZheZhao { opacity: 1; }

.lunBoSu {
  position: absolute;
  top: 14px;
  right: 16px;
  z-index: 4;
  font-size: 11.5px;
  font-weight: 700;
  letter-spacing: .5px;
  color: #fff;
  background: rgba(17, 24, 39, .42);
  padding: 4px 10px;
  border-radius: 999px;
  backdrop-filter: blur(6px);
}
.lunBoKaPian {
  position: absolute;
  left: 18px;
  right: 92px;
  bottom: 14px;
  z-index: 3;
  opacity: 0;
  transform: translateY(8px);
  transition: opacity .3s ease, transform .3s ease;
}
.lunBoXiang:hover .lunBoKaPian { opacity: 1; transform: translateY(0); }
.lunBoKaPian strong {
  display: block;
  font-size: 18px;
  font-weight: 800;
  color: #fff;
  letter-spacing: .5px;
  text-shadow: 0 2px 12px rgba(0, 0, 0, .55);
}
.lunBoKaPian .lunBoFu {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, .88);
  margin-top: 3px;
  line-height: 1.45;
  text-shadow: 0 1px 8px rgba(0, 0, 0, .55);
}
.lunBoZhiShiQi {
  position: absolute;
  right: 18px;
  bottom: 20px;
  z-index: 5;
  display: flex;
  gap: 6px;
}
.lunBoZhiShiQi .tiao {
  width: 16px;
  height: 4px;
  border-radius: 999px;
  background: rgba(17, 24, 39, .26);
  cursor: pointer;
  transition: all .3s ease;
}
.lunBoZhiShiQi .tiao:hover { background: rgba(17, 24, 39, .45); }
.lunBoZhiShiQi .tiao.xuan {
  width: 28px;
  background: #111827;
}

.huan1, .huan2, .huan3 { position: absolute; border-radius: 50%; }
.huan1 { width: 270px; height: 270px; border: 1px dashed rgba(255, 255, 255, .35); }
.huan2 { width: 205px; height: 205px; border: 1px solid rgba(255, 255, 255, .22); }
.huan3 { width: 150px; height: 150px; background: rgba(255, 255, 255, .10); }
.heroCard {
  position: relative;
  z-index: 2;
  width: 132px;
  height: 132px;
  border-radius: 34px;
  background: rgba(255, 255, 255, .16);
  border: 1px solid rgba(255, 255, 255, .38);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 20px 46px rgba(0, 0, 0, .20);
  backdrop-filter: blur(6px);
}
.yingXiongTuBiao { font-size: 66px; color: #fff; }
.fuBiao {
  position: absolute;
  z-index: 3;
  background: #fff;
  color: var(--zhuSe);
  font-size: 12.5px;
  font-weight: 700;
  padding: 7px 13px;
  border-radius: 999px;
  box-shadow: 0 10px 24px rgba(0, 0, 0, .16);
}
.fuBiao1 { top: 26px; left: -6px; }
.fuBiao2 { top: 108px; right: -14px; }
.fuBiao3 { bottom: 22px; left: 12px; }

/* ===== 区块标题 ===== */
.quTou { display: flex; align-items: baseline; gap: 14px; flex-wrap: wrap; }
.kuaiBiaoTi {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 21px;
  margin: 34px 0 16px;
  color: var(--wenZi);
}
.kuaiBiaoTi .el-icon { color: var(--zhuSe); font-size: 22px; }
.quFuZhu { font-size: 13px; color: var(--wenZiSan); }

/* ===== 分类 ===== */
.fenLeiWangGe {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 14px;
}
.fenLeiKaPian {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 18px 10px;
  background: #fff;
  border-radius: 16px;
  cursor: pointer;
  transition: transform .2s ease, box-shadow .2s ease, border-color .2s ease;
  border: 1px solid var(--bianKuang);
}
.fenLeiKaPian:hover { transform: translateY(-4px); box-shadow: var(--banYing); border-color: #d7e2ff; }
.fenLeiTuBiao {
  font-size: 22px;
  width: 46px;
  height: 46px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  background: #eef3ff;
  color: var(--zhuSe);
  transition: .2s;
}
.fenLeiKaPian span { font-size: 13.5px; color: var(--wenZi); }
.fenLeiKaPian.xuanZhong {
  background: var(--zhuTiDu);
  border-color: transparent;
  box-shadow: 0 12px 26px rgba(47, 107, 255, .28);
}
.fenLeiKaPian.xuanZhong span { color: #fff; font-weight: 600; }
.fenLeiKaPian.xuanZhong .fenLeiTuBiao { background: rgba(255, 255, 255, .2); color: #fff; }

/* ===== 商品 ===== */
.shangPinTou { display: flex; align-items: flex-end; justify-content: space-between; gap: 16px; }
.shangPinTouWen .kuaiBiaoTi { margin: 34px 0 6px; }
.shangPinFuZhu { font-size: 13px; color: var(--wenZiSan); margin: 0 0 16px; }

.kongZhuangTai {
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: var(--yuanJiao);
  padding: 26px 0;
}

.wangGe {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 18px;
}
.shangPinKaPian {
  background: #fff;
  border-radius: var(--yuanJiao);
  overflow: hidden;
  cursor: pointer;
  transition: transform .22s ease, box-shadow .22s ease, border-color .22s ease;
  border: 1px solid var(--bianKuang);
}
.shangPinKaPian:hover { transform: translateY(-5px); box-shadow: var(--banYing); border-color: #d7e2ff; }
.shangPinTuPian { position: relative; height: 180px; overflow: hidden; background: #f5f7fb; }
.shangPinTuPian img { width: 100%; height: 100%; object-fit: cover; transition: transform .45s ease; }
.shangPinKaPian:hover .shangPinTuPian img { transform: scale(1.06); }
.shangPinTuPian .tuPianZhanWei { width: 100%; height: 100%; font-size: 52px; }
.jiaGeBiaoQian {
  position: absolute;
  left: 10px;
  bottom: 10px;
  background: rgba(0, 0, 0, .62);
  color: #fff;
  padding: 3px 10px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 600;
  backdrop-filter: blur(4px);
}
.shangPinXinXi { padding: 12px 14px 14px; }
.shangPinBiaoTi {
  font-size: 15px;
  color: var(--wenZi);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.shangPinDiBu { display: flex; align-items: center; justify-content: space-between; margin-top: 10px; }
.faBuZhe { display: flex; align-items: center; gap: 4px; color: var(--wenZiSan); font-size: 12px; }
.jiaGe { color: var(--hongSe); font-weight: 800; font-size: 16px; }
.fenYe { display: flex; justify-content: center; margin-top: 28px; }

@media (max-width: 880px) {
  .yingXiongYou { display: none; }
  .yingXiongZuo h1 { font-size: 32px; }
  .yingXiongNei { padding: 44px 24px; }
  .shuJu { gap: 24px; }
  .shuJu b { font-size: 24px; }
}
</style>
