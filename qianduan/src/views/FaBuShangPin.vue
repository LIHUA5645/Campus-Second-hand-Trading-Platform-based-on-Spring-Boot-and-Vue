<!-- 作者：肖沐樑 QQ：2047419378 -->
<template>
  <div class="faBu">
    <div class="rongQi">
      <el-card class="kePian">
        <div class="biaoTiHang">
          <h2><el-icon><UploadFilled /></el-icon> 发布二手商品</h2>
          <span class="jianJie">填好信息，让你的闲置被同学发现～</span>
        </div>

        <el-form :model="biaoDan" label-width="90px" class="biaoDan">
          <el-row :gutter="24">
            <el-col :span="14">
              <el-form-item label="商品标题">
                <el-input v-model="biaoDan.biaoTi" size="large" placeholder="例如：九成新 Kindle 阅读器" />
              </el-form-item>
              <el-form-item label="商品分类">
                <el-select v-model="biaoDan.fenLeiId" size="large" placeholder="请选择分类" style="width:100%">
                  <el-option v-for="f in fenLeiLieBiao" :key="f.id" :label="f.mingCheng" :value="f.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="出售价格">
                <el-input-number v-model="biaoDan.jiaGe" :min="0" :precision="2" :step="1" size="large" controls-position="right" style="width:100%" />
              </el-form-item>
              <el-form-item label="商品描述">
                <el-input v-model="biaoDan.miaoShu" type="textarea" :rows="5" placeholder="描述成色、入手渠道、转手原因等" />
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="商品图片">
                <div class="tuPianShangChuan">
                  <div class="tuPianYuLan" :class="{ youTu: biaoDan.tuPian }" v-loading="shangChuanZhong" @click="xuanTu">
                    <img v-if="biaoDan.tuPian" :src="biaoDan.tuPian" alt="预览" />
                    <div v-else class="tuPianZhanWei">
                      <el-icon><Plus /></el-icon>
                      <span>点击上传商品图</span>
                      <em>支持 jpg / png / webp，最大 8MB</em>
                    </div>
                    <div v-if="biaoDan.tuPian && !shangChuanZhong" class="tuPianZheZhao">
                      <el-button size="small" round @click.stop="xuanTu"><el-icon><RefreshRight /></el-icon> 更换</el-button>
                      <el-button size="small" round plain type="danger" @click.stop="qingChuTu">移除</el-button>
                    </div>
                  </div>
                  <input ref="wenJianRef" type="file" accept="image/*" class="yinCangWenJian" @change="wenJianBianHua" />
                </div>
              </el-form-item>
              <el-form-item label="图片链接">
                <el-input v-model="biaoDan.tuPian" placeholder="也可以粘贴网络图片地址" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" size="large" round class="tiJiao" @click="tiJiao"><el-icon><Check /></el-icon> 立即发布</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const fenLeiLieBiao = ref([])
const biaoDan = ref({ biaoTi: '', fenLeiId: null, jiaGe: 0, tuPian: '', miaoShu: '' })
const wenJianRef = ref(null)
const shangChuanZhong = ref(false)

const yongHu = () => {
  const y = localStorage.getItem('yongHu')
  return y ? JSON.parse(y) : null
}

// 点击预览框选择本地图片
const xuanTu = () => {
  if (shangChuanZhong.value) return
  if (wenJianRef.value) wenJianRef.value.click()
}

const qingChuTu = () => {
  biaoDan.value.tuPian = ''
}

// 选中文件后直接上传，成功后把地址写回表单
const wenJianBianHua = async (shiJian) => {
  const wenJian = shiJian.target.files && shiJian.target.files[0]
  shiJian.target.value = ''
  if (!wenJian) return
  if (!/^image\//.test(wenJian.type)) {
    ElMessage.warning('请选择图片文件')
    return
  }
  if (wenJian.size > 8 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 8MB')
    return
  }
  const biaoDanShu = new FormData()
  biaoDanShu.append('file', wenJian)
  shangChuanZhong.value = true
  try {
    const res = await jieKou.shangChuanShangPinTuYongHu(biaoDanShu)
    if (res.data.chengGong) {
      biaoDan.value.tuPian = res.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.xinXi || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败，请检查网络后重试')
  } finally {
    shangChuanZhong.value = false
  }
}

onMounted(() => {
  if (!yongHu()) { ElMessage.warning('请先登录'); luYou.push('/denglu'); return }
  jieKou.fenLeiLieBiao().then(res => { if (res.data.chengGong) fenLeiLieBiao.value = res.data.lieBiao })
})

const tiJiao = async () => {
  const y = yongHu()
  if (!biaoDan.value.biaoTi || !biaoDan.value.jiaGe) { ElMessage.warning('标题和价格必填'); return }
  const res = await jieKou.fabu({ ...biaoDan.value, yongHuId: y.id })
  if (res.data.chengGong) {
    ElMessage.success('发布成功')
    luYou.push('/shouye')
  } else {
    ElMessage.error(res.data.xinXi || '发布失败')
  }
}
</script>

<style scoped>
.faBu { padding: 24px 0 40px; }
.biaoTiHang { display: flex; align-items: baseline; gap: 12px; margin-bottom: 22px; border-bottom: 1px solid var(--bianKuang); padding-bottom: 14px; }
.biaoTiHang h2 { display: flex; align-items: center; gap: 8px; margin: 0; font-size: 22px; color: var(--wenZi); }
.biaoTiHang h2 .el-icon { color: var(--zhuSe); }
.jianJie { color: var(--wenZiSan); font-size: 13px; }
.tuPianShangChuan { width: 100%; }
.yinCangWenJian { display: none; }
.tuPianYuLan {
  position: relative;
  width: 100%;
  height: 200px;
  border-radius: 12px;
  overflow: hidden;
  border: 1px dashed var(--bianKuang);
  cursor: pointer;
  background: var(--beiJing);
  transition: border-color .18s ease, box-shadow .18s ease;
}
.tuPianYuLan:hover { border-color: var(--zhuSe); box-shadow: 0 6px 18px rgba(47, 107, 255, .12); }
.tuPianYuLan.youTu { border-style: solid; }
.tuPianYuLan img { width: 100%; height: 100%; object-fit: cover; display: block; }
.tuPianYuLan .tuPianZhanWei { width: 100%; height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 6px; color: var(--wenZiSan); }
.tuPianYuLan .tuPianZhanWei .el-icon { font-size: 34px; color: #9bb4ff; }
.tuPianYuLan .tuPianZhanWei em { font-style: normal; font-size: 11.5px; color: var(--wenZiSan); }
.tuPianZheZhao {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: rgba(15, 23, 42, .45);
  opacity: 0;
  transition: opacity .18s ease;
}
.tuPianYuLan:hover .tuPianZheZhao { opacity: 1; }
.tiJiao { padding: 12px 40px; }
</style>
