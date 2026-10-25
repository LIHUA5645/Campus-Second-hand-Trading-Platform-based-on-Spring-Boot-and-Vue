<!-- 作者：肖沐樑 QQ：2047419378  完成时间：2026，10，24 -->
<template>
  <div class="woDeShangPin">
    <div class="rongQi">
      <div class="touBu">
        <div>
          <h2 class="kuaiBiaoTi"><el-icon><Goods /></el-icon> 我发布的商品</h2>
          <p class="fuBiaoTi">在这里编辑、上下架或删除自己发布的闲置</p>
        </div>
        <el-button type="primary" round @click="luYou.push('/fabu')"><el-icon><Plus /></el-icon> 发布新商品</el-button>
      </div>

      <div class="tongJiHang">
        <div class="tongJiKa"><b>{{ shangPinLieBiao.length }}</b><span>已发布</span></div>
        <div class="tongJiKa"><b>{{ zaiShouShu }}</b><span>在售中</span></div>
        <div class="tongJiKa"><b>{{ yiXiaJiaShu }}</b><span>已下架</span></div>
      </div>

      <el-card class="kePian">
        <el-table :data="shangPinLieBiao" style="width: 100%" v-if="shangPinLieBiao.length">
          <el-table-column label="图片" width="86">
            <template #default="{ row }">
              <div class="lieBiaoTu">
                <img v-if="row.tuPian" :src="row.tuPian" alt="商品图" />
                <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="biaoTi" label="标题" min-width="220" show-overflow-tooltip />
          <el-table-column label="价格" width="110">
            <template #default="{ row }"><span class="jiaGe">￥{{ row.jiaGe }}</span></template>
          </el-table-column>
          <el-table-column label="状态" width="110">
            <template #default="{ row }">
              <el-tag v-if="row.zhuangTai === 0" type="success" effect="light" round>在售</el-tag>
              <el-tag v-else-if="row.zhuangTai === 1" type="warning" effect="light" round>已售</el-tag>
              <el-tag v-else type="info" effect="light" round>已下架</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="发布时间" width="170">
            <template #default="{ row }">{{ shiJian(row.fabuShiJian) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="320">
            <template #default="{ row }">
              <el-button size="small" type="primary" plain round @click="daKaiBianJi(row)">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button v-if="row.zhuangTai === 0" size="small" type="warning" plain round @click="xiaJia(row.id)">下架</el-button>
              <el-button v-else-if="row.zhuangTai === 2" size="small" type="success" plain round @click="shangJia(row.id)">上架</el-button>
              <el-button size="small" plain round @click="luYou.push('/shangpin/' + row.id)">查看</el-button>
              <el-button size="small" type="danger" plain round @click="shanChu(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="还没有发布商品，点右上角「发布新商品」开始吧～" />
      </el-card>
    </div>

    <el-dialog v-model="shangPinDuiHua" title="编辑商品" width="620px" align-center>
      <el-form label-width="88px">
        <el-form-item label="商品标题">
          <el-input v-model="bianJiShangPin.biaoTi" maxlength="60" show-word-limit placeholder="商品标题" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="bianJiShangPin.fenLeiId" placeholder="请选择分类" style="width:100%">
            <el-option v-for="f in fenLeiLieBiao" :key="f.id" :label="f.mingCheng" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出售价格">
          <el-input-number v-model="bianJiShangPin.jiaGe" :min="0" :precision="2" :step="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="bianJiShangPin.miaoShu" type="textarea" :rows="3" placeholder="描述成色、转手原因等" />
        </el-form-item>
        <el-form-item label="商品图片">
          <div class="bianJiTuPian">
            <div class="bianJiTu" v-loading="bianJiShangChuanZhong" @click="xuanBianJiTu">
              <img v-if="bianJiShangPin.tuPian" :src="bianJiShangPin.tuPian" alt="预览" />
              <div v-else class="bianJiTuZhan"><el-icon><Plus /></el-icon><span>点击上传</span></div>
            </div>
            <el-input v-model="bianJiShangPin.tuPian" placeholder="图片地址（上传后自动填入）" />
          </div>
          <input ref="bianJiWenJianRef" type="file" accept="image/*" class="yinCangWenJian" @change="bianJiWenJianBianHua" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shangPinDuiHua = false">取消</el-button>
        <el-button type="primary" :loading="bianJiBaoCunZhong" @click="baoCunShangPin">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage, ElMessageBox } from 'element-plus'

const luYou = useRouter()
const shangPinLieBiao = ref([])
const fenLeiLieBiao = ref([])
const shangPinDuiHua = ref(false)
const bianJiShangPin = ref({})
const bianJiWenJianRef = ref(null)
const bianJiShangChuanZhong = ref(false)
const bianJiBaoCunZhong = ref(false)

const zaiShouShu = computed(() => shangPinLieBiao.value.filter(s => s.zhuangTai === 0).length)
const yiXiaJiaShu = computed(() => shangPinLieBiao.value.filter(s => s.zhuangTai === 2).length)

const huoQu = () => {
  const y = localStorage.getItem('yongHu')
  if (!y) { luYou.push('/denglu'); return null }
  return JSON.parse(y)
}

const shiJian = (zhi) => {
  if (!zhi) return '—'
  const d = new Date(zhi)
  if (isNaN(d.getTime())) return String(zhi)
  const buLing = (n) => (n < 10 ? '0' + n : '' + n)
  return d.getFullYear() + '-' + buLing(d.getMonth() + 1) + '-' + buLing(d.getDate())
    + ' ' + buLing(d.getHours()) + ':' + buLing(d.getMinutes())
}

const jiaZai = async () => {
  const y = huoQu()
  if (!y) return
  const res = await jieKou.woDeShangPin(y.id)
  if (res.data.chengGong) shangPinLieBiao.value = res.data.lieBiao || []
}

const xiaJia = async (id) => {
  const res = await jieKou.xiajia(id)
  if (res.data.chengGong) { ElMessage.success('已下架'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}

const shangJia = async (id) => {
  const res = await jieKou.shangjia(id)
  if (res.data.chengGong) { ElMessage.success('已重新上架'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}

const shanChu = async (hang) => {
  try {
    await ElMessageBox.confirm('删除后不可恢复，确定删除「' + hang.biaoTi + '」吗？', '删除商品', { type: 'warning' })
  } catch (e) {
    return
  }
  const res = await jieKou.shanChuWoDeShangPin(hang.id)
  if (res.data.chengGong) { ElMessage.success('已删除'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '删除失败')
}

const daKaiBianJi = async (hang) => {
  if (!fenLeiLieBiao.value.length) {
    const res = await jieKou.fenLeiLieBiao()
    if (res.data.chengGong) fenLeiLieBiao.value = res.data.lieBiao || []
  }
  bianJiShangPin.value = { ...hang }
  shangPinDuiHua.value = true
}

const baoCunShangPin = async () => {
  if (!bianJiShangPin.value.biaoTi || !bianJiShangPin.value.biaoTi.trim()) {
    ElMessage.warning('商品标题不能为空')
    return
  }
  bianJiBaoCunZhong.value = true
  try {
    const res = await jieKou.xiugaiShangPin({
      id: bianJiShangPin.value.id,
      biaoTi: bianJiShangPin.value.biaoTi.trim(),
      miaoShu: bianJiShangPin.value.miaoShu,
      jiaGe: bianJiShangPin.value.jiaGe,
      tuPian: bianJiShangPin.value.tuPian,
      fenLeiId: bianJiShangPin.value.fenLeiId
    })
    if (res.data.chengGong) {
      ElMessage.success('商品已更新')
      shangPinDuiHua.value = false
      jiaZai()
    } else {
      ElMessage.error(res.data.xinXi || '保存失败')
    }
  } finally {
    bianJiBaoCunZhong.value = false
  }
}

const xuanBianJiTu = () => {
  if (bianJiShangChuanZhong.value) return
  if (bianJiWenJianRef.value) bianJiWenJianRef.value.click()
}

const bianJiWenJianBianHua = async (shiJianObj) => {
  const wenJian = shiJianObj.target.files && shiJianObj.target.files[0]
  shiJianObj.target.value = ''
  if (!wenJian) return
  if (!/^image\//.test(wenJian.type)) { ElMessage.warning('请选择图片文件'); return }
  if (wenJian.size > 8 * 1024 * 1024) { ElMessage.warning('图片不能超过 8MB'); return }
  const biaoDan = new FormData()
  biaoDan.append('file', wenJian)
  bianJiShangChuanZhong.value = true
  try {
    const res = await jieKou.shangChuanShangPinTuYongHu(biaoDan)
    if (res.data.chengGong) {
      bianJiShangPin.value.tuPian = res.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.xinXi || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败，请检查网络后重试')
  } finally {
    bianJiShangChuanZhong.value = false
  }
}

onMounted(() => {
  if (!huoQu()) return
  jiaZai()
  jieKou.fenLeiLieBiao().then(res => { if (res.data.chengGong) fenLeiLieBiao.value = res.data.lieBiao || [] })
})
</script>

<style scoped>
.woDeShangPin { padding: 24px 0 40px; }
.touBu { display: flex; align-items: flex-end; justify-content: space-between; gap: 16px; margin-bottom: 18px; }
.kuaiBiaoTi { display: flex; align-items: center; gap: 8px; margin: 0; font-size: 22px; color: var(--wenZi); }
.kuaiBiaoTi .el-icon { color: var(--zhuSe); }
.fuBiaoTi { margin: 6px 0 0; font-size: 13px; color: var(--wenZiSan); }

.tongJiHang { display: flex; gap: 16px; margin-bottom: 18px; }
.tongJiKa {
  flex: 1;
  max-width: 220px;
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: 14px;
  padding: 14px 18px;
  display: flex;
  flex-direction: column;
  box-shadow: var(--banYing);
}
.tongJiKa b { font-size: 24px; color: var(--zhuSe); }
.tongJiKa span { margin-top: 2px; font-size: 12.5px; color: var(--wenZiSan); }

.lieBiaoTu { width: 56px; height: 56px; border-radius: 10px; overflow: hidden; background: var(--beiJing); }
.lieBiaoTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.lieBiaoTu .tuPianZhanWei { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: #cdd6e4; background: linear-gradient(135deg, #eef3ff, #e7fbf5); }
.jiaGe { color: var(--hongSe); font-weight: 700; }

.bianJiTuPian { display: flex; align-items: center; gap: 12px; width: 100%; }
.bianJiTu {
  width: 78px;
  height: 78px;
  flex-shrink: 0;
  border-radius: 10px;
  overflow: hidden;
  border: 1px dashed var(--bianKuang);
  background: var(--beiJing);
  cursor: pointer;
  transition: border-color .18s ease;
}
.bianJiTu:hover { border-color: var(--zhuSe); }
.bianJiTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.bianJiTuZhan { width: 100%; height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 2px; color: var(--wenZiSan); font-size: 11.5px; }
.bianJiTuZhan .el-icon { font-size: 18px; color: #9bb4ff; }
.yinCangWenJian { display: none; }
</style>
