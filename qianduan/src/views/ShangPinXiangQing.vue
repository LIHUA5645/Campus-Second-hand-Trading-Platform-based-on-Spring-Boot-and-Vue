<!-- 作者：肖沐樑 QQ：3387432690 -->
<template>
  <div class="xiangQing">
    <div class="rongQi">
      <el-button text class="fanHui" @click="luYou.push('/shouye')"><el-icon><ArrowLeft /></el-icon> 返回首页</el-button>

      <el-card v-if="shangPin" class="kePian zhuTiKaPian" v-loading="!shangPin">
        <div class="neiRong">
          <div class="tuPianQu">
            <img v-if="shangPin.tuPian" :src="shangPin.tuPian" alt="商品图" />
            <div v-else class="tuPianZhanWei"><el-icon><Picture /></el-icon></div>
          </div>
          <div class="youCe">
            <div class="biaoTiHang">
              <h1>{{ shangPin.biaoTi }}</h1>
              <el-tag v-if="shangPin.zhuangTai === 0" type="success" effect="dark" round>在售</el-tag>
              <el-tag v-else-if="shangPin.zhuangTai === 1" type="warning" effect="dark" round>已售出</el-tag>
              <el-tag v-else type="info" effect="dark" round>已下架</el-tag>
            </div>
            <div class="jiaGe">￥{{ shangPin.jiaGe }}</div>
            <p class="miaoShu">{{ shangPin.miaoShu || '卖家很懒，没有填写描述~' }}</p>

            <div class="maiJiaKaPian">
              <el-avatar :size="40" class="maiJiaTouXiang">{{ (shangPin.yongHuMing || '匿').slice(0, 1) }}</el-avatar>
              <div>
                <div class="maiJiaMing">{{ shangPin.yongHuMing || '匿名用户' }}</div>
                <div class="maiJiaXiaoMing">校园卖家 · 诚信交易</div>
              </div>
            </div>

            <div class="caoZuoHang">
              <el-button v-if="keYiGouMai" type="danger" size="large" round @click="duiHua = true"><el-icon><ShoppingCart /></el-icon> 立即购买</el-button>
              <el-button v-else-if="shangPin.zhuangTai !== 0" size="large" round disabled>该商品暂不可购买</el-button>
              <el-button v-else type="primary" size="large" round @click="luYou.push('/denglu')"><el-icon><User /></el-icon> 登录后购买</el-button>
            </div>
          </div>
        </div>
      </el-card>

      <el-skeleton v-else :rows="8" animated />

      <el-card v-if="xiangGuanLie.length" class="kePian xiangGuanKaPian">
        <template #header>
          <div class="xiangGuanTou">
            <span class="xiangGuanTi"><el-icon><MagicStick /></el-icon> 看了又看</span>
            <span class="xiangGuanFu">协同过滤算出来的相似闲置</span>
          </div>
        </template>
        <div class="xiangGuanLie">
          <div v-for="xg in xiangGuanLie" :key="xg.id" class="xiangGuanXiang" @click="luYou.push('/shangpin/' + xg.id)">
            <div class="xiangGuanTu">
              <img v-if="xg.tuPian" :src="xg.tuPian" :alt="xg.biaoTi" />
              <div v-else class="zhanWei"><el-icon><Picture /></el-icon></div>
            </div>
            <div class="xiangGuanMing">{{ xg.biaoTi }}</div>
            <div class="xiangGuanJia">￥{{ xg.jiaGe }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="duiHua" title="确认下单" width="440px" align-center>
      <el-form label-width="84px">
        <el-form-item label="收货地址">
          <el-input v-model="diZhi" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="留言">
          <el-input v-model="liuYan" type="textarea" :rows="3" placeholder="给卖家留言（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="duiHua = false">取消</el-button>
        <el-button type="primary" @click="xiaDan">提交订单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage } from 'element-plus'

const luYou = useRouter()
const luYouXin = useRoute()
const shangPin = ref(null)
const xiangGuanLie = ref([])
const duiHua = ref(false)
const diZhi = ref('')
const liuYan = ref('')

const dangQianYongHu = () => {
  const y = localStorage.getItem('yongHu')
  return y ? JSON.parse(y) : null
}

const keYiGouMai = computed(() => {
  const y = dangQianYongHu()
  return y && shangPin.value && shangPin.value.zhuangTai === 0 && shangPin.value.yongHuId !== y.id
})

const jiaZai = async () => {
  const res = await jieKou.xiangqing(luYouXin.params.id)
  if (res.data.chengGong) {
    shangPin.value = res.data.shangPin
    jiaZaiXiangGuan(res.data.shangPin.id)
  }
}

const jiaZaiXiangGuan = async (id) => {
  try {
    const res = await jieKou.xiangGuanShangPin(id, 5)
    if (res.data.chengGong) xiangGuanLie.value = res.data.lieBiao || []
  } catch (e) {
    // 推荐失败不影响详情页
  }
}

const xiaDan = async () => {
  if (!diZhi.value) { ElMessage.warning('请填写收货地址'); return }
  const y = dangQianYongHu()
  const res = await jieKou.xiadan({
    shangPinId: shangPin.value.id,
    maiJiaId: y.id,
    diZhi: diZhi.value,
    liuYan: liuYan.value
  })
  if (res.data.chengGong) {
    ElMessage.success('下单成功，等待卖家发货')
    duiHua.value = false
    jiaZai()
  } else {
    ElMessage.error(res.data.xinXi || '下单失败')
  }
}

onMounted(jiaZai)
</script>

<style scoped>
.xiangQing { padding: 24px 0; }
.fanHui { margin-bottom: 12px; }
.zhuTiKaPian { padding: 8px; }
.neiRong { display: flex; gap: 36px; }
.tuPianQu { width: 380px; height: 380px; flex-shrink: 0; border-radius: 12px; overflow: hidden; background: #f5f7fa; }
.tuPianQu img { width: 100%; height: 100%; object-fit: cover; }
.tuPianQu .tuPianZhanWei { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 72px; color: #cdd6e4; background: linear-gradient(135deg, #eef3ff, #e7fbf5); }
.youCe { flex: 1; display: flex; flex-direction: column; }
.biaoTiHang { display: flex; align-items: center; gap: 12px; }
.biaoTiHang h1 { font-size: 24px; margin: 0; color: var(--wenZi); }
.jiaGe { color: var(--hongSe); font-size: 32px; font-weight: 800; margin: 14px 0; }
.miaoShu { color: var(--wenZiEr); line-height: 1.7; background: var(--beiJing); padding: 14px 16px; border-radius: 12px; }
.maiJiaKaPian { display: flex; align-items: center; gap: 12px; padding: 14px; border: 1px solid var(--bianKuang); border-radius: 12px; margin: 18px 0; }
.maiJiaTouXiang { background: var(--zhuTiDu); color: #fff; font-weight: 700; }
.maiJiaMing { font-weight: 600; color: var(--wenZi); }
.maiJiaXiaoMing { font-size: 12px; color: var(--wenZiSan); margin-top: 2px; }
.caoZuoHang { margin-top: auto; }
.caoZuoHang .el-button { padding: 12px 28px; }

/* ===== 看了又看（协同过滤推荐） ===== */
.xiangGuanKaPian { margin-top: 22px; }
.xiangGuanTou { display: flex; align-items: baseline; gap: 10px; }
.xiangGuanTi { display: flex; align-items: center; gap: 6px; font-size: 17px; font-weight: 700; color: var(--wenZi); }
.xiangGuanTi .el-icon { color: var(--zhuSe); }
.xiangGuanFu { font-size: 12.5px; color: var(--wenZiSan); }
.xiangGuanLie {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 16px;
}
.xiangGuanXiang { cursor: pointer; }
.xiangGuanXiang:hover .xiangGuanMing { color: var(--zhuSe); }
.xiangGuanTu {
  width: 100%;
  aspect-ratio: 1 / 1;
  border-radius: 12px;
  overflow: hidden;
  background: var(--beiJing);
}
.xiangGuanTu img { width: 100%; height: 100%; object-fit: cover; display: block; }
.xiangGuanTu .zhanWei {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--wenZiSan);
  font-size: 24px;
}
.xiangGuanMing {
  margin-top: 8px;
  font-size: 13px;
  color: var(--wenZi);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color .2s ease;
}
.xiangGuanJia { margin-top: 4px; font-size: 14px; font-weight: 800; color: var(--hongSe); }
</style>
