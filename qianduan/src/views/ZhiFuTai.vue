<!-- 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24 -->
<template>
  <div class="zhiFuTai">
    <div class="rongQi">
      <h2 class="kuaiBiaoTi">
        <el-icon><Wallet /></el-icon> 收银台
        <span class="fuBiaoTi">请在 15 分钟内完成支付，超时订单将保留但需重新发起</span>
      </h2>

      <el-card v-if="dingDan" class="kePian">
        <div class="shouYinBu">
          <div class="dingDanKa">
            <div class="shangPinHang">
              <div class="tuPianQu">
                <img v-if="dingDan.tuPian" :src="dingDan.tuPian" alt="商品图" />
                <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
              </div>
              <div class="shangPinXin">
                <div class="biaoTi">{{ dingDan.shangPinBiaoTi }}</div>
                <div class="fuXinXi">订单号：{{ dingDanHao }}</div>
                <div class="fuXinXi">下单时间：{{ shiJian(dingDan.xiaDanShiJian) }}</div>
              </div>
            </div>
            <el-divider />
            <div class="xinXiHang"><span>收货地址</span><b>{{ dingDan.diZhi || '—' }}</b></div>
            <div v-if="dingDan.liuYan" class="xinXiHang"><span>买家留言</span><b>{{ dingDan.liuYan }}</b></div>
            <div class="xinXiHang"><span>订单状态</span><b>{{ zhuangTaiWenBen }}</b></div>
            <div class="yingFuHang">
              <span>应付金额</span>
              <b class="jinE">￥{{ dingDan.jiaGe }}</b>
            </div>
          </div>

          <div class="fangShiKa">
            <h3><el-icon><CreditCard /></el-icon> 选择支付方式</h3>

            <div class="fangShiXiang" :class="{ xuanZhong: fangShi === 'alipay' }" @click="fangShi = 'alipay'">
              <div class="fangShiTu zhiFuBaoTu"><el-icon><Money /></el-icon></div>
              <div class="fangShiWen">
                <div class="fangShiMing">支付宝</div>
                <div class="fangShiFu">{{ zhiFuBaoShuoMing }}</div>
              </div>
              <el-icon v-if="fangShi === 'alipay'" class="gou"><Select /></el-icon>
            </div>

            <div class="fangShiXiang" :class="{ xuanZhong: fangShi === 'huodao' }" @click="fangShi = 'huodao'">
              <div class="fangShiTu xianXiaTu"><el-icon><Shop /></el-icon></div>
              <div class="fangShiWen">
                <div class="fangShiMing">货到付款 / 当面交易</div>
                <div class="fangShiFu">校内当面验货，双方自行收款，平台不参与资金</div>
              </div>
              <el-icon v-if="fangShi === 'huodao'" class="gou"><Select /></el-icon>
            </div>

            <div class="daoJiShi">
              <el-icon><Timer /></el-icon> 请在 <b>{{ shengYu }}</b> 内完成支付
            </div>

            <el-button type="primary" size="large" round class="fuKuanAnNiu" :loading="jiaZaiZhong" @click="quFuKuan">
              {{ fangShi === 'alipay' ? '支付宝付款 ￥' + dingDan.jiaGe : '提交订单（线下交易）' }}
            </el-button>
            <p class="anQuanTiShi"><el-icon><Lock /></el-icon> 付款资金由支付宝担保，请勿脱离平台交易</p>
          </div>
        </div>
      </el-card>

      <el-skeleton v-else :rows="8" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const luYouXin = useRoute()

const dingDan = ref(null)
const fangShi = ref('alipay')
const jiaZaiZhong = ref(false)
const yiPeiZhi = ref(true)
const shiFouShaXiang = ref(true)
const shengYuMiao = ref(15 * 60)
let jiShiQi = null

const dingDanHao = computed(() => 'SP' + (dingDan.value ? dingDan.value.id : ''))
const zhiFuBaoShuoMing = computed(() => {
  if (!yiPeiZhi.value) return '管理员尚未配置支付，请改用线下交易'
  return shiFouShaXiang.value ? '沙箱环境：不会真实扣款，用于联调验证' : '正式环境：将真实扣款'
})
const zhuangTaiWenBen = computed(() => {
  const d = dingDan.value
  if (!d) return ''
  if (d.zhuangTai === 3) return '已取消'
  if (d.zhuangTai === 2) return '已完成'
  if (d.zhiFuZhuangTai === 0) return d.zhiFuFangShi === 'huodao' ? '待当面付款' : '待付款'
  return ['待发货', '待收货', '已完成', '已取消'][d.zhuangTai] || '未知'
})
const shengYu = computed(() => {
  const f = Math.floor(shengYuMiao.value / 60)
  const m = shengYuMiao.value % 60
  return (f < 10 ? '0' + f : f) + ':' + (m < 10 ? '0' + m : m)
})

const shiJian = (zhi) => {
  if (!zhi) return '—'
  const d = new Date(zhi)
  if (isNaN(d.getTime())) return String(zhi)
  const buLing = (n) => (n < 10 ? '0' + n : '' + n)
  return d.getFullYear() + '-' + buLing(d.getMonth() + 1) + '-' + buLing(d.getDate())
    + ' ' + buLing(d.getHours()) + ':' + buLing(d.getMinutes())
}

const jiaZai = async () => {
  const id = luYouXin.params.id
  const res = await jieKou.dingDanXiangQing(id)
  if (res.data.chengGong && res.data.dingDan) {
    dingDan.value = res.data.dingDan
    if (dingDan.value.zhiFuZhuangTai === 1) {
      ElMessage.info('该订单已支付')
      luYou.push('/dingdan')
    }
  } else {
    ElMessage.error(res.data.xinXi || '订单不存在')
    luYou.push('/dingdan')
  }
}

// 后台是否填过支付参数
const jiaZaiPeiZhi = async () => {
  try {
    const res = await jieKou.zhiFuPeiZhi()
    if (res.data.chengGong) {
      yiPeiZhi.value = !!res.data.yiPeiZhi && !!res.data.appId
      shiFouShaXiang.value = res.data.shaXiang !== false
      if (!yiPeiZhi.value) fangShi.value = 'huodao'
    }
  } catch (e) {
    yiPeiZhi.value = false
    fangShi.value = 'huodao'
  }
}

const quFuKuan = async () => {
  if (!dingDan.value) return
  jiaZaiZhong.value = true
  try {
    if (fangShi.value === 'huodao') {
      const res = await jieKou.zhiFuHuoDao(dingDan.value.id)
      if (res.data.chengGong) {
        ElMessage.success('已提交，请与卖家当面交易并付款')
        luYou.push('/dingdan')
      } else {
        ElMessage.error(res.data.xinXi || '提交失败')
      }
      return
    }
    const fanHui = window.location.origin + '/zhifu/jieguo?dingDanId=' + dingDan.value.id
    const res = await jieKou.zhiFuXiaDan({ dingDanId: dingDan.value.id, fanHuiDiZhi: fanHui })
    if (res.data.chengGong && res.data.html) {
      // 后端返回的是自动提交到支付宝的表单，塞进页面即可跳转
      const rongQi = document.createElement('div')
      rongQi.innerHTML = res.data.html
      document.body.appendChild(rongQi)
    } else {
      ElMessage.error(res.data.xinXi || '发起支付失败')
    }
  } finally {
    jiaZaiZhong.value = false
  }
}

onMounted(async () => {
  const y = localStorage.getItem('yongHu')
  if (!y) { luYou.push('/denglu'); return }
  await jiaZaiPeiZhi()
  await jiaZai()
  jiShiQi = setInterval(() => {
    if (shengYuMiao.value > 0) shengYuMiao.value -= 1
  }, 1000)
})

onUnmounted(() => {
  if (jiShiQi) clearInterval(jiShiQi)
})
</script>

<style scoped>
.zhiFuTai { padding: 12px 0 40px; }
.kuaiBiaoTi { display: flex; align-items: center; gap: 8px; font-size: 22px; margin: 24px 0 16px; color: var(--wenZi); }
.kuaiBiaoTi .el-icon { color: var(--zhuSe); }
.fuBiaoTi { font-size: 13px; font-weight: 400; color: var(--wenZiSan); margin-left: 6px; }

.shouYinBu { display: flex; gap: 28px; }
.dingDanKa { flex: 1; min-width: 0; }
.fangShiKa { width: 380px; flex-shrink: 0; padding: 18px; border: 1px solid var(--bianKuang); border-radius: 14px; background: #fbfcff; }
.fangShiKa h3 { display: flex; align-items: center; gap: 6px; margin: 0 0 14px; font-size: 16px; color: var(--wenZi); }
.fangShiKa h3 .el-icon { color: var(--zhuSe); }

.shangPinHang { display: flex; gap: 14px; }
.tuPianQu { width: 84px; height: 84px; border-radius: 12px; overflow: hidden; flex-shrink: 0; background: var(--beiJing); }
.tuPianQu img { width: 100%; height: 100%; object-fit: cover; }
.tuPianQu .tuPianZhanWei { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 30px; color: #cdd6e4; background: linear-gradient(135deg, #eef3ff, #e7fbf5); }
.shangPinXin { min-width: 0; }
.biaoTi { font-size: 16px; font-weight: 700; color: var(--wenZi); }
.fuXinXi { margin-top: 4px; font-size: 12.5px; color: var(--wenZiSan); }
.xinXiHang { display: flex; justify-content: space-between; gap: 16px; padding: 8px 0; font-size: 13.5px; color: var(--wenZiEr); }
.xinXiHang b { color: var(--wenZi); font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.yingFuHang { display: flex; align-items: baseline; justify-content: space-between; margin-top: 10px; padding-top: 14px; border-top: 1px dashed var(--bianKuang); font-size: 14px; color: var(--wenZiEr); }
.jinE { font-size: 28px; font-weight: 800; color: var(--hongSe); }

.fangShiXiang {
  display: flex; align-items: center; gap: 12px; padding: 12px 14px; margin-bottom: 10px;
  border: 1px solid var(--bianKuang); border-radius: 12px; background: #fff; cursor: pointer;
  transition: border-color .18s ease, box-shadow .18s ease;
}
.fangShiXiang:hover { border-color: #c9dbff; }
.fangShiXiang.xuanZhong { border-color: var(--zhuSe); box-shadow: 0 6px 16px rgba(47, 107, 255, .12); }
.fangShiTu { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 20px; flex-shrink: 0; }
.zhiFuBaoTu { background: linear-gradient(135deg, #1677ff, #4096ff); }
.xianXiaTu { background: linear-gradient(135deg, #ff7a45, #ffb26b); }
.fangShiWen { flex: 1; min-width: 0; }
.fangShiMing { font-size: 14px; font-weight: 600; color: var(--wenZi); }
.fangShiFu { margin-top: 3px; font-size: 11.5px; color: var(--wenZiSan); }
.gou { color: var(--zhuSe); font-size: 18px; flex-shrink: 0; }

.daoJiShi { display: flex; align-items: center; justify-content: center; gap: 5px; margin: 14px 0 12px; font-size: 12.5px; color: var(--wenZiSan); }
.daoJiShi b { color: var(--hongSe); font-size: 14px; }
.fuKuanAnNiu { width: 100%; padding: 14px 0; font-size: 15px; font-weight: 700; }
.anQuanTiShi { display: flex; align-items: center; justify-content: center; gap: 4px; margin: 12px 0 0; font-size: 11.5px; color: var(--wenZiSan); }
.anQuanTiShi .el-icon { color: var(--qingSe); }

@media (max-width: 900px) {
  .shouYinBu { flex-direction: column; }
  .fangShiKa { width: 100%; }
}
</style>
