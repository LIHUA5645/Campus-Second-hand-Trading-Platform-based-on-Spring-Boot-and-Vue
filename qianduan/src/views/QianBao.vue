<!-- 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，25 -->
<template>
  <div class="qianBao">
    <div class="rongQi">
      <div class="touBu">
        <div>
          <h2 class="kuaiBiaoTi"><el-icon><Wallet /></el-icon> 我的钱包</h2>
          <p class="fuBiaoTi">买家付款先由平台代收，确认收货后计入余额，可随时申请提现</p>
        </div>
        <el-button type="primary" round :disabled="yuE <= 0" @click="daKaiTiXian">
          <el-icon><Money /></el-icon> 申请提现
        </el-button>
      </div>

      <div class="yuEKa">
        <div class="yuEZuo">
          <span class="yuEBiaoQian">可提现余额（元）</span>
          <b class="yuEShu">￥{{ jinEWenBen(yuE) }}</b>
          <div class="yuEFu">
            <span>累计卖出 ￥{{ jinEWenBen(leiJiShouRu) }}</span>
            <i></i>
            <span>累计提现 ￥{{ jinEWenBen(leiJiTiXian) }}</span>
          </div>
        </div>
        <div class="yuEYou">
          <div class="daiDaoZhang">
            <span>待到账（已付款未确认收货）</span>
            <b>￥{{ jinEWenBen(daiDaoZhang) }}</b>
          </div>
          <el-alert
            class="tiShi"
            type="info"
            :closable="false"
            show-icon
            title="买家付款先进入平台账户，确认收货后自动结算到你的余额；提现由管理员审核并线下打款。"
          />
        </div>
      </div>

      <el-tabs v-model="yeQian" class="yeQian">
        <el-tab-pane label="账单流水" name="jilu">
          <el-card class="kePian">
            <el-table :data="jiLu" style="width: 100%" v-if="jiLu.length">
              <el-table-column label="类型" width="130">
                <template #default="{ row }">
                  <el-tag :type="leiXingYanSe(row.leiXing)" effect="light" round>{{ leiXingWenBen(row.leiXing) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="金额" width="140">
                <template #default="{ row }">
                  <span :class="row.jinE >= 0 ? 'shouRu' : 'zhiChu'">
                    {{ row.jinE >= 0 ? '+' : '-' }}￥{{ jinEWenBen(Math.abs(row.jinE)) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="shuoMing" label="说明" min-width="240" show-overflow-tooltip />
              <el-table-column label="变动后余额" width="140">
                <template #default="{ row }">￥{{ jinEWenBen(row.yuE) }}</template>
              </el-table-column>
              <el-table-column label="时间" width="170">
                <template #default="{ row }">{{ shiJian(row.shiJian) }}</template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="还没有账单记录，卖出商品并完成交易后就会有收入啦～" />
          </el-card>
        </el-tab-pane>

        <el-tab-pane :label="'提现记录' + (tiXianJiLu.length ? '（' + tiXianJiLu.length + '）' : '')" name="tixian">
          <el-card class="kePian">
            <el-table :data="tiXianJiLu" style="width: 100%" v-if="tiXianJiLu.length">
              <el-table-column label="金额" width="120">
                <template #default="{ row }"><span class="jiaGe">￥{{ jinEWenBen(row.jinE) }}</span></template>
              </el-table-column>
              <el-table-column label="收款方式" width="120">
                <template #default="{ row }">{{ fangShiWenBen(row.fangShi) }}</template>
              </el-table-column>
              <el-table-column prop="zhangHao" label="收款账号" min-width="180" show-overflow-tooltip />
              <el-table-column prop="xingMing" label="收款人" width="120" />
              <el-table-column label="状态" width="120">
                <template #default="{ row }">
                  <el-tag v-if="row.zhuangTai === 0" type="warning" effect="light" round>待审核</el-tag>
                  <el-tag v-else-if="row.zhuangTai === 1" type="success" effect="light" round>已打款</el-tag>
                  <el-tag v-else type="danger" effect="light" round>已驳回</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="beiZhu" label="管理员备注" min-width="180" show-overflow-tooltip />
              <el-table-column label="申请时间" width="170">
                <template #default="{ row }">{{ shiJian(row.shenQingShiJian) }}</template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="还没有提现记录，余额到账后点右上角「申请提现」" />
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="tiXianDuiHua" title="申请提现" width="520px" align-center>
      <el-alert
        class="tiShi"
        type="warning"
        :closable="false"
        show-icon
        :title="'当前可提现 ￥' + jinEWenBen(yuE) + '，提交后金额会先冻结，管理员审核通过即打款。'"
      />
      <el-form class="tiXianBiaoDan" label-width="110px">
        <el-form-item label="提现金额">
          <el-input-number v-model="tiXian.jinE" :min="0.01" :max="yuE" :precision="2" :step="10" style="width:100%" />
        </el-form-item>
        <el-form-item label="快捷">
          <div class="kuaiJie">
            <el-button size="small" round @click="tiXian.jinE = yuE">全部提现</el-button>
            <el-button size="small" round @click="tiXian.jinE = banShu">提现一半</el-button>
          </div>
        </el-form-item>
        <el-form-item label="收款方式">
          <el-select v-model="tiXian.fangShi" placeholder="请选择收款方式" style="width:100%">
            <el-option label="支付宝" value="alipay" />
            <el-option label="微信" value="weixin" />
            <el-option label="银行卡" value="yinhang" />
          </el-select>
        </el-form-item>
        <el-form-item label="收款账号">
          <el-input v-model="tiXian.zhangHao" :placeholder="zhangHaoTiShi" />
        </el-form-item>
        <el-form-item label="收款人姓名">
          <el-input v-model="tiXian.xingMing" placeholder="请填写与收款账号一致的真实姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="tiXian.shouJi" placeholder="便于管理员核对（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button round @click="tiXianDuiHua = false">取消</el-button>
        <el-button type="primary" round :loading="tiXianZhong" @click="tiJiaoTiXian">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import jieKou from '@/api/jieKou'

const luYou = useRouter()
const yuE = ref(0)
const leiJiShouRu = ref(0)
const leiJiTiXian = ref(0)
const daiDaoZhang = ref(0)
const jiLu = ref([])
const tiXianJiLu = ref([])
const yeQian = ref('jilu')

const tiXianDuiHua = ref(false)
const tiXianZhong = ref(false)
const tiXian = ref({ jinE: 0, fangShi: 'alipay', zhangHao: '', xingMing: '', shouJi: '' })

const banShu = computed(() => Math.floor(yuE.value / 2 * 100) / 100)

const zhangHaoTiShi = computed(() => {
  if (tiXian.value.fangShi === 'weixin') return '请填写微信号'
  if (tiXian.value.fangShi === 'yinhang') return '请填写银行卡号（建议附开户行）'
  return '请填写支付宝账号（手机号或邮箱）'
})

const huoQu = () => {
  const y = localStorage.getItem('yongHu')
  if (!y) { luYou.push('/denglu'); return null }
  return JSON.parse(y)
}

const jinEWenBen = (zhi) => Number(zhi || 0).toFixed(2)

const shiJian = (zhi) => {
  if (!zhi) return '—'
  const d = new Date(zhi)
  if (isNaN(d.getTime())) return String(zhi)
  const buLing = (n) => (n < 10 ? '0' + n : '' + n)
  return d.getFullYear() + '-' + buLing(d.getMonth() + 1) + '-' + buLing(d.getDate())
    + ' ' + buLing(d.getHours()) + ':' + buLing(d.getMinutes())
}

const leiXingWenBen = (leiXing) => {
  if (leiXing === 'shouru') return '卖货收入'
  if (leiXing === 'tixian') return '提现'
  if (leiXing === 'bohui') return '提现退回'
  if (leiXing === 'tuihui') return '订单退款'
  return '其它'
}

const leiXingYanSe = (leiXing) => {
  if (leiXing === 'shouru') return 'success'
  if (leiXing === 'tixian') return 'warning'
  if (leiXing === 'bohui' || leiXing === 'tuihui') return 'info'
  return 'info'
}

const fangShiWenBen = (fangShi) => {
  if (fangShi === 'weixin') return '微信'
  if (fangShi === 'yinhang') return '银行卡'
  return '支付宝'
}

const jiaZai = async () => {
  const res = await jieKou.qianBao()
  if (res.data.chengGong) {
    yuE.value = res.data.yuE || 0
    leiJiShouRu.value = res.data.leiJiShouRu || 0
    leiJiTiXian.value = res.data.leiJiTiXian || 0
    daiDaoZhang.value = res.data.daiDaoZhang || 0
    jiLu.value = res.data.jiLu || []
    tiXianJiLu.value = res.data.tiXianJiLu || []
  } else {
    ElMessage.error(res.data.xinXi || '加载钱包失败')
  }
}

const daKaiTiXian = () => {
  tiXian.value = {
    jinE: Number(yuE.value.toFixed(2)),
    fangShi: 'alipay',
    zhangHao: '',
    xingMing: '',
    shouJi: ''
  }
  tiXianDuiHua.value = true
}

const tiJiaoTiXian = async () => {
  if (!tiXian.value.jinE || tiXian.value.jinE <= 0) {
    ElMessage.warning('请输入提现金额')
    return
  }
  if (tiXian.value.jinE > yuE.value) {
    ElMessage.warning('提现金额不能超过可提现余额')
    return
  }
  if (!tiXian.value.zhangHao || !tiXian.value.zhangHao.trim()) {
    ElMessage.warning('请填写收款账号')
    return
  }
  if (!tiXian.value.xingMing || !tiXian.value.xingMing.trim()) {
    ElMessage.warning('请填写收款人姓名')
    return
  }
  tiXianZhong.value = true
  try {
    const res = await jieKou.shenQingTiXian({
      jinE: tiXian.value.jinE,
      fangShi: tiXian.value.fangShi,
      zhangHao: tiXian.value.zhangHao.trim(),
      xingMing: tiXian.value.xingMing.trim(),
      shouJi: tiXian.value.shouJi
    })
    if (res.data.chengGong) {
      ElMessage.success(res.data.xinXi || '提现申请已提交')
      tiXianDuiHua.value = false
      yeQian.value = 'tixian'
      jiaZai()
    } else {
      ElMessage.error(res.data.xinXi || '提交失败')
    }
  } catch (e) {
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    tiXianZhong.value = false
  }
}

onMounted(() => {
  if (!huoQu()) return
  jiaZai()
})
</script>

<style scoped>
.qianBao { padding: 24px 0 40px; }
.touBu { display: flex; align-items: flex-end; justify-content: space-between; gap: 16px; margin-bottom: 18px; }
.kuaiBiaoTi { display: flex; align-items: center; gap: 8px; margin: 0; font-size: 22px; color: var(--wenZi); }
.kuaiBiaoTi .el-icon { color: var(--zhuSe); }
.fuBiaoTi { margin: 6px 0 0; font-size: 13px; color: var(--wenZiSan); }

.yuEKa {
  display: flex;
  gap: 20px;
  padding: 22px 26px;
  border-radius: 18px;
  background: linear-gradient(135deg, #2f6bff, #15c5b8);
  color: #fff;
  box-shadow: 0 12px 28px rgba(47, 107, 255, .22);
  margin-bottom: 18px;
}
.yuEZuo { flex: 1; min-width: 260px; }
.yuEBiaoQian { font-size: 13px; opacity: .88; }
.yuEShu { display: block; margin-top: 6px; font-size: 34px; line-height: 1.1; letter-spacing: .5px; }
.yuEFu { display: flex; align-items: center; gap: 12px; margin-top: 12px; font-size: 12.5px; opacity: .92; }
.yuEFu i { width: 1px; height: 12px; background: rgba(255, 255, 255, .5); display: inline-block; }

.yuEYou { flex: 1.1; min-width: 260px; display: flex; flex-direction: column; gap: 12px; }
.daiDaoZhang {
  background: rgba(255, 255, 255, .16);
  border: 1px solid rgba(255, 255, 255, .28);
  border-radius: 14px;
  padding: 10px 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
}
.daiDaoZhang b { font-size: 18px; }
.tiShi { margin-bottom: 14px; }

.yeQian :deep(.el-tabs__item) { font-size: 14.5px; }
.shouRu { color: #17b978; font-weight: 700; }
.zhiChu { color: var(--hongSe); font-weight: 700; }
.jiaGe { color: var(--hongSe); font-weight: 700; }

.tiXianBiaoDan { margin-top: 6px; }
.kuaiJie { display: flex; gap: 8px; }

@media (max-width: 880px) {
  .yuEKa { flex-direction: column; }
  .touBu { flex-direction: column; align-items: flex-start; }
}
</style>
