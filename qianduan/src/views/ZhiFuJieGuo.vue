<!-- 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24 -->
<template>
  <div class="zhiFuJieGuo">
    <div class="rongQi">
      <el-card class="kePian jieGuoKa" v-loading="chaXunZhong">
        <div v-if="yiZhiFu" class="jieGuoQu">
          <el-icon class="chengGongTu"><CircleCheckFilled /></el-icon>
          <h2>支付成功</h2>
          <p class="fuWen">钱款已进入支付宝担保账户，卖家会尽快发货</p>
          <div v-if="dingDan" class="jinE">￥{{ dingDan.jiaGe }}</div>
          <div v-if="dingDan" class="dingDanHao">订单号：SP{{ dingDan.id }}</div>
          <div class="anNiuHang">
            <el-button type="primary" round @click="luYou.push('/dingdan')">查看我的订单</el-button>
            <el-button round @click="luYou.push('/shouye')">继续逛逛</el-button>
          </div>
        </div>
        <div v-else class="jieGuoQu">
          <el-icon class="shiBaiTu"><WarningFilled /></el-icon>
          <h2>支付未完成</h2>
          <p class="fuWen">{{ xinXi || '暂未查询到支付记录，如果已扣款请稍等几秒再查询' }}</p>
          <div class="anNiuHang">
            <el-button type="primary" round :loading="chaXunZhong" @click="chaXun">重新查询</el-button>
            <el-button round @click="luYou.push('/zhifu/' + dingDanId)">返回收银台</el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const luYouXin = useRoute()

const dingDanId = ref(luYouXin.query.dingDanId)
const dingDan = ref(null)
const yiZhiFu = ref(false)
const xinXi = ref('')
const chaXunZhong = ref(false)

const chaXun = async () => {
  if (!dingDanId.value) {
    luYou.push('/dingdan')
    return
  }
  chaXunZhong.value = true
  try {
    const res = await jieKou.zhiFuChaXun(dingDanId.value)
    if (res.data.chengGong) {
      yiZhiFu.value = !!res.data.yiZhiFu
      dingDan.value = res.data.dingDan || null
      xinXi.value = res.data.xinXi || ''
      if (yiZhiFu.value) ElMessage.success('支付成功')
    } else {
      xinXi.value = res.data.xinXi || '查询失败'
    }
  } catch (e) {
    xinXi.value = '查询支付结果失败，请稍后重试'
  } finally {
    chaXunZhong.value = false
  }
}

onMounted(() => {
  if (!localStorage.getItem('yongHu')) {
    luYou.push('/denglu')
    return
  }
  chaXun()
})
</script>

<style scoped>
.zhiFuJieGuo { padding: 40px 0 60px; }
.jieGuoKa { max-width: 560px; margin: 0 auto; padding: 20px 0; }
.jieGuoQu { text-align: center; padding: 16px 0 8px; }
.chengGongTu { font-size: 68px; color: #22c55e; }
.shiBaiTu { font-size: 68px; color: var(--chengSe); }
.jieGuoQu h2 { margin: 14px 0 6px; font-size: 22px; color: var(--wenZi); }
.fuWen { margin: 0; font-size: 13.5px; color: var(--wenZiSan); }
.jinE { margin-top: 14px; font-size: 30px; font-weight: 800; color: var(--hongSe); }
.dingDanHao { margin-top: 6px; font-size: 12.5px; color: var(--wenZiSan); }
.anNiuHang { display: flex; justify-content: center; gap: 12px; margin-top: 22px; }
</style>
