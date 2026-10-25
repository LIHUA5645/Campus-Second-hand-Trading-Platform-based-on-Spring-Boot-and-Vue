<!-- 作者：肖沐樑 QQ：2047419378 -->
<template>
  <div class="xiaoXiZhongXin">
    <div class="rongQi">
      <h2 class="kuaiBiaoTi">
        <el-icon><ChatDotRound /></el-icon> 消息中心
        <span class="fuBiaoTi">和商品发布者聊聊，交易更放心</span>
      </h2>

      <el-card class="kePian liaoTianKa" shadow="never">
        <div class="liaoTianBu">
          <aside class="huiHuaLie">
            <div class="lieTou">
              <span class="lieTi">全部会话</span>
              <span v-if="zongWeiDu" class="weiDuZong">{{ zongWeiDu > 99 ? '99+' : zongWeiDu }} 条未读</span>
            </div>
            <div class="huiHuaGun" v-loading="huiHuaJiaZai">
              <div
                v-for="h in huiHuaLie"
                :key="huiHuaJian(h)"
                class="huiHuaXiang"
                :class="{ huoYue: shiHuoYue(h) }"
                @click="daKai(h)"
              >
                <div class="touXiangKuang">
                  <img v-if="h.duiFangTouXiang" :src="h.duiFangTouXiang" alt="头像" class="touXiangTu" />
                  <el-avatar v-else :size="42" class="touXiangZhan">{{ (h.duiFangMing || '匿').slice(0, 1) }}</el-avatar>
                  <span v-if="h.weiDuShu" class="hongDian">{{ h.weiDuShu > 99 ? '99+' : h.weiDuShu }}</span>
                </div>
                <div class="huiHuaXin">
                  <div class="hang1">
                    <span class="ming">{{ h.duiFangMing || '匿名用户' }}</span>
                    <span class="shiJian">{{ duanShiJian(h.zuiHouShiJian) }}</span>
                  </div>
                  <div class="hang2">{{ h.zuiHouShiWo ? '我：' : '' }}{{ h.zuiHouNeiRong }}</div>
                  <div v-if="h.shangPinBiaoTi" class="hang3">
                    <img v-if="h.shangPinTuPian" :src="h.shangPinTuPian" alt="商品" class="hang3Tu" />
                    <el-icon v-else><Goods /></el-icon>
                    <span class="hang3Wen">{{ h.shangPinBiaoTi }}</span>
                  </div>
                </div>
              </div>
              <el-empty
                v-if="!huiHuaJiaZai && huiHuaLie.length === 0"
                description="还没有收到消息，去商品详情页找卖家聊聊吧"
                :image-size="88"
              />
            </div>
          </aside>

          <section v-if="dangQian" class="liaoTianQu">
            <header class="liaoShou">
              <div class="shouZuo">
                <img v-if="dangQian.duiFangTouXiang" :src="dangQian.duiFangTouXiang" alt="头像" class="shouTou" />
                <el-avatar v-else :size="38" class="touXiangZhan">{{ (dangQian.duiFangMing || '匿').slice(0, 1) }}</el-avatar>
                <div>
                  <div class="liaoMing">{{ dangQian.duiFangMing || '匿名用户' }}</div>
                  <div class="liaoFu">校园卖家 · 诚信交易</div>
                </div>
              </div>
              <div v-if="dangQian.shangPinId" class="shangPinKa" @click="quShangPin">
                <div class="kaTu">
                  <img v-if="dangQian.shangPinTuPian" :src="dangQian.shangPinTuPian" alt="商品图" />
                  <div v-else class="kaZhanWei"><el-icon><Picture /></el-icon></div>
                </div>
                <div class="kaWen">
                  <div class="kaBiaoTi">{{ dangQian.shangPinBiaoTi || '关联商品' }}</div>
                  <div class="kaFu">
                    <span class="kaJia">￥{{ dangQian.shangPinJiaGe ?? '—' }}</span> · 点击查看
                  </div>
                </div>
                <el-icon class="kaJian"><ArrowRight /></el-icon>
              </div>
            </header>

            <div ref="xiaoXiGunRef" class="xiaoXiGun" v-loading="xiaoXiJiaZai">
              <div v-if="xiaoXiLie.length === 0" class="kongZhuangTai">
                这是你和 {{ dangQian.duiFangMing || '卖家' }} 的会话，先打个招呼吧～
              </div>
              <div v-for="x in xiaoXiLie" :key="x.id" class="xiaoXiHang" :class="{ woFa: shiWoFa(x) }">
                <img v-if="shiWoFa(x) && touXiang" :src="touXiang" alt="我" class="xiaoTou" />
                <el-avatar v-else-if="shiWoFa(x)" :size="34" class="xiaoTouZhan">{{ woMing.slice(0, 1) }}</el-avatar>
                <img v-else-if="dangQian.duiFangTouXiang" :src="dangQian.duiFangTouXiang" alt="对方" class="xiaoTou" />
                <el-avatar v-else :size="34" class="xiaoTouZhan">{{ (dangQian.duiFangMing || '匿').slice(0, 1) }}</el-avatar>
                <div class="qiPao">{{ x.neiRong }}</div>
              </div>
            </div>

            <footer class="shuRuQu">
              <el-input
                v-model="neiRong"
                type="textarea"
                :rows="3"
                resize="none"
                maxlength="1000"
                show-word-limit
                placeholder="输入消息，Enter 发送，Shift + Enter 换行"
                @keydown.enter.exact.prevent="faSong"
              />
              <div class="shuRuHang">
                <span class="anQuanTiShi"><el-icon><Lock /></el-icon> 请勿脱离平台交易，注意财产安全</span>
                <el-button type="primary" round :loading="zhengZaiFa" @click="faSong">
                  <el-icon><Promotion /></el-icon> 发送
                </el-button>
              </div>
            </footer>
          </section>

          <section v-else class="kongLiaoTian">
            <el-empty description="选择左侧的会话，开始和卖家聊天" />
          </section>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const luYouXin = useRoute()

const yongHu = ref(null)
const huiHuaLie = ref([])
const dangQian = ref(null)
const xiaoXiLie = ref([])
const neiRong = ref('')
const huiHuaJiaZai = ref(false)
const xiaoXiJiaZai = ref(false)
const zhengZaiFa = ref(false)
const xiaoXiGunRef = ref(null)
let lunXunJiShi = null

const woMing = computed(() => {
  const y = yongHu.value
  return y ? (y.xingMing || y.yongHuMing || '我') : '我'
})
const touXiang = computed(() => (yongHu.value ? yongHu.value.touXiang : ''))
const zongWeiDu = computed(() => huiHuaLie.value.reduce((he, h) => he + (h.weiDuShu || 0), 0))

const huiHuaJian = (h) => (h.duiFangId || 0) + '_' + (h.shangPinId || 0)
const shiHuoYue = (h) => dangQian.value && huiHuaJian(dangQian.value) === huiHuaJian(h)
const shiWoFa = (x) => yongHu.value && x.faSongRenId === yongHu.value.id

const duanShiJian = (zhi) => {
  if (!zhi) return ''
  const shi = new Date(zhi)
  if (isNaN(shi.getTime())) return ''
  const xianZai = new Date()
  const buLing = (n) => (n < 10 ? '0' + n : '' + n)
  const tongTian = shi.getFullYear() === xianZai.getFullYear()
    && shi.getMonth() === xianZai.getMonth()
    && shi.getDate() === xianZai.getDate()
  if (tongTian) return buLing(shi.getHours()) + ':' + buLing(shi.getMinutes())
  return (shi.getMonth() + 1) + '-' + buLing(shi.getDate())
}

const gunDaoDi = () => {
  const he = xiaoXiGunRef.value
  if (he) he.scrollTop = he.scrollHeight
}

const tieDi = () => {
  const he = xiaoXiGunRef.value
  if (!he) return true
  return he.scrollHeight - he.scrollTop - he.clientHeight < 90
}

const jiaZaiHuiHua = async (baoChiXuanZhong = false) => {
  if (!yongHu.value) return
  huiHuaJiaZai.value = true
  try {
    const res = await jieKou.xiaoXiHuiHua(yongHu.value.id)
    if (res.data.chengGong) {
      huiHuaLie.value = res.data.lieBiao || []
      if (baoChiXuanZhong && dangQian.value) {
        const jian = huiHuaJian(dangQian.value)
        const xin = huiHuaLie.value.find((h) => huiHuaJian(h) === jian)
        if (xin) {
          dangQian.value = xin
        } else if (!xiaoXiLie.value.length) {
          // 刚发出的第一条消息还没落库到列表时保留占位会话
          const zhanWei = { ...dangQian.value }
          huiHuaLie.value.unshift(zhanWei)
          dangQian.value = zhanWei
        }
      }
    }
  } catch (e) {
    // 网络异常时保留原有列表
  } finally {
    huiHuaJiaZai.value = false
  }
}

const jiaZaiXiaoXi = async (gunDong = true) => {
  if (!yongHu.value || !dangQian.value || !dangQian.value.shangPinId) {
    xiaoXiLie.value = []
    xiaoXiJiaZai.value = false
    return
  }
  const yuanLaiTieDi = tieDi()
  try {
    const res = await jieKou.xiaoXiLieBiao(yongHu.value.id, dangQian.value.duiFangId, dangQian.value.shangPinId)
    if (res.data.chengGong) {
      xiaoXiLie.value = res.data.lieBiao || []
      await nextTick()
      if (gunDong && yuanLaiTieDi) gunDaoDi()
    }
  } finally {
    xiaoXiJiaZai.value = false
  }
}

const daKai = async (h) => {
  dangQian.value = h
  xiaoXiLie.value = []
  xiaoXiJiaZai.value = true
  await jiaZaiXiaoXi()
  h.weiDuShu = 0
  // 通知顶栏刷新未读数，打开会话后小红点立即消失
  window.dispatchEvent(new Event('xiaoXiYiDu'))
  await nextTick()
  gunDaoDi()
}

const faSong = async () => {
  const wenBen = neiRong.value.trim()
  if (!dangQian.value) return
  if (!wenBen) {
    ElMessage.warning('请输入消息内容')
    return
  }
  zhengZaiFa.value = true
  try {
    const res = await jieKou.faSongXiaoXi({
      faSongRenId: yongHu.value.id,
      jieShouRenId: dangQian.value.duiFangId,
      shangPinId: dangQian.value.shangPinId || null,
      neiRong: wenBen
    })
    if (res.data.chengGong) {
      xiaoXiLie.value.push(res.data.xiaoXi)
      neiRong.value = ''
      await nextTick()
      gunDaoDi()
      const huiHua = dangQian.value
      jiaZaiHuiHua(false).then(() => {
        const jian = huiHuaJian(huiHua)
        const xin = huiHuaLie.value.find((h) => huiHuaJian(h) === jian)
        if (xin) dangQian.value = xin
      })
    } else {
      ElMessage.error(res.data.xinXi || '发送失败')
    }
  } finally {
    zhengZaiFa.value = false
  }
}

const quShangPin = () => {
  if (dangQian.value && dangQian.value.shangPinId) {
    luYou.push('/shangpin/' + dangQian.value.shangPinId)
  }
}

// 从商品详情页点「发消息」跳转过来时，直接打开对应会话
const congCanShuDaKai = () => {
  const canShu = luYouXin.query
  if (!canShu.duiFangId) return
  const duiFangId = Number(canShu.duiFangId)
  const shangPinId = canShu.shangPinId ? Number(canShu.shangPinId) : null
  const yiYou = huiHuaLie.value.find((h) => h.duiFangId === duiFangId && h.shangPinId === shangPinId)
  if (yiYou) {
    daKai(yiYou)
    return
  }
  daKai({
    duiFangId: duiFangId,
    duiFangMing: canShu.duiFangMing || '卖家',
    duiFangTouXiang: canShu.duiFangTouXiang || '',
    shangPinId: shangPinId,
    shangPinBiaoTi: canShu.shangPinBiaoTi || '',
    shangPinTuPian: canShu.shangPinTuPian || '',
    shangPinJiaGe: canShu.shangPinJiaGe ? Number(canShu.shangPinJiaGe) : null,
    weiDuShu: 0
  })
}

onMounted(async () => {
  const cun = localStorage.getItem('yongHu')
  if (!cun) {
    luYou.push('/denglu')
    return
  }
  yongHu.value = JSON.parse(cun)
  await jiaZaiHuiHua()
  congCanShuDaKai()
  // 轮询新消息，保证卖家回消息能及时看到
  lunXunJiShi = setInterval(async () => {
    await jiaZaiHuiHua(true)
    if (dangQian.value) await jiaZaiXiaoXi(false)
  }, 10000)
})

onUnmounted(() => {
  if (lunXunJiShi) clearInterval(lunXunJiShi)
})
</script>

<style scoped>
.xiaoXiZhongXin { padding: 8px 0 24px; }
.kuaiBiaoTi { display: flex; align-items: center; gap: 8px; font-size: 22px; margin: 24px 0 16px; color: var(--wenZi); }
.kuaiBiaoTi .el-icon { color: var(--zhuSe); }
.fuBiaoTi { font-size: 13px; font-weight: 400; color: var(--wenZiSan); margin-left: 6px; }

.liaoTianKa { padding: 0; overflow: hidden; }
.liaoTianKa :deep(.el-card__body) { padding: 0; }
.liaoTianBu { display: flex; height: calc(100vh - 230px); min-height: 540px; max-height: 760px; }

/* ===== 左侧会话列表 ===== */
.huiHuaLie { width: 320px; flex-shrink: 0; border-right: 1px solid var(--bianKuang); display: flex; flex-direction: column; background: #fbfcff; }
.lieTou { display: flex; align-items: center; justify-content: space-between; padding: 16px 18px; border-bottom: 1px solid var(--bianKuang); }
.lieTi { font-size: 15px; font-weight: 700; color: var(--wenZi); }
.weiDuZong { font-size: 12px; color: #fff; background: var(--hongSe); border-radius: 999px; padding: 2px 9px; }
.huiHuaGun { flex: 1; overflow-y: auto; padding: 8px; }
.huiHuaXiang { display: flex; gap: 11px; padding: 12px; border-radius: 12px; cursor: pointer; transition: background .18s ease; }
.huiHuaXiang:hover { background: #f0f4ff; }
.huiHuaXiang.huoYue { background: linear-gradient(135deg, #eef3ff, #e9fbf8); box-shadow: inset 0 0 0 1px #dbe6ff; }
.touXiangKuang { position: relative; flex-shrink: 0; }
.touXiangTu { width: 42px; height: 42px; border-radius: 50%; object-fit: cover; display: block; }
.touXiangZhan { background: var(--zhuTiDu); color: #fff; font-weight: 700; }
.hongDian {
  position: absolute; top: -4px; right: -6px; min-width: 17px; height: 17px; padding: 0 4px;
  background: var(--hongSe); color: #fff; font-size: 11px; line-height: 17px; text-align: center;
  border-radius: 999px; box-shadow: 0 2px 6px rgba(245, 57, 91, .35);
}
.huiHuaXin { flex: 1; min-width: 0; }
.hang1 { display: flex; align-items: center; justify-content: space-between; gap: 8px; }
.ming { font-size: 14px; font-weight: 600; color: var(--wenZi); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.shiJian { font-size: 11px; color: var(--wenZiSan); flex-shrink: 0; }
.hang2 { margin-top: 3px; font-size: 13px; color: var(--wenZiEr); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.hang3 { margin-top: 5px; display: flex; align-items: center; gap: 4px; font-size: 11.5px; color: var(--wenZiSan); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.hang3 .el-icon { font-size: 12px; color: var(--qingSe); }
.hang3Tu { width: 24px; height: 24px; border-radius: 6px; object-fit: cover; flex-shrink: 0; }
.hang3Wen { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

/* ===== 右侧聊天区 ===== */
.liaoTianQu { flex: 1; display: flex; flex-direction: column; min-width: 0; background: #fff; }
.liaoShou { display: flex; align-items: center; justify-content: space-between; gap: 16px; padding: 13px 20px; border-bottom: 1px solid var(--bianKuang); }
.shouZuo { display: flex; align-items: center; gap: 10px; min-width: 0; }
.shouTou { width: 38px; height: 38px; border-radius: 50%; object-fit: cover; display: block; }
.liaoMing { font-size: 15px; font-weight: 700; color: var(--wenZi); }
.liaoFu { font-size: 12px; color: var(--wenZiSan); margin-top: 2px; }
.shangPinKa {
  display: flex; align-items: center; gap: 10px; padding: 7px 10px; max-width: 300px;
  border: 1px solid var(--bianKuang); border-radius: 12px; cursor: pointer; transition: all .18s ease;
}
.shangPinKa:hover { border-color: #c9dbff; box-shadow: 0 6px 16px rgba(47, 107, 255, .1); transform: translateY(-1px); }
.kaTu { width: 38px; height: 38px; border-radius: 8px; overflow: hidden; flex-shrink: 0; }
.kaTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.kaZhanWei { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: var(--beiJing); color: #b9c6da; }
.kaWen { min-width: 0; }
.kaBiaoTi { font-size: 13px; font-weight: 600; color: var(--wenZi); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.kaFu { font-size: 11px; color: var(--wenZiSan); margin-top: 2px; }
.kaJia { color: var(--hongSe); font-weight: 700; }
.kaJian { color: var(--wenZiSan); font-size: 13px; flex-shrink: 0; }

.xiaoXiGun { flex: 1; overflow-y: auto; padding: 20px 22px; background: linear-gradient(180deg, #fbfcff 0%, #ffffff 60%); }
.kongZhuangTai { text-align: center; color: var(--wenZiSan); font-size: 13px; padding: 40px 0; }
.xiaoXiHang { display: flex; align-items: flex-start; gap: 9px; margin-bottom: 16px; }
.xiaoXiHang.woFa { flex-direction: row-reverse; }
.xiaoTou { width: 34px; height: 34px; border-radius: 50%; object-fit: cover; flex-shrink: 0; display: block; }
.xiaoTouZhan { background: var(--zhuTiDu); color: #fff; font-weight: 700; flex-shrink: 0; }
.qiPao {
  max-width: 62%; padding: 10px 13px; font-size: 14px; line-height: 1.65;
  color: var(--wenZi); background: #f2f5fa; border-radius: 4px 14px 14px 14px;
  white-space: pre-wrap; word-break: break-word;
}
.woFa .qiPao {
  background: linear-gradient(135deg, #2f6bff 0%, #15c5b8 100%);
  color: #fff; border-radius: 14px 4px 14px 14px;
  box-shadow: 0 6px 16px rgba(47, 107, 255, .22);
}

.shuRuQu { border-top: 1px solid var(--bianKuang); padding: 12px 18px 14px; background: #fff; }
.shuRuQu :deep(.el-textarea__inner) { border-radius: 12px; font-size: 14px; box-shadow: 0 0 0 1px var(--bianKuang) inset; }
.shuRuQu :deep(.el-textarea__inner:focus) { box-shadow: 0 0 0 1px var(--zhuSe) inset; }
.shuRuHang { display: flex; align-items: center; justify-content: space-between; margin-top: 10px; }
.anQuanTiShi { display: flex; align-items: center; gap: 4px; font-size: 12px; color: var(--wenZiSan); }
.anQuanTiShi .el-icon { color: var(--qingSe); }
.kongLiaoTian { flex: 1; display: flex; align-items: center; justify-content: center; }

@media (max-width: 860px) {
  .liaoTianBu { flex-direction: column; height: auto; }
  .huiHuaLie { width: 100%; max-height: 260px; border-right: none; border-bottom: 1px solid var(--bianKuang); }
  .liaoTianQu { min-height: 480px; }
  .shangPinKa { display: none; }
  .qiPao { max-width: 78%; }
}
</style>
