<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div class="geRen">
    <div class="rongQi">
      <el-card class="kePian touBuKaPian" v-if="yongHu">
        <el-upload
          class="touXiangShangChuan"
          :show-file-list="false"
          :http-request="shangChuanWoDeTouXiang"
          accept="image/png,image/jpeg,image/webp,image/gif"
        >
          <div class="touXiangKuang" title="点击更换头像">
            <el-avatar :size="76" class="touXiang">
              <img v-if="yongHu.touXiang" :src="yongHu.touXiang" alt="我的头像" class="touXiangTu" />
              <template v-else>{{ (yongHu.xingMing || yongHu.yongHuMing || '我').slice(0, 1) }}</template>
            </el-avatar>
            <div class="touXiangZheZhao">
              <el-icon><Camera /></el-icon>
              <span>更换头像</span>
            </div>
          </div>
        </el-upload>
        <div class="touBuXinXi">
          <h2>{{ yongHu.xingMing || yongHu.yongHuMing }}</h2>
          <p>@{{ yongHu.yongHuMing }} ｜ 校园二手玩家</p>
        </div>
        <div class="shuJu">
          <div><b>{{ shangPinLieBiao.length }}</b><span>已发布</span></div>
          <div><b>{{ zaiShouShu }}</b><span>在售中</span></div>
        </div>
        <el-button type="primary" round @click="duiHua = true"><el-icon><Edit /></el-icon> 编辑资料</el-button>
      </el-card>

      <el-card class="kePian">
        <div class="biaoTiHang">
          <h3><el-icon><Goods /></el-icon> 我发布的商品</h3>
          <el-button type="primary" round @click="luYou.push('/fabu')"><el-icon><Plus /></el-icon> 发布新商品</el-button>
        </div>
        <el-table :data="shangPinLieBiao" style="width: 100%" v-if="shangPinLieBiao.length">
          <el-table-column prop="biaoTi" label="标题" min-width="200" />
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
          <el-table-column label="操作" width="170">
            <template #default="{ row }">
              <el-button v-if="row.zhuangTai === 0" size="small" type="danger" plain round @click="xiaJia(row.id)">下架</el-button>
              <el-button size="small" plain round @click="luYou.push('/shangpin/' + row.id)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="还没有发布商品，去发布第一个吧～" />
      </el-card>
    </div>

    <el-dialog v-model="duiHua" title="编辑资料" width="440px" align-center>
      <el-form label-width="80px">
        <el-form-item label="昵称"><el-input v-model="bianJi.xingMing" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="bianJi.shouJi" /></el-form-item>
        <el-form-item label="学号"><el-input v-model="bianJi.xueHao" /></el-form-item>
      </el-form>
      <p class="miMaTiShi">修改密码请退出后在登录页使用「找回密码」，通过手机号 + 学号校验重置。</p>
      <template #footer>
        <el-button @click="duiHua = false">取消</el-button>
        <el-button type="primary" @click="baoCun">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const yongHu = ref(null)
const shangPinLieBiao = ref([])
const duiHua = ref(false)
const bianJi = ref({ xingMing: '', shouJi: '', xueHao: '' })

const zaiShouShu = computed(() => shangPinLieBiao.value.filter(s => s.zhuangTai === 0).length)

const huoQu = () => {
  const y = localStorage.getItem('yongHu')
  if (!y) { luYou.push('/denglu'); return null }
  return JSON.parse(y)
}

const jiaZai = async () => {
  const y = huoQu()
  if (!y) return
  const g = await jieKou.geren(y.id)
  if (g.data.chengGong) yongHu.value = g.data.yongHu
  const sp = await jieKou.woDeShangPin(y.id)
  if (sp.data.chengGong) shangPinLieBiao.value = sp.data.lieBiao
}

const xiaJia = async (id) => {
  const res = await jieKou.xiajia(id)
  if (res.data.chengGong) { ElMessage.success('已下架'); jiaZai() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}

// 上传个人头像：先传图，再把地址写进用户资料
const shangChuanWoDeTouXiang = (option) => {
  const biaoDan = new FormData()
  biaoDan.append('file', option.file)
  jieKou.shangChuanGeRenTouXiang(biaoDan).then(async (res) => {
    if (!res.data.chengGong) {
      ElMessage.error(res.data.xinXi || '上传失败')
      return
    }
    const y = huoQu()
    if (!y) return
    const r = await jieKou.xiugaiYongHu({ id: y.id, touXiang: res.data.url })
    if (r.data.chengGong) {
      localStorage.setItem('yongHu', JSON.stringify(r.data.yongHu))
      yongHu.value = r.data.yongHu
      ElMessage.success('头像已更新')
    } else {
      ElMessage.error(r.data.xinXi || '头像保存失败')
    }
  }).catch(() => ElMessage.error('上传失败，请确认后端已启动'))
}

const baoCun = async () => {
  const y = huoQu()
  const shuJu = { id: y.id, xingMing: bianJi.value.xingMing, shouJi: bianJi.value.shouJi, xueHao: bianJi.value.xueHao }
  const res = await jieKou.xiugaiYongHu(shuJu)
  if (res.data.chengGong) {
    localStorage.setItem('yongHu', JSON.stringify(res.data.yongHu))
    ElMessage.success('资料已更新')
    duiHua.value = false
    jiaZai()
  } else {
    ElMessage.error(res.data.xinXi || '保存失败')
  }
}

onMounted(() => {
  const y = huoQu()
  if (y) { bianJi.value.xingMing = y.xingMing || ''; bianJi.value.shouJi = y.shouJi || ''; bianJi.value.xueHao = y.xueHao || '' }
  jiaZai()
})
</script>

<style scoped>
.geRen { padding: 24px 0 40px; }
.touBuKaPian { display: flex; align-items: center; gap: 18px; margin-bottom: 18px; padding: 24px; }
.touXiang { background: var(--zhuTiDu); color: #fff; font-weight: 700; font-size: 32px; }
.touXiangShangChuan { display: inline-block; }
.touXiangShangChuan :deep(.el-upload) { border-radius: 50%; outline: none; }
.touXiangKuang { position: relative; border-radius: 50%; cursor: pointer; }
.touXiangKuang .touXiang { box-shadow: 0 8px 20px rgba(47, 107, 255, .28); }
.touXiangTu { width: 100%; height: 100%; object-fit: cover; display: block; }
.touXiangZheZhao {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  background: rgba(15, 23, 42, .55);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  font-size: 11px;
  opacity: 0;
  transition: opacity .18s ease;
}
.touXiangZheZhao .el-icon { font-size: 18px; }
.touXiangKuang:hover .touXiangZheZhao { opacity: 1; }
.touBuXinXi { flex: 1; }
.touBuXinXi h2 { margin: 0 0 4px; font-size: 22px; color: var(--wenZi); }
.touBuXinXi p { margin: 0; color: var(--wenZiSan); font-size: 13px; }
.shuJu { display: flex; gap: 28px; margin-right: 8px; }
.shuJu div { display: flex; flex-direction: column; align-items: center; }
.shuJu b { font-size: 22px; color: var(--zhuSe); }
.shuJu span { font-size: 12px; color: var(--wenZiSan); }
.biaoTiHang { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; }
.biaoTiHang h3 { display: flex; align-items: center; gap: 8px; margin: 0; font-size: 17px; color: var(--wenZi); }
.biaoTiHang h3 .el-icon { color: var(--zhuSe); }
.jiaGe { color: var(--hongSe); font-weight: 700; }
.miMaTiShi { margin: 4px 0 0; font-size: 12.5px; color: var(--wenZiSan); }
</style>
