<!-- 作者：肖沐樑 QQ：2047419378 -->
<template>
  <div class="dingDan">
    <div class="rongQi">
      <h2 class="kuaiBiaoTi"><el-icon><List /></el-icon> 我的订单</h2>

      <el-card class="kePian">
        <el-tabs v-model="qieHuan">
          <el-tab-pane label="我买的" name="maiJia">
            <div v-for="d in dingDanLieBiao" :key="d.id" class="hangMu">
              <div class="tuPianQu">
                <img v-if="d.tuPian" :src="d.tuPian" alt="商品图" />
                <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
              </div>
              <div class="xinXi">
                <div class="biaoTi">{{ d.shangPinBiaoTi }}</div>
                <div class="fuXinXi">收货地址：{{ d.diZhi || '—' }}</div>
                <div v-if="d.liuYan" class="fuXinXi">留言：{{ d.liuYan }}</div>
                <div class="jiaGe">￥{{ d.jiaGe }}</div>
              </div>
              <div class="zhuangTaiQu">
                <el-tag :type="zhuangTaiYanSe(d)" effect="light" round>{{ zhuangTaiWenBen(d) }}</el-tag>
                <div class="caoZuo">
                  <el-button v-if="d.zhiFuZhuangTai === 0 && d.zhuangTai === 0" size="small" type="danger" round @click="quZhiFu(d.id)">去支付</el-button>
                  <el-button v-if="d.zhuangTai === 1" size="small" type="success" round @click="sheZhi(d.id, 2)">确认收货</el-button>
                  <el-button v-if="d.zhuangTai === 0" size="small" plain round @click="sheZhi(d.id, 3)">取消订单</el-button>
                </div>
              </div>
            </div>
            <el-empty v-if="dingDanLieBiao.length === 0" description="还没有购买记录" />
          </el-tab-pane>

          <el-tab-pane label="我卖出的" name="maiJiaChu">
            <div v-for="d in dingDanLieBiao" :key="d.id" class="hangMu">
              <div class="tuPianQu">
                <img v-if="d.tuPian" :src="d.tuPian" alt="商品图" />
                <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
              </div>
              <div class="xinXi">
                <div class="biaoTi">{{ d.shangPinBiaoTi }}</div>
                <div class="fuXinXi">买家：{{ d.maiJiaMing || '—' }} / {{ d.maiJiaShouJi || '' }}</div>
                <div class="fuXinXi">收货地址：{{ d.diZhi || '—' }}</div>
                <div class="jiaGe">￥{{ d.jiaGe }}</div>
              </div>
              <div class="zhuangTaiQu">
                <el-tag :type="zhuangTaiYanSe(d)" effect="light" round>{{ zhuangTaiWenBen(d) }}</el-tag>
                <div class="caoZuo">
                  <el-button v-if="d.zhuangTai === 0" size="small" type="primary" round @click="sheZhi(d.id, 1)">发货</el-button>
                  <el-button v-if="d.zhiFuZhuangTai === 0 && d.zhiFuFangShi === 'huodao' && d.zhuangTai !== 3" size="small" type="warning" round @click="queRenShouKuan(d.id)">确认收款</el-button>
                </div>
              </div>
            </div>
            <el-empty v-if="dingDanLieBiao.length === 0" description="还没有卖出记录" />
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const qieHuan = ref('maiJia')
const dingDanLieBiao = ref([])

const yongHu = () => {
  const y = localStorage.getItem('yongHu')
  if (!y) { luYou.push('/denglu'); return null }
  return JSON.parse(y)
}

const jiaZai = async () => {
  const y = yongHu()
  if (!y) return
  const jiaoSe = qieHuan.value === 'maiJia' ? 'maiJia' : 'chushou'
  const res = await jieKou.woDeDingDan(y.id, jiaoSe)
  if (res.data.chengGong) dingDanLieBiao.value = res.data.lieBiao
}

// 状态要叠加付款情况：未付款时优先显示「待付款」
const zhuangTaiWenBen = (d) => {
  if (d.zhuangTai === 3) return '已取消'
  if (d.zhuangTai === 2) return '已完成'
  if (d.zhiFuZhuangTai === 0) return d.zhiFuFangShi === 'huodao' ? '待当面付款' : '待付款'
  return ['待发货', '待收货', '已完成', '已取消'][d.zhuangTai] || '未知'
}
const zhuangTaiYanSe = (d) => {
  if (d.zhuangTai === 3) return 'info'
  if (d.zhuangTai === 2) return 'success'
  if (d.zhiFuZhuangTai === 0) return 'danger'
  return ['warning', 'primary', 'success', 'info'][d.zhuangTai] || 'info'
}

const sheZhi = async (id, z) => {
  const res = await jieKou.dingDanZhuangTai(id, z)
  if (res.data.chengGong) { ElMessage.success('操作成功'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}

const quZhiFu = (id) => {
  luYou.push('/zhifu/' + id)
}

const queRenShouKuan = async (id) => {
  const res = await jieKou.zhiFuShouKuan(id)
  if (res.data.chengGong) { ElMessage.success('已确认收款'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}

watch(qieHuan, jiaZai)
onMounted(jiaZai)
</script>

<style scoped>
.kuaiBiaoTi { display: flex; align-items: center; gap: 8px; font-size: 22px; margin: 24px 0 16px; color: var(--wenZi); }
.kuaiBiaoTi .el-icon { color: var(--zhuSe); }
.hangMu { display: flex; align-items: center; gap: 18px; padding: 16px 0; border-bottom: 1px solid var(--bianKuang); }
.tuPianQu { width: 84px; height: 84px; border-radius: 12px; overflow: hidden; flex-shrink: 0; }
.tuPianQu img { width: 100%; height: 100%; object-fit: cover; }
.tuPianQu .tuPianZhanWei { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 32px; color: #cdd6e4; background: linear-gradient(135deg, #eef3ff, #e7fbf5); }
.xinXi { flex: 1; }
.biaoTi { font-weight: 600; color: var(--wenZi); font-size: 15px; }
.fuXinXi { color: var(--wenZiSan); font-size: 13px; margin-top: 4px; }
.jiaGe { color: var(--hongSe); font-weight: 700; margin-top: 6px; }
.zhuangTaiQu { text-align: right; min-width: 120px; }
.caoZuo { margin-top: 10px; }
</style>
