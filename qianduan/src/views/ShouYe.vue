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
            <div><b>7</b><span>商品分类</span></div>
            <div><b>24h</b><span>极速交易</span></div>
          </div>
        </div>
        <div class="yingXiongYou">
          <div class="huan1"></div>
          <div class="huan2"></div>
          <div class="huan3"></div>
          <div class="heroCard">
            <el-icon class="yingXiongTuBiao"><ShoppingBag /></el-icon>
          </div>
          <span class="fuBiao fuBiao1">教材 9 成新</span>
          <span class="fuBiao fuBiao2">数码好价</span>
          <span class="fuBiao fuBiao3">宿舍好物</span>
        </div>
      </div>
    </section>

    <div class="rongQi">
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
  guanJianCi.value = luYouXin.query.kw || ''
  jiaZaiShangPin()
})
</script>

<style scoped>
.shouYe { padding-bottom: 40px; }

/* ===== Hero ===== */
.yingXiong {
  position: relative;
  overflow: hidden;
  color: #fff;
  border-radius: 0 0 28px 28px;
  background:
    radial-gradient(circle at 84% 14%, rgba(255, 255, 255, .22) 0, rgba(255, 255, 255, 0) 40%),
    radial-gradient(circle at 6% 94%, rgba(255, 255, 255, .14) 0, rgba(255, 255, 255, 0) 38%),
    linear-gradient(120deg, #2f6bff 0%, #2b8bf0 48%, #15c5b8 100%);
}
.heroDots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, .5) 1px, transparent 1px);
  background-size: 22px 22px;
  opacity: .18;
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
  background: rgba(255, 255, 255, .16);
  border: 1px solid rgba(255, 255, 255, .28);
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
.yingXiongZuo p { font-size: 16px; opacity: .92; margin: 0 0 26px; }
.zhuTiWenZiBai { color: #fff; text-shadow: 0 2px 14px rgba(0, 0, 0, .2); }

.yingXiongSouSuo { max-width: 480px; }
.yingXiongSouSuo :deep(.el-input__wrapper) {
  border-radius: 999px;
  padding-left: 18px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, .16);
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
.shuJu span { font-size: 13px; opacity: .85; margin-top: 3px; }

/* Hero 右侧插画 */
.yingXiongYou {
  position: relative;
  width: 320px;
  height: 280px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
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
