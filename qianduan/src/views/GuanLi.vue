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
        <el-menu-item index="lunbo"><el-icon><Picture /></el-icon><span>轮播图管理</span></el-menu-item>
        <el-menu-item index="gonggao"><el-icon><Bell /></el-icon><span>公告管理</span></el-menu-item>
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
              <div v-if="t.fuZhu" class="fuZhu">{{ t.fuZhu }}</div>
            </div>
          </div>

          <div class="fenXiHang">
            <div class="fenXiKa">
              <h4><el-icon><PieChart /></el-icon> 分类商品分布</h4>
              <div v-if="!(tongJi.fenLeiFenBu && tongJi.fenLeiFenBu.length)" class="kongWen">暂无数据</div>
              <div v-else class="tiaoLie">
                <div v-for="f in tongJi.fenLeiFenBu" :key="f.mingCheng" class="tiaoHang">
                  <span class="tiaoMing">{{ f.mingCheng }}</span>
                  <div class="tiaoWai"><div class="tiaoNei" :style="{ width: baiFenBi(f.shu) + '%' }"></div></div>
                  <span class="tiaoShu">{{ f.shu }}</span>
                </div>
              </div>
            </div>

            <div class="fenXiKa">
              <h4><el-icon><DataAnalysis /></el-icon> 订单状态分布</h4>
              <div class="zhuangTaiWangGe">
                <div v-for="d in dingDanFenBuLie" :key="d.ming" class="zhuangTaiKa" :class="d.lei">
                  <b>{{ d.shu }}</b><span>{{ d.ming }}</span>
                </div>
              </div>
              <div class="chengJiao">已完成成交额：<b>￥{{ tongJi.chengJiaoE || 0 }}</b></div>
            </div>
          </div>
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
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button v-if="!row.guanLiYuan" type="primary" link size="small" @click="sheZhiGuanLiYuan(row, true)">设为管理员</el-button>
                <el-button v-else type="warning" link size="small" @click="sheZhiGuanLiYuan(row, false)">取消管理员</el-button>
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
            <el-table-column label="操作" width="190" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.zhuangTai === 0" type="warning" link size="small" @click="xiaJia(row.id)">下架</el-button>
                <el-button v-if="row.zhuangTai === 2" type="success" link size="small" @click="shangJia(row.id)">恢复上架</el-button>
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
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.zhuangTai === 0" type="primary" link size="small" @click="gengXinDingDan(row.id, 1)">发货</el-button>
                <el-button v-if="row.zhuangTai === 1" type="success" link size="small" @click="gengXinDingDan(row.id, 2)">完成</el-button>
                <el-button v-if="row.zhuangTai === 0 || row.zhuangTai === 1" type="danger" link size="small" @click="gengXinDingDan(row.id, 3)">取消</el-button>
                <el-button type="danger" link size="small" @click="shanChuDingDan(row.id)">删除</el-button>
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

        <!-- 轮播图管理 -->
        <div v-else-if="active === 'lunbo'">
          <div class="gongJu">
            <el-button type="primary" @click="daKaiLunBo()"><el-icon><Plus /></el-icon> 新增轮播图</el-button>
            <span class="tiShi">启用的轮播图会按「排序」从小到大展示在首页顶部</span>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column label="预览" width="160">
              <template #default="{ row }">
                <el-image :src="row.tuPian" fit="cover" class="lunBoYuLan" :preview-src-list="[row.tuPian]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="biaoTi" label="主标题" />
            <el-table-column prop="fuBiaoTi" label="副标题" show-overflow-tooltip />
            <el-table-column prop="lianJie" label="跳转链接" width="140" show-overflow-tooltip />
            <el-table-column prop="paiXu" label="排序" width="80" />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.zhuangTai === 0 ? 'success' : 'info'" size="small">{{ row.zhuangTai === 0 ? '启用' : '停用' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="daKaiLunBo(row)">编辑</el-button>
                <el-button :type="row.zhuangTai === 0 ? 'warning' : 'success'" link size="small" @click="qieHuanLunBoZt(row)">{{ row.zhuangTai === 0 ? '停用' : '启用' }}</el-button>
                <el-button type="danger" link size="small" @click="shanChuLunBo(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 公告管理 -->
        <div v-else-if="active === 'gonggao'">
          <div class="gongJu">
            <el-button type="primary" @click="daKaiGongGao()"><el-icon><Plus /></el-icon> 发布公告</el-button>
            <span class="tiShi">显示中的公告会出现在首页，最新 5 条</span>
          </div>
          <el-table :data="lieBiao" border stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="biaoTi" label="标题" width="220" show-overflow-tooltip />
            <el-table-column prop="neiRong" label="内容" show-overflow-tooltip />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.zhuangTai === 0 ? 'success' : 'info'" size="small">{{ row.zhuangTai === 0 ? '显示' : '隐藏' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发布时间" width="170">
              <template #default="{ row }">{{ shiJian(row.chuangJianShiJian) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="daKaiGongGao(row)">编辑</el-button>
                <el-button :type="row.zhuangTai === 0 ? 'warning' : 'success'" link size="small" @click="qieHuanGongGaoZt(row)">{{ row.zhuangTai === 0 ? '隐藏' : '显示' }}</el-button>
                <el-button type="danger" link size="small" @click="shanChuGongGao(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>

    <!-- 轮播图编辑弹窗 -->
    <el-dialog v-model="lunBoDuiHua" :title="lunBoBiaoDan.id ? '编辑轮播图' : '新增轮播图'" width="580px">
      <el-form :model="lunBoBiaoDan" label-width="90px">
        <el-form-item label="轮播图片">
          <div class="tuPianShangChuan">
            <div class="yuLanKuang" @click="xuanTuPian">
              <img v-if="lunBoBiaoDan.tuPian" :src="lunBoBiaoDan.tuPian" alt="预览" />
              <div v-else class="kongTu">
                <span class="jiaHao">+</span>
                <span>点击选择本地图片</span>
              </div>
              <div class="yuLanZheZhao" v-if="lunBoBiaoDan.tuPian">点击更换图片</div>
            </div>
            <div class="caoZuoHang">
              <el-button class="shangChuanAnNiu" :loading="shangChuanZhong" @click="xuanTuPian">本地上传</el-button>
              <el-button v-if="lunBoBiaoDan.tuPian" class="caiJianAnNiu" @click="caiJianDangQianTu">裁剪调整</el-button>
            </div>
            <el-input v-model="lunBoBiaoDan.tuPian" size="small" placeholder="图片地址，也可直接粘贴外链" />
            <input ref="wenJianShuRu" type="file" accept="image/png,image/jpeg,image/webp,image/gif" class="yinCangWenJian" @change="xuanWenJian" />
            <div class="biaoDanTiShi">建议 <b>16:9</b> 横图（1920×1080、2048×1152）。选图后可拖动、缩放，框内区域就是首页的展示效果，裁剪后自动上传。</div>
          </div>
        </el-form-item>
        <el-form-item label="主标题"><el-input v-model="lunBoBiaoDan.biaoTi" placeholder="如：校园闲置 · 安心流转" /></el-form-item>
        <el-form-item label="副标题"><el-input v-model="lunBoBiaoDan.fuBiaoTi" placeholder="如：教材 / 数码 / 好物，低价淘" /></el-form-item>
        <el-form-item label="跳转链接"><el-input v-model="lunBoBiaoDan.lianJie" placeholder="如 /shangpin/1，留空则不跳转" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="lunBoBiaoDan.paiXu" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="lunBoBiaoDan.zhuangTai" :active-value="0" :inactive-value="1" active-text="启用" inactive-text="停用" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="lunBoDuiHua = false">取消</el-button>
        <el-button type="primary" @click="baoCunLunBo">保存</el-button>
      </template>
    </el-dialog>

    <!-- 轮播图裁剪弹窗 -->
    <el-dialog v-model="caiJianDuiHua" title="裁剪轮播图" width="540px" append-to-body :close-on-click-modal="false">
      <div class="caiJianTiShi">图片不动，拖动白色取景框选择要展示的区域；下方滑块调整框大小，框内即首页 16:9 效果。</div>
      <div class="caiJianShiKou">
        <img :src="caiJianYuanTu" :style="caiJianTuYangShi" draggable="false" alt="待裁剪图片" />
        <div class="caiJianKuang" :style="caiJianKuangYangShi" @mousedown.prevent="kaiShiTuoKuang">
          <span class="caiJianJiao jiao1"></span>
          <span class="caiJianJiao jiao2"></span>
          <span class="caiJianJiao jiao3"></span>
          <span class="caiJianJiao jiao4"></span>
        </div>
      </div>
      <div class="suoFangHang">
        <span class="suoFangBiaoQian">框大小</span>
        <el-slider v-model="kuangBiLi" :min="20" :max="100" :show-tooltip="false" size="small" />
        <span class="suoFangZhiBaiFen">{{ kuangBiLi }}%</span>
      </div>
      <template #footer>
        <el-button @click="caiJianDuiHua = false">取消</el-button>
        <el-button class="shangChuanAnNiu" :loading="shangChuanZhong" @click="queRenCaiJian">确定并上传</el-button>
      </template>
    </el-dialog>

    <!-- 公告编辑弹窗 -->
    <el-dialog v-model="gongGaoDuiHua" :title="gongGaoBiaoDan.id ? '编辑公告' : '发布公告'" width="580px">
      <el-form :model="gongGaoBiaoDan" label-width="80px">
        <el-form-item label="标题"><el-input v-model="gongGaoBiaoDan.biaoTi" placeholder="公告标题" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="gongGaoBiaoDan.neiRong" type="textarea" :rows="5" placeholder="公告内容" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="gongGaoBiaoDan.zhuangTai" :active-value="0" :inactive-value="1" active-text="显示" inactive-text="隐藏" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gongGaoDuiHua = false">取消</el-button>
        <el-button type="primary" @click="baoCunGongGao">保存</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import jieKou from '@/api/jieKou'
import { ElMessage, ElMessageBox } from 'element-plus'

const luYou = useRouter()
const guanLiYuan = ref(JSON.parse(localStorage.getItem('guanLiYuan') || '{}'))

const active = ref('shouye')
const yeMianMing = computed(() => ({
  shouye: '数据概览', yonghu: '用户管理', shangpin: '商品管理', dingdan: '订单管理',
  fenlei: '分类管理', lunbo: '轮播图管理', gonggao: '公告管理'
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
  { biaoQian: '用户总数', zhi: tongJi.value.yongHuShu ?? '—', fuZhu: `今日 +${tongJi.value.jinRiYongHu ?? 0}` },
  { biaoQian: '商品总数', zhi: tongJi.value.shangPinShu ?? '—', fuZhu: `今日 +${tongJi.value.jinRiShangPin ?? 0}` },
  { biaoQian: '在售商品', zhi: tongJi.value.zaiShouShu ?? '—', fuZhu: '' },
  { biaoQian: '订单总数', zhi: tongJi.value.dingDanShu ?? '—', fuZhu: '' }
])
const dingDanFenBuLie = computed(() => {
  const d = tongJi.value.dingDanFenBu || {}
  return [
    { ming: '待发货', shu: d.daiFaHuo ?? 0, lei: 'zt1' },
    { ming: '待收货', shu: d.daiShouHuo ?? 0, lei: 'zt2' },
    { ming: '已完成', shu: d.yiWanCheng ?? 0, lei: 'zt3' },
    { ming: '已取消', shu: d.yiQuXiao ?? 0, lei: 'zt4' }
  ]
})
const zuiDaFenLei = computed(() => {
  const shuLie = (tongJi.value.fenLeiFenBu || []).map(f => f.shu)
  return Math.max(1, ...shuLie)
})
const baiFenBi = (shu) => Math.round(((shu || 0) / zuiDaFenLei.value) * 100)

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
  } else if (active.value === 'lunbo') {
    res = await jieKou.adminLunBo()
  } else if (active.value === 'gonggao') {
    res = await jieKou.adminGongGao()
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
const sheZhiGuanLiYuan = async (row, shi) => {
  try {
    await ElMessageBox.confirm(`确定${shi ? '将「' + row.yongHuMing + '」设为管理员' : '取消「' + row.yongHuMing + '」的管理员权限'}吗？`, '提示', { type: 'warning' })
    const res = await jieKou.adminSheZhiGuanLiYuan(row.id, shi)
    if (res.data.chengGong) { ElMessage.success('已更新'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '操作失败')
  } catch (e) { /* 取消 */ }
}
const xiaJia = async (id) => {
  const res = await jieKou.adminXiaJiaShangPin(id)
  if (res.data.chengGong) { ElMessage.success('已下架'); loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}
const shangJia = async (id) => {
  const res = await jieKou.adminShangJiaShangPin(id)
  if (res.data.chengGong) { ElMessage.success('已恢复上架'); loadLieBiao() }
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
const shanChuDingDan = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该订单吗？此操作不可恢复。', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuDingDan(id)
    if (res.data.chengGong) { ElMessage.success('已删除'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '删除失败')
  } catch (e) { /* 取消 */ }
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

// ===== 轮播图 =====
const lunBoDuiHua = ref(false)
const lunBoBiaoDan = ref({})
const daKaiLunBo = (row) => {
  lunBoBiaoDan.value = row
    ? { ...row }
    : { biaoTi: '', fuBiaoTi: '', tuPian: '/images/lunbo/banner1.jpg', lianJie: '/', paiXu: 0, zhuangTai: 0 }
  lunBoDuiHua.value = true
}
const baoCunLunBo = async () => {
  if (!lunBoBiaoDan.value.tuPian || !lunBoBiaoDan.value.tuPian.trim()) {
    ElMessage.warning('请填写轮播图片地址'); return
  }
  const res = await jieKou.adminBaoCunLunBo(lunBoBiaoDan.value)
  if (res.data.chengGong) { ElMessage.success('已保存'); lunBoDuiHua.value = false; loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '保存失败')
}
const wenJianShuRu = ref(null)
const shangChuanZhong = ref(false)
const caiJianDuiHua = ref(false)
const caiJianYuanTu = ref('')
const caiJianImg = ref(null)
const caiJianKuang = ref({ left: 0, top: 0, w: 0, h: 0 })
const kuangBiLi = ref(100)
const SHI_KUAN = 480
const SHI_GAO = 270

const xuanTuPian = () => {
  if (wenJianShuRu.value) {
    wenJianShuRu.value.value = ''
    wenJianShuRu.value.click()
  }
}
// 图片以 contain 完整显示，算出显示尺寸与位置（图片不动）
const tuXianShi = computed(() => {
  const img = caiJianImg.value
  if (!img) return null
  const base = Math.min(SHI_KUAN / img.naturalWidth, SHI_GAO / img.naturalHeight)
  const dw = img.naturalWidth * base
  const dh = img.naturalHeight * base
  return { base, dw, dh, imgLeft: (SHI_KUAN - dw) / 2, imgTop: (SHI_GAO - dh) / 2 }
})
// 图片内可容纳的最大 16:9 框宽
const kuangZuiDa = computed(() => {
  const t = tuXianShi.value
  if (!t) return 0
  return Math.min(t.dw, t.dh * 16 / 9)
})
const caiJianTuYangShi = computed(() => {
  const t = tuXianShi.value
  if (!t) return {}
  return { left: t.imgLeft + 'px', top: t.imgTop + 'px', width: t.dw + 'px', height: t.dh + 'px' }
})
const caiJianKuangYangShi = computed(() => {
  const k = caiJianKuang.value
  return { left: k.left + 'px', top: k.top + 'px', width: k.w + 'px', height: k.h + 'px' }
})
// 调整框大小（保持中心），并限制在图片范围内
const gengXinKuang = () => {
  const t = tuXianShi.value
  if (!t) return
  const w = kuangZuiDa.value * kuangBiLi.value / 100
  const h = w * 9 / 16
  const k = caiJianKuang.value
  const cx = k.left + k.w / 2
  const cy = k.top + k.h / 2
  let left = cx - w / 2
  let top = cy - h / 2
  left = Math.min(t.imgLeft + t.dw - w, Math.max(t.imgLeft, left))
  top = Math.min(t.imgTop + t.dh - h, Math.max(t.imgTop, top))
  caiJianKuang.value = { left, top, w, h }
}
let tuoDongZhong = null
const yiDongKuang = (e) => {
  if (!tuoDongZhong) return
  const t = tuXianShi.value
  if (!t) return
  const k = caiJianKuang.value
  let left = tuoDongZhong.ol + (e.clientX - tuoDongZhong.cx)
  let top = tuoDongZhong.ot + (e.clientY - tuoDongZhong.cy)
  left = Math.min(t.imgLeft + t.dw - k.w, Math.max(t.imgLeft, left))
  top = Math.min(t.imgTop + t.dh - k.h, Math.max(t.imgTop, top))
  caiJianKuang.value = { ...k, left, top }
}
const jieShuTuoKuang = () => {
  tuoDongZhong = null
  window.removeEventListener('mousemove', yiDongKuang)
  window.removeEventListener('mouseup', jieShuTuoKuang)
}
const kaiShiTuoKuang = (e) => {
  if (!caiJianImg.value) return
  tuoDongZhong = { cx: e.clientX, cy: e.clientY, ol: caiJianKuang.value.left, ot: caiJianKuang.value.top }
  window.addEventListener('mousemove', yiDongKuang)
  window.addEventListener('mouseup', jieShuTuoKuang)
}
watch(kuangBiLi, gengXinKuang)

const daKaiCaiJian = (laiYuan) => {
  const tu = new Image()
  tu.onload = () => {
    caiJianImg.value = tu
    kuangBiLi.value = 100
    const t = tuXianShi.value
    const w = kuangZuiDa.value
    const h = w * 9 / 16
    caiJianKuang.value = { left: t.imgLeft + (t.dw - w) / 2, top: t.imgTop + (t.dh - h) / 2, w, h }
    caiJianDuiHua.value = true
  }
  tu.onerror = () => ElMessage.error('图片加载失败，无法裁剪')
  tu.src = laiYuan
}
const xuanWenJian = (e) => {
  const wenJian = e.target.files && e.target.files[0]
  if (!wenJian) return
  if (wenJian.size > 8 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 8MB')
    return
  }
  caiJianYuanTu.value = URL.createObjectURL(wenJian)
  daKaiCaiJian(caiJianYuanTu.value)
}
const caiJianDangQianTu = () => {
  if (!lunBoBiaoDan.value.tuPian) {
    ElMessage.warning('还没有图片，请先本地上传')
    return
  }
  caiJianYuanTu.value = lunBoBiaoDan.value.tuPian
  daKaiCaiJian(lunBoBiaoDan.value.tuPian)
}
const shangChuanWenJian = async (wenJian, ming) => {
  const biaoDan = new FormData()
  biaoDan.append('file', wenJian, ming)
  shangChuanZhong.value = true
  try {
    const res = await jieKou.shangChuanTuPian(biaoDan)
    if (res.data.chengGong) {
      lunBoBiaoDan.value.tuPian = res.data.url
      caiJianDuiHua.value = false
      ElMessage.success('已裁剪并上传，图片地址已填入')
    } else {
      ElMessage.error(res.data.xinXi || '上传失败')
    }
  } catch (err) {
    ElMessage.error('上传失败，请确认后端已启动且已登录管理员')
  } finally {
    shangChuanZhong.value = false
  }
}
const queRenCaiJian = () => {
  const img = caiJianImg.value
  const t = tuXianShi.value
  if (!img || !t) return
  const k = caiJianKuang.value
  const sx = (k.left - t.imgLeft) / t.base
  const sy = (k.top - t.imgTop) / t.base
  const sw = k.w / t.base
  const sh = k.h / t.base
  const huaBu = document.createElement('canvas')
  huaBu.width = 1600
  huaBu.height = 900
  const ctx = huaBu.getContext('2d')
  ctx.fillStyle = '#ffffff'
  ctx.fillRect(0, 0, 1600, 900)
  ctx.imageSmoothingEnabled = true
  ctx.imageSmoothingQuality = 'high'
  try {
    ctx.drawImage(img, sx, sy, sw, sh, 0, 0, 1600, 900)
    huaBu.toBlob((blob) => {
      if (blob) shangChuanWenJian(blob, 'lunbo-' + Date.now() + '.jpg')
      else ElMessage.error('裁剪失败，请重试')
    }, 'image/jpeg', 0.92)
  } catch (err) {
    ElMessage.error('该图片受跨域限制无法裁剪，请下载后本地上传')
  }
}
const qieHuanLunBoZt = async (row) => {
  const res = await jieKou.adminLunBoZhuangTai(row.id, row.zhuangTai === 0 ? 1 : 0)
  if (res.data.chengGong) { ElMessage.success('已更新'); loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}
const shanChuLunBo = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该轮播图吗？', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuLunBo(id)
    if (res.data.chengGong) { ElMessage.success('已删除'); loadLieBiao() }
    else ElMessage.error(res.data.xinXi || '删除失败')
  } catch (e) { /* 取消 */ }
}

// ===== 公告 =====
const gongGaoDuiHua = ref(false)
const gongGaoBiaoDan = ref({})
const daKaiGongGao = (row) => {
  gongGaoBiaoDan.value = row ? { ...row } : { biaoTi: '', neiRong: '', zhuangTai: 0 }
  gongGaoDuiHua.value = true
}
const baoCunGongGao = async () => {
  if (!gongGaoBiaoDan.value.biaoTi || !gongGaoBiaoDan.value.biaoTi.trim()) {
    ElMessage.warning('请填写公告标题'); return
  }
  const res = await jieKou.adminBaoCunGongGao(gongGaoBiaoDan.value)
  if (res.data.chengGong) { ElMessage.success('已保存'); gongGaoDuiHua.value = false; loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '保存失败')
}
const qieHuanGongGaoZt = async (row) => {
  const res = await jieKou.adminGongGaoZhuangTai(row.id, row.zhuangTai === 0 ? 1 : 0)
  if (res.data.chengGong) { ElMessage.success('已更新'); loadLieBiao() }
  else ElMessage.error(res.data.xinXi || '操作失败')
}
const shanChuGongGao = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该公告吗？', '提示', { type: 'warning' })
    const res = await jieKou.adminShanChuGongGao(id)
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
.gongJu { display: flex; gap: 12px; align-items: center; margin-bottom: 16px; }
.tiShi { font-size: 12.5px; color: var(--wenZiSan); }

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
.tongJiKaPian .fuZhu { font-size: 12px; color: var(--wenZiSan); margin-top: 6px; }

.fenXiHang { display: grid; grid-template-columns: 1.2fr 1fr; gap: 18px; margin-top: 18px; }
@media (max-width: 1100px) { .fenXiHang { grid-template-columns: 1fr; } }
.fenXiKa {
  background: #fff;
  border: 1px solid var(--bianKuang);
  border-radius: var(--yuanJiao);
  box-shadow: var(--banYing);
  padding: 20px 22px;
}
.fenXiKa h4 { display: flex; align-items: center; gap: 8px; font-size: 15px; margin: 0 0 16px; color: var(--wenZi); }
.fenXiKa h4 .el-icon { color: var(--zhuSe); }
.kongWen { font-size: 13px; color: var(--wenZiSan); padding: 20px 0; }

.tiaoLie { display: flex; flex-direction: column; gap: 12px; }
.tiaoHang { display: flex; align-items: center; gap: 12px; }
.tiaoMing { width: 80px; font-size: 13px; color: var(--wenZiEr); flex-shrink: 0; }
.tiaoWai { flex: 1; height: 10px; border-radius: 999px; background: #eef1f6; overflow: hidden; }
.tiaoNei { height: 100%; border-radius: 999px; background: linear-gradient(90deg, #2f6bff, #15c5b8); transition: width .4s ease; }
.tiaoShu { width: 34px; text-align: right; font-size: 13px; font-weight: 700; color: var(--wenZi); }

.zhuangTaiWangGe { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }
.zhuangTaiKa {
  border-radius: 14px;
  padding: 16px 10px;
  text-align: center;
  background: #f7f9fc;
  border: 1px solid var(--bianKuang);
}
.zhuangTaiKa b { display: block; font-size: 24px; font-weight: 800; color: var(--wenZi); }
.zhuangTaiKa span { font-size: 12.5px; color: var(--wenZiEr); }
.zhuangTaiKa.zt1 b { color: #e6a23c; }
.zhuangTaiKa.zt2 b { color: #2f6bff; }
.zhuangTaiKa.zt3 b { color: #15a06b; }
.zhuangTaiKa.zt4 b { color: #9aa4b2; }
.chengJiao { margin-top: 16px; font-size: 13px; color: var(--wenZiEr); }
.chengJiao b { color: var(--hongSe); font-size: 16px; }

.lunBoYuLan { width: 132px; height: 62px; border-radius: 8px; display: block; }
.yuLan { margin-top: 10px; width: 100%; }
.yuLan img { width: 100%; height: 150px; object-fit: cover; border-radius: 10px; border: 1px solid var(--bianKuang); }

.fenYe { display: flex; justify-content: center; margin-top: 20px; }

.biaoDanTiShi { font-size: 12px; color: #909399; line-height: 1.5; margin-top: 6px; }
.yinCangWenJian { display: none; }

/* ===== 轮播图片上传区 ===== */
.tuPianShangChuan { width: 100%; max-width: 400px; }
.yuLanKuang {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  border-radius: 14px;
  overflow: hidden;
  background: #f5f7fb;
  border: 1px dashed #c9d3e6;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color .2s ease, box-shadow .2s ease;
}
.yuLanKuang:hover { border-color: #2f6bff; box-shadow: 0 12px 28px rgba(47, 107, 255, .16); }
.yuLanKuang img { width: 100%; height: 100%; object-fit: cover; display: block; }
.kongTu { display: flex; flex-direction: column; align-items: center; gap: 6px; color: #97a3b8; font-size: 12.5px; }
.kongTu .jiaHao { font-size: 26px; line-height: 1; color: #b9c4d6; }
.yuLanZheZhao {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(10, 20, 40, .46);
  color: #fff;
  font-size: 13px;
  letter-spacing: .5px;
  opacity: 0;
  transition: opacity .2s ease;
}
.yuLanKuang:hover .yuLanZheZhao { opacity: 1; }
.caoZuoHang { display: flex; gap: 10px; margin: 12px 0 10px; }
.shangChuanAnNiu {
  border: none !important;
  border-radius: 10px !important;
  height: auto !important;
  padding: 9px 22px !important;
  font-weight: 600;
  color: #fff !important;
  background: linear-gradient(135deg, #2f6bff 0%, #15c5b8 100%) !important;
  box-shadow: 0 8px 20px rgba(47, 107, 255, .26);
  transition: transform .2s ease, box-shadow .2s ease;
}
.shangChuanAnNiu:hover { transform: translateY(-2px); box-shadow: 0 12px 26px rgba(47, 107, 255, .34); }
.caiJianAnNiu { border-radius: 10px !important; }

/* ===== 裁剪弹窗（移动取景框） ===== */
.caiJianTiShi { font-size: 12.5px; color: #7a869a; line-height: 1.6; margin-bottom: 12px; }
.caiJianShiKou {
  position: relative;
  width: 480px;
  height: 270px;
  margin: 0 auto;
  overflow: hidden;
  border-radius: 8px;
  background: #0b1117;
  user-select: none;
}
.caiJianShiKou img { position: absolute; display: block; max-width: none; pointer-events: none; }
.caiJianKuang {
  position: absolute;
  box-sizing: border-box;
  border: 2px solid #fff;
  box-shadow: 0 0 0 9999px rgba(10, 20, 40, .52);
  cursor: move;
}
.caiJianKuang:active { cursor: grabbing; }
.caiJianJiao { position: absolute; width: 14px; height: 14px; border: 3px solid #fff; pointer-events: none; }
.jiao1 { left: -2px; top: -2px; border-right: none; border-bottom: none; }
.jiao2 { right: -2px; top: -2px; border-left: none; border-bottom: none; }
.jiao3 { left: -2px; bottom: -2px; border-right: none; border-top: none; }
.jiao4 { right: -2px; bottom: -2px; border-left: none; border-top: none; }
.suoFangHang { display: flex; align-items: center; gap: 12px; margin-top: 18px; padding: 0 4px; }
.suoFangBiaoQian { font-size: 12.5px; color: #7a869a; flex-shrink: 0; }
.suoFangHang .el-slider { flex: 1; }
.suoFangZhiBaiFen { font-size: 12.5px; color: #2f6bff; font-weight: 600; width: 40px; text-align: right; flex-shrink: 0; }
</style>
