<template>
  <el-container class="guanLi" v-if="guanLiYuan.id">
    <el-aside width="220px" class="ceBian">
      <div class="ceBianBiaoTi">管理后台</div>
      <el-menu :default-active="active" @select="qieHuan" class="ceBianCaiDan">
        <el-menu-item index="shouye"><el-icon><DataLine /></el-icon><span>数据概览</span></el-menu-item>
        <el-menu-item index="yonghu"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
        <el-menu-item index="shangpin"><el-icon><Goods /></el-icon><span>商品管理</span></el-menu-item>
        <el-menu-item index="dingdan"><el-icon><Tickets /></el-icon><span>订单管理</span></el-menu-item>
        <el-menu-item index="fenlei"><el-icon><Menu /></el-icon><span>分类管理</span></el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="dingBu">
        <span class="yeMianBiaoTi">{{ yeMianMing }}</span>
        <div class="yongHu">
          <span class="mingZi">{{ guanLiYuan.xingMing || guanLiYuan.yongHuMing }}</span>
          <el-button size="small" @click="tuiChu">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="zhuTi">
        <!-- 数据概览 -->
        <div v-if="active === 'shouye'">
          <div class="tongJiWangGe">
            <div class="tongJiKaPian" v-for="t in tongJiLie" :key="t.biaoQian">
              <div class="shuZi">{{ t.zhi }}</div>
              <div class="biaoQian">{{ t.biaoQian }}</div>
            </div>
          </div>
          <p class="wenXin">这里汇总平台的用户、商品与订单核心数据，便于快速掌握运营情况。</p>
        </div>

        <!-- 用户管理 -->
        <div v-else-if="active === 'yonghu'">
          <div class="gongJu">
            <el-input v-model="keyword" placeholder="搜索用户名 / 昵称" clearable style="width: 240px" @keyup.enter="chaZhao" @clear="chaZhao">
              <template #prefix><el-icon><Search /></el-icon></template>
            </el-input>
            <el-button type="primary" @click="chaZhao">查询</el-button>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="yongHuMing" label="用户名" />
            <el-table-column prop="xingMing" label="昵称" />
            <el-table-column prop="shouJi" label="手机" />
            <el-table-column prop="xueHao" label="学号" />
            <el-table-column label="管理员" width="90">
              <template #default="{ row }">
                <el-tag :type="row.guanLiYuan ? 'warning' : 'info'" size="small">{{ row.guanLiYuan ? '是' : '否' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="注册时间">
              <template #default="{ row }">{{ shiJian(row.zhuCeShiJian) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="danger" link size="small" @click="shanChuYongHu(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="fenYe" v-if="total > meiYe">
            <el-pagination layout="prev, pager, next" :total="total" :page-size="meiYe" :current-page="page + 1" @current-change="fanYe" />
          </div>
        </div>

        <!-- 商品管理 -->
        <div v-else-if="active === 'shangpin'">
          <div class="gongJu">
            <el-input v-model="keyword" placeholder="搜索商品标题" clearable style="width: 240px" @keyup.enter="chaZhao" @clear="chaZhao">
              <template #prefix><el-icon><Search /></el-icon></template>
            </el-input>
            <el-button type="primary" @click="chaZhao">查询</el-button>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="biaoTi" label="标题" show-overflow-tooltip />
            <el-table-column label="价格" width="100">
              <template #default="{ row }">￥{{ row.jiaGe }}</template>
            </el-table-column>
            <el-table-column label="分类" width="110">
              <template #default="{ row }">{{ fenLeiMing(row.fenLeiId) }}</template>
            </el-table-column>
            <el-table-column prop="yongHuMing" label="发布者" width="120" />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.zhuangTai === 0 ? 'success' : row.zhuangTai === 1 ? 'primary' : 'info'" size="small">{{ shangPinZt[row.zhuangTai] }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发布时间">
              <template #default="{ row }">{{ shiJian(row.fabuShiJian) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="130" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.zhuangTai === 0" type="warning" link size="small" @click="xiaJia(row.id)">下架</el-button>
                <el-button type="danger" link size="small" @click="shanChuShangPin(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="fenYe" v-if="total > meiYe">
            <el-pagination layout="prev, pager, next" :total="total" :page-size="meiYe" :current-page="page + 1" @current-change="fanYe" />
          </div>
        </div>

        <!-- 订单管理 -->
        <div v-else-if="active === 'dingdan'">
          <div class="gongJu">
            <el-select v-model="dingDanZt" placeholder="全部状态" clearable style="width: 160px" @change="chaZhao">
              <el-option label="待发货" :value="0" />
              <el-option label="待收货" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已取消" :value="3" />
            </el-select>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="shangPinBiaoTi" label="商品" show-overflow-tooltip />
            <el-table-column label="金额" width="100">
              <template #default="{ row }">￥{{ row.jiaGe }}</template>
            </el-table-column>
            <el-table-column prop="maiJiaMing" label="买家" width="110" />
            <el-table-column prop="maiJiaShouJi" label="联系电话" width="130" />
            <el-table-column prop="diZhi" label="地址" show-overflow-tooltip />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.zhuangTai === 2 ? 'success' : row.zhuangTai === 3 ? 'info' : 'warning'" size="small">{{ dingDanZtWen[row.zhuangTai] }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="下单时间">
              <template #default="{ row }">{{ shiJian(row.xiaDanShiJian) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="170" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.zhuangTai === 0" type="primary" link size="small" @click="gengXinDingDan(row.id, 1)">发货</el-button>
                <el-button v-if="row.zhuangTai === 1" type="success" link size="small" @click="gengXinDingDan(row.id, 2)">完成</el-button>
                <el-button v-if="row.zhuangTai === 0 || row.zhuangTai === 1" type="danger" link size="small" @click="gengXinDingDan(row.id, 3)">取消</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="fenYe" v-if="total > meiYe">
            <el-pagination layout="prev, pager, next" :total="total" :page-size="meiYe" :current-page="page + 1" @current-change="fanYe" />
          </div>
        </div>

        <!-- 分类管理 -->
        <div v-else-if="active === 'fenlei'">
          <div class="gongJu">
            <el-input v-model="xinFenLei" placeholder="新分类名称" style="width: 220px" @keyup.enter="tianJiaFenLei" />
            <el-button type="primary" @click="tianJiaFenLei">添加分类</el-button>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column prop="id" label="ID" width="90" />
            <el-table-column prop="mingCheng" label="分类名称" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button type="danger" link size="small" @click="shanChuFenLei(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage, ElMessageBox } from 'element-plus'

const luYou = useRouter()
const guanLiYuan = ref(JSON.parse(localStorage.getItem('guanLiYuan') || '{}'))

const active = ref('shouye')
const yeMianMing = computed(() => ({
  shouye: '数据概览', yonghu: '用户管理', shangpin: '商品管理', dingdan: '订单管理', fenlei: '分类管理'
}[active.value]))

const meiYe = 10
const page = ref(0)
const total = ref(0)
const keyword = ref('')
const dingDanZt = ref(null)
const lieBiao = ref([])
const fenLeiLie = ref([])
const xinFenLei = ref('')

const shangPinZt = { 0: '在售', 1: '已售', 2: '已下架' }
const dingDanZtWen = { 0: '待发货', 1: '待收货', 2: '已完成', 3: '已取消' }

const tongJi = ref({})
const tongJiLie = computed(() => [
  { biaoQian: '用户总数', zhi: tongJi.value.yongHuShu ?? '—' },
  { biaoQian: '商品总数', zhi: tongJi.value.shangPinShu ?? '—' },
  { biaoQian: '在售商品', zhi: tongJi.value.zaiShouShu ?? '—' },
  { biaoQian: '订单总数', zhi: tongJi.value.dingDanShu ?? '—' }
])

const shiJian = (v) => {
  if (!v) return '-'
  const d = new Date(v)
  if (isNaN(d.getTime())) return v
  return d.toLocaleString('zh-CN', { hour12: false })
}
const fenLeiMing = (id) => {
  const f = fenLeiLie.value.find(x => x.id === id)
  return f ? f.mingCheng : '—'
}

const loadTongJi = async () => {
  const res = await jieKou.adminTongJi()
  if (res.data.chengGong) tongJi.value = res.data
}

const loadLieBiao = async () => {
  let res
  if (active.value === 'yonghu') {
    res = await jieKou.adminYongHu({ yeMa: page.value, meiYe, guanJianCi: keyword.value })
  } else if (active.value === 'shangpin') {
    res = await jieKou.adminShangPin({ yeMa: page.value, meiYe, guanJianCi: keyword.value })
  } else if (active.value === 'dingdan') {
    res = await jieKou.adminDingDan({ yeMa: page.value, meiYe, zhuangTai: dingDanZt.value })
  } else if (active.value === 'fenlei') {
    res = await jieKou.adminFenLei()
  }
  if (res && res.data.chengGong) {
    lieBiao.value = res.data.lieBiao || []
    total.value = res.data.zongShu || lieBiao.value.length
  }
}

const qieHuan = (val) => {
  active.value = val
  page.value = 0
  keyword.value = ''
  dingDanZt.value = null
  if (val === 'shouye') loadTongJi()
  else loadLieBiao()
}
const chaZhao = () => { page.value = 0; loadLieBiao() }
const fanYe = (p) => { page.value = p - 1; loadLieBiao() }

const shanChuYongHu = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuYongHu(id)
    if (res.data.chengGong) { ElMessage.success('已删除'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '删除失败')
  } catch (e) { /* 取消 */ }
}
const xiaJia = async (id) => {
  const res = await jieKou.adminXiaJiaShangPin(id)
  if (res.data.chengGong) { ElMessage.success('已下架'); loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}
const shanChuShangPin = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？此操作不可恢复。', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuShangPin(id)
    if (res.data.chengGong) { ElMessage.success('已删除'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '删除失败')
  } catch (e) { /* 取消 */ }
}
const gengXinDingDan = async (id, zt) => {
  const res = await jieKou.adminDingDanZhuangTai(id, zt)
  if (res.data.chengGong) { ElMessage.success('已更新'); loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '更新失败')
}
const tianJiaFenLei = async () => {
  if (!xinFenLei.value.trim()) { ElMessage.warning('请输入分类名称'); return }
  const res = await jieKou.adminTianJiaFenLei(xinFenLei.value.trim())
  if (res.data.chengGong) { ElMessage.success('已添加'); xinFenLei.value = ''; loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '添加失败')
}
const shanChuFenLei = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该分类吗？', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuFenLei(id)
    if (res.data.chengGong) { ElMessage.success('已删除'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '删除失败')
  } catch (e) { /* 取消 */ }
}

const tuiChu = () => {
  localStorage.removeItem('guanLiYuan')
  luYou.push('/admin/login')
}

onMounted(() => {
  if (!guanLiYuan.value.id) { luYou.push('/admin/login'); return }
  loadTongJi()
  jieKou.adminFenLei().then(res => { if (res.data.chengGong) fenLeiLie.value = res.data.lieBiao })
})
</script>

<style scoped>
.guanLi { min-height: 100vh; }
.ceBian { background: #1f2733; color: #fff; }
.ceBianBiaoTi { font-size: 18px; font-weight: 800; text-align: center; padding: 20px 0; color: #fff; letter-spacing: 1px; }
.ceBianCaiDan { background: transparent; border-right: none; }
.ceBianCaiDan :deep(.el-menu-item) { color: #c5ccd6; }
.ceBianCaiDan :deep(.el-menu-item.is-active) { background: #2f6bff; color: #fff; border-radius: 10px; margin: 4px 10px; }
.ceBianCaiDan :deep(.el-menu-item:hover) { background: rgba(255, 255, 255, .08); color: #fff; }

.dingBu {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid var(--bianKuang);
  box-shadow: var(--banYing);
}
.yeMianBiaoTi { font-size: 18px; font-weight: 700; color: var(--wenZi); }
.yongHu { display: flex; align-items: center; gap: 12px; }
.yongHu .mingZi { font-size: 14px; color: var(--wenZiEr); }

.zhuTi { background: var(--beiJing); padding: 24px; }
.gongJu { display: flex; gap: 12px; margin-bottom: 16px; }

.tongJiWangGe { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 18px; }
.tongJiKaPian {
  background: #fff;
  border-radius: var(--yuanJiao);
  padding: 24px;
  box-shadow: var(--banYing);
  border: 1px solid var(--bianKuang);
}
.tongJiKaPian .shuZi { font-size: 34px; font-weight: 800; color: var(--zhuSe); }
.tongJiKaPian .biaoQian { font-size: 14px; color: var(--wenZiEr); margin-top: 6px; }
.wenXin { color: var(--wenZiSan); font-size: 13px; margin-top: 18px; }

.fenYe { display: flex; justify-content: center; margin-top: 20px; }
</style>
